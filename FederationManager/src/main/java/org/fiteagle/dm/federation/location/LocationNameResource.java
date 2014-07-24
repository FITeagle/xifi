package org.fiteagle.dm.federation.location;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Stateless
@Path("/locationname")
public class LocationNameResource {

	Logger log = LoggerFactory.getLogger(getClass());
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationName(@QueryParam("lat") String latitude, @QueryParam("lng") String longitude){
		
		LocationServiceClient client = new LocationServiceClient();
		log.info("lat = "+ latitude  + "   lng = " + longitude  );
		//TODO check if name is taken
		
		String name = client.getLocation(longitude, latitude);
		RegionNameSuggestion suggestion = new RegionNameSuggestion();
		if(name != null){
		String name1 = name.split("-")[0].trim();
		String name2 = name.split("-")[1].trim();
		
		String[] names = {name, name1, name2};
		suggestion.setNames(names);
		}
		return Response.ok().entity(suggestion).build();
	}
}
