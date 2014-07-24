package org.fiteagle.dm.federation;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

import org.fiteagle.dm.federation.annotation.PATCH;
import org.fiteagle.dm.federation.dao.EndpointDAO;
import org.fiteagle.dm.federation.model.Endpoint;
import org.fiteagle.dm.federation.model.LinkInfo;

@Stateless
@Path("/endpoints")
public class EndpointResource {

	@EJB
	EndpointDAO endPointDAO;
	
	@GET
	@Produces("application/hal+json")
	public Response findEndpoints(@Context UriInfo uriInfo, @QueryParam("service_id") String serviceId, @QueryParam("region")String regionId, @QueryParam("interface") String interfaceType, @QueryParam("page") String page, @QueryParam("per_page") String per_page){
		List<Endpoint> endpoints = endPointDAO.findEndpoints(serviceId, regionId, interfaceType);
		
		for(Endpoint e: endpoints){
			e.addLink("self", uriInfo.getAbsolutePath().toString()+ "/" + e.getId());

		
		}
		
		GenericEntity<List<Endpoint>> entity = new GenericEntity<List<Endpoint>>(endpoints){};
		return Response.ok(entity).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	public Response addEndpoint(@Context UriInfo uriInfo, Endpoint endpoint) throws URISyntaxException{
		
		Endpoint created = endPointDAO.addEndpoint(endpoint);
		created.addLink("self", uriInfo.getAbsolutePath().toString()+ "/" + created.getId());

		Response res = Response.created(new URI(uriInfo.getAbsolutePath().toString()+ "/" + created.getId())).entity(created).build();
		return res;
	}
	
	@GET
	@Path("/{endpointid}")
	@Produces("application/hal+json")
	public Response getEndpoint(@Context UriInfo uriInfo, @PathParam("endpointid") long endpointId){
		Endpoint endpoint = endPointDAO.findEndpoint(endpointId);
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
	public Response updateEndpoint(@Context UriInfo uriInfo, @PathParam("endpointid") long endpointId, Endpoint endpoint) throws URISyntaxException{
		Endpoint updated = endPointDAO.updateEndpoint(endpointId,endpoint);
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
