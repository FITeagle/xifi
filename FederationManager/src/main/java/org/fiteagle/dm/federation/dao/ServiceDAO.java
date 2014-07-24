package org.fiteagle.dm.federation.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.fiteagle.dm.federation.model.Service;

@Stateless
public class ServiceDAO {

	@PersistenceContext(unitName="registryDB")
	EntityManager em;
	@EJB
	EndpointDAO endpointDAO;
	public Service createService(Service service){
		em.persist(service);
		return service;
	}

	public List<Service> findServices(String type) {
		CriteriaBuilder ctb = em.getCriteriaBuilder();
		CriteriaQuery<Service> query = ctb.createQuery(Service.class);
		Root<Service> root = query.from(Service.class);
		query.select(root);
		List<Predicate> predicateList = new ArrayList<>();
		Predicate typePred;
		if(type != null){
			typePred = ctb.equal(root.get("type"), type);
			if(typePred!= null){
				predicateList.add(typePred);
			}
		}
		if(predicateList.size()>0){
			Predicate[] predicates = new Predicate[predicateList.size()];
			predicateList.toArray(predicates);
			query.where(predicates);
		}
		return em.createQuery(query).getResultList();
	}



	public Service findService(long serviceid) {
		return em.find(Service.class, serviceid);
	}

	public Service updateService(Service service) {
		return em.merge(service);
	}

	public void deleteService(long serviceid) {
		Service s = em.getReference(Service.class, serviceid);
		if(s!= null){
			try{
				em.remove(s);
				endpointDAO.deleteEndpointForServiceId(serviceid);
			}catch(EntityNotFoundException e){
				
			}
		}
		
		
	}
}
