package org.fiteagle.dm.federation.dao;

import java.util.ArrayList;
import java.util.Calendar;
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

import org.fiteagle.dm.federation.model.ContactInformation;
import org.fiteagle.dm.federation.model.Member;
import org.fiteagle.dm.federation.model.Region;
import org.fiteagle.dm.federation.model.RegionStatus;

import com.uaihebert.factory.EasyCriteriaFactory;
import com.uaihebert.model.EasyCriteria;

@Stateless
public class RegionDAO {

	@PersistenceContext(unitName = "registryDB")
	EntityManager em;

	@EJB
	EndpointDAO endpointDao;

	public Region createRegion(Region region) {
		RegionStatus status = null;
		System.out.println(region.getRegionStatus());
		if (region.getRegionStatus() == null) {
			status = new RegionStatus();
			status.setTimestamp(Calendar.getInstance().getTimeInMillis());
			status.setStatus("created");
			
			
		}else{
			status = region.getRegionStatus();
		}
		region.setRegionStatus(null);

		em.persist(region);
	
		region.setRegionStatus(status);
		status.setRegion(region.getUUID());
		
		
		em.merge(region);
		return region;
	}

	public Region findRegion(long regionid) {
		Region r = em.find(Region.class, regionid);
		return r;

	}

	public List<Region> findRegions(String country, String status, String publicNode, String adminUsername) {
		EasyCriteria< Region> easyCriteria =  EasyCriteriaFactory.createQueryCriteria(em, Region.class);
		if(country != null)
			easyCriteria.andEquals("country", country);
		
		if(publicNode != null)
			easyCriteria.andEquals("publicNode", publicNode);
		
		if(status!= null){
			easyCriteria.innerJoin("regionStatus");
			easyCriteria.andEquals("regionStatus.status", status);
		}
		if(adminUsername != null)
			easyCriteria.andEquals("adminUsername", adminUsername);
		
		List<Region> regions = easyCriteria.getResultList();
		return regions;
	}

	public Region updateRegion(Region r) {
		Region former = em.find(Region.class, r.getUUID());
		if (former != null) {
			if (r.getAdminUsername() != null) {
				former.setAdminUsername(r.getAdminUsername());
			}
			if (r.getCountry() != null) {
				former.setCountry(r.getCountry());
			}
			if (r.getLatitude() != null) {
				former.setLatitude(r.getLatitude());
			}
			if (r.getLongitude() != null) {
				former.setLongitude(r.getLongitude());
			}
			if (r.getNodeType() != null) {
				former.setNodeType(r.getNodeType());
			}

            if(r.getRegionId() != null){
                former.setRegionId(r.getRegionId());
            }
			em.merge(former);
		}
		return former;
	}

	public void deleteRegion(long regionid) {
		Region r = em.getReference(Region.class, regionid);
		if (r != null)
			try {
				em.remove(r);
				endpointDao.deleteEndpointsForRegion(regionid);
			} catch (EntityNotFoundException e) {

			}

	}

	public RegionStatus findRegionStatusForId(long regionid) {
		RegionStatus regionStatus = em.find(RegionStatus.class, regionid);
		return regionStatus;
	}

	public RegionStatus updateRegionStatus(RegionStatus status) {
		Region r = em.find(Region.class, status.getRegion());
		RegionStatus updated = null;
		if (r != null) {

			updated = em.merge(status);
		}
		return updated;

	}

	public Member addMember(long regionid,
			Member member) {
		Region r = em.find(Region.class, regionid);
		r.addMember(member);
		Member created = null;
		em.merge(r);
		List<Member> members = r.getMembers();
		for (Member m : members) {
			if (m.equals(member))
				created = m;
		}
		return created;
	}

	public List<Member> getMembers(long regionid) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<Member> query = criteriaBuilder
				.createQuery(Member.class);
		Root<Member> root = query.from(Member.class);
		query.select(root);
		List<Predicate> predicateList = new ArrayList<>();
		Predicate regionidpred;
		regionidpred = criteriaBuilder.equal(root.get("region").get("uuid"),
				regionid);
		predicateList.add(regionidpred);
		
		if (predicateList.size() > 0) {
			Predicate[] predicates = new Predicate[predicateList.size()];
			predicateList.toArray(predicates);
			query.where(predicates);
		}
		return em.createQuery(query).getResultList();
	}

	public Member getMember(long memberId) {
		return em.find(Member.class, memberId);
	}

	public Member updateMember(long memberId,
			Member updated) {
		Member former = em
				.find(Member.class, memberId);
		
		em.merge(former);
		return former;
	}

	public void deleteMember(long memberId) {
		Member c = em.find(Member.class, memberId);
		c.setRegion(null);
		em.remove(c);

	}

	public ContactInformation addContactInformation(long regionid,
			ContactInformation contactInfo) {
		Region r = em.find(Region.class, regionid);
		r.addContact(contactInfo);
		ContactInformation created = null;
		em.merge(r);
		List<ContactInformation> contacts = r.getContacts();
		for (ContactInformation c : contacts) {
			if (c.equals(contactInfo))
				created = c;
		}
		return created;
	}

	public List<ContactInformation> getContacts(long regionid, String type) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<ContactInformation> query = criteriaBuilder
				.createQuery(ContactInformation.class);
		Root<ContactInformation> root = query.from(ContactInformation.class);
		query.select(root);
		List<Predicate> predicateList = new ArrayList<>();
		Predicate regionidpred, typepred;
		regionidpred = criteriaBuilder.equal(root.get("region").get("uuid"),
				regionid);
		predicateList.add(regionidpred);
		if (type != null) {
			typepred = criteriaBuilder.equal(root.get("type"), type);
			if (typepred != null) {
				predicateList.add(typepred);
			}
		}
		if (predicateList.size() > 0) {
			Predicate[] predicates = new Predicate[predicateList.size()];
			predicateList.toArray(predicates);
			query.where(predicates);
		}
		return em.createQuery(query).getResultList();
	}

	public ContactInformation getContactInfo(long contactId) {
		return em.find(ContactInformation.class, contactId);
	}

	public ContactInformation updateContactInformation(long contactId,
			ContactInformation updated) {
		ContactInformation former = em
				.find(ContactInformation.class, contactId);
		if (updated.getLocation() != null) {
			former.setLocation(updated.getLocation());
		}
		if (updated.getCountry() != null) {
			former.setCountry(updated.getCountry());
		}
		if (updated.getEmail() != null) {
			former.setEmail(updated.getEmail());
		}
		if (updated.getFax() != null) {
			former.setFax(updated.getFax());
		}
		if (updated.getName() != null) {
			former.setName(updated.getName());
		}
		if (updated.getPhone() != null) {
			former.setPhone(updated.getPhone());
		}
		if (updated.getType() != null) {
			former.setType(updated.getType());
		}

		em.merge(former);
		return former;
	}

	public void deleteContact(long contactId) {
		ContactInformation c = em.find(ContactInformation.class, contactId);
		c.setRegion(null);
		em.remove(c);

	}
}
