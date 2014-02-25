package org.fiteagle.xifi.api;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

import org.fiteagle.api.Endpoint;
import org.fiteagle.api.IEndpointDAO;
import org.fiteagle.xifi.api.annotation.PATCH;

@Stateless
@Path("/endpoints")
public class EndpointResource {

	
	IEndpointDAO endPointDAO;
	@PostConstruct
	private void init(){
		try {
			endPointDAO = (IEndpointDAO) new InitialContext().lookup("java:jboss/exported/regionManagement-0.0.1-SNAPSHOT/EndpointDAO!org.fiteagle.api.IEndpointDAO");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@GET
	@Produces("application/hal+json")
	public Response findEndpoints(@Context UriInfo uriInfo, @QueryParam("service_id") String serviceId, @QueryParam("region")String regionId, @QueryParam("interface") String interfaceType, @QueryParam("page") String page, @QueryParam("per_page") String per_page){
		List<? extends Endpoint> endpoints = endPointDAO.findEndpoints(serviceId, regionId, interfaceType);
		
		for(Endpoint e: endpoints){
			e.addLink("self", uriInfo.getAbsolutePath().toString()+ "/" + e.getId());
		}
		
		GenericEntity<List<? extends Endpoint>> entity = new GenericEntity<List<? extends Endpoint>>(endpoints){};
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
