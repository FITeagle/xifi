package org.fiteagle.xifi.api;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

import org.fiteagle.api.IEndpoint;
import org.fiteagle.api.IEndpointDAO;
import org.fiteagle.xifi.api.annotation.PATCH;

@Stateless
@Path("/endpoints")
public class EndpointResource {

	@EJB(beanInterface=IEndpointDAO.class, mappedName="IEndpointDAO")
	IEndpointDAO endPointDAO;
	
	@GET
	@Produces("application/hal+json")
	public Response findEndpoints(@Context UriInfo uriInfo, @QueryParam("service_id") String serviceId, @QueryParam("region")String regionId, @QueryParam("interface") String interfaceType, @QueryParam("page") String page, @QueryParam("per_page") String per_page){
		List<? extends IEndpoint> endpoints = endPointDAO.findEndpoints(serviceId, regionId, interfaceType);
		
		for(IEndpoint e: endpoints){
			e.addLink("self", uriInfo.getAbsolutePath().toString()+ "/" + e.getId());
		}
		
		GenericEntity<List<? extends IEndpoint>> entity = new GenericEntity<List<? extends IEndpoint>>(endpoints){};
		return Response.ok(entity).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	public Response addEndpoint(@Context UriInfo uriInfo, IEndpoint endpoint) throws URISyntaxException{
		
		IEndpoint created = endPointDAO.addEndpoint(endpoint);
		created.addLink("self", uriInfo.getAbsolutePath().toString()+ "/" + created.getId());
		Response res = Response.created(new URI(uriInfo.getAbsolutePath().toString()+ "/" + created.getId())).entity(created).build();
		return res;
	}
	
	@GET
	@Path("/{endpointid}")
	@Produces("application/hal+json")
	public Response getEndpoint(@Context UriInfo uriInfo, @PathParam("endpointid") long endpointId){
		IEndpoint endpoint = endPointDAO.findEndpoint(endpointId);
		if(endpoint == null)
			return Response.status(404).build();
		endpoint.addLink("self", uriInfo.getAbsolutePath().toString());
		Response res = Response.ok(endpoint).build();
		return res;
	}
	
	@PATCH
	@Path("/{endpointid}")
	@Produces("application/hal+json")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateEndpoint(@Context UriInfo uriInfo, @PathParam("endpointid") long endpointId, IEndpoint endpoint) throws URISyntaxException{
		IEndpoint updated = endPointDAO.updateEndpoint(endpointId,endpoint);
		updated.addLink("self", uriInfo.getAbsolutePath().toString());
		Response res = Response.created(new URI(uriInfo.getAbsolutePath().toString())).entity(updated).build();
		return res;

	}
	
	@DELETE
	@Path("/{endpointid}")
	public Response deleteEndpoint(@PathParam("endpointid") long endpointId){
		endPointDAO.deleteEndpoint(endpointId);
		return Response.noContent().build();
	}
}