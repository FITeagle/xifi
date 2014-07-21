package org.fiteagle.dm.federation.auth;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/auth")
public class UserResource {

	@EJB
	private Userstore store;
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@QueryParam("token") String token){
		User u = store.getUser(token);
		if(u != null){
			return Response.ok().entity(u).build();
		}else{
			return Response.status(404).build();
		}
		
		
	}
	@DELETE
	public Response deleteUser(@QueryParam("token") String token){
		store.deleteUser(token);
		return Response.noContent().build();
	}
	
}
