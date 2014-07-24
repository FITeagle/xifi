package org.fiteagle.dm.federation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.fiteagle.dm.federation.annotation.PATCH;
import org.fiteagle.dm.federation.dao.ServiceDAO;
import org.fiteagle.dm.federation.model.Service;

@Stateless
@Path("/services")
public class ServiceResource {
	private static final Logger LOGGER = Logger.getLogger(ServiceResource.class.getName());

	@EJB
	ServiceDAO serviceDao;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	public Response addService(@Context UriInfo uriInfo, Service service) throws URISyntaxException{
		LOGGER.log(Level.INFO, "Received add service request");
		Service created = serviceDao.createService(service);
		
		created.addLinksWithId(uriInfo.getAbsolutePath().toString());
		
		return Response.created(new URI(uriInfo.getAbsolutePath().toString() + "/"+ created.getUUID())).entity(created).build();
		
	}


	@GET
	@Produces("application/hal+json")
	public Response getAllServices(@Context UriInfo uriInfo,@QueryParam("type") String type,@QueryParam("page") String page, @QueryParam("per_page")String per_page) {
		List<Service> services = serviceDao.findServices(type);
			
	
		for(Service s : services){
			s.addLinksWithId(uriInfo.getAbsolutePath().toString());
		}
		GenericEntity<List<Service>> entity = new GenericEntity<List<Service>>(services){};
		return Response.ok(entity).build();
	}


	@GET
	@Produces("application/hal+json")
	@Path("/{serviceid}")
	public Response getService(@Context UriInfo uriInfo, @PathParam("serviceid")long serviceid) {
		Service s = serviceDao.findService(serviceid);
		if(s == null)
			return Response.status(404).build();
		s.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
		return Response.ok(s).build();
	}

	@PATCH
	@Produces("application/hal+json")
	@Path("/{serviceid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateService(@Context UriInfo uriInfo, @PathParam("serviceid") long serviceId, Service service) {
		service.setId(serviceId);
		Service updated = serviceDao.updateService(service);
		updated.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
	
		return Response.created(uriInfo.getAbsolutePath()).entity(updated).build();
	}

	@DELETE
	@Path("/{serviceid}")
	public Response deleteService(@PathParam("serviceid")long serviceid) {
		serviceDao.deleteService(serviceid);
		return Response.noContent().build();
	}
	
	
}
