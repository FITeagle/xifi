package org.fiteagle.xifi.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import org.fiteagle.api.ContactInformation;
import org.fiteagle.api.IRegionDAO;
import org.fiteagle.api.Region;
import org.fiteagle.api.RegionStatus;
import org.fiteagle.xifi.api.annotation.PATCH;



@Stateless
@Path("/regions")
public class RegionResource {
	private static final Logger LOGGER = Logger.getLogger(RegionResource.class.getName());
	//@EJB(beanInterface=IRegionDAO.class, mappedName="IRegionDAO")
	IRegionDAO regionDao;
	
	@PostConstruct
	private void init() throws NamingException{
		LOGGER.log(Level.INFO, "init method");
		regionDao = (IRegionDAO) new InitialContext().lookup("java:jboss/exported/regionManagement-0.0.1-SNAPSHOT/RegionDAO!org.fiteagle.api.IRegionDAO");

	}
	@GET
	@Path("/{regionid}")
	@Produces("application/hal+json")
	public Response getRegionInfo(@Context UriInfo uriInfo,@PathParam("regionid") final long regionid){
		LOGGER.log(Level.INFO, "getting JSON for region " + regionid);
	
		
		Region newRegion = regionDao.findRegion(regionid);
		if(newRegion == null)
			return Response.status(404).build();
		
		newRegion.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
		Response res = Response.ok(newRegion).build();
		return res;
		
	}


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	public Response postRegionInfo(@Context UriInfo uriInfo,Region region) throws URISyntaxException {
		LOGGER.log(Level.INFO, "Received post request ");
		
		Region newRegion = regionDao.createRegion(region);
		newRegion.addLinksWithId(uriInfo.getAbsolutePath().toString());
		return Response.created(new URI(uriInfo.getAbsolutePath().toString() + "/" + newRegion.getId())).entity(newRegion).build();
		
	}


	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	@Path("/{regionid}")
	public Response updateRegion(@Context UriInfo uriInfo,@PathParam("regionid") long id, Region r) throws URISyntaxException {
		LOGGER.log(Level.INFO, "Received patch request ");
		Response res;
		if((r.getId() != 0 && id != r.getId())){
			res = Response.status(400).build();
		}else{
			if(r.getId() == 0)
				r.setId(id);
		Region newRegion = regionDao.updateRegion(r);
		if(newRegion == null){
			return Response.status(404).build();
		}
		newRegion.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
		res =  Response.created(uriInfo.getAbsolutePath()).entity(newRegion).build();
		}
		return res;
		
	}

	@DELETE
	@Path("/{regionid}")
	public Response deleteRegion(@PathParam("regionid")long regionid){
		LOGGER.log(Level.INFO, "Received delete request ");
		regionDao.deleteRegion(regionid);
		return Response.noContent().build();
	}

	@GET
	@Produces("application/hal+json")
	public Response getAllRegions(@Context UriInfo uriInfo,@QueryParam("country") String country, @QueryParam("page")String page, @QueryParam("per_page") String per_page) {
		LOGGER.log(Level.INFO, "Received find All Regions");
		List<? extends Region> regions	 = regionDao.findRegions(country);
		
		for(Region r: regions){
			r.addLinksWithId(uriInfo.getAbsolutePath().toString());

		}
		GenericEntity<List<? extends Region>> entity = new GenericEntity<List<? extends Region>>(regions){};
		return Response.ok(entity).build();
		
	}
	
	@GET
	@Path("/{regionid}/status")
	@Produces("application/hal+json")
	public Response getRegionStatus(@Context UriInfo uriInfo,@PathParam("regionid") long regionid){
		LOGGER.log(Level.INFO, "Received status request for Region : "+ regionid);
		RegionStatus regionStatus = regionDao.findRegionStatusForId(regionid);
		if(regionStatus == null)
			return Response.status(404).build();
		regionStatus.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
		return Response.ok(regionStatus).build();
		
	}
	

	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	@Path("/{regionid}/status")
	public Response updateRegionStatus(@Context UriInfo uriInfo,@PathParam("regionid") long regionid, RegionStatus status){
		LOGGER.log(Level.INFO, "Received region status update for region : "+ regionid);
		status.setRegion(regionid);
		RegionStatus newRegionStatus = regionDao.updateRegionStatus(status);
		if(newRegionStatus == null)
			return Response.status(404).build();
		newRegionStatus.addLinksWithoutId(uriInfo.getAbsolutePath().toString());

		return Response.created(uriInfo.getAbsolutePath()).entity(newRegionStatus).build();
		
	}
	
	@GET
	@Path("/{regionid}/contacts")
	@Produces("application/hal+json")
	public Response getRegionContacts(@Context UriInfo uriInfo,@PathParam("regionid") long regionid, @QueryParam("type") String type){
		LOGGER.log(Level.INFO, "Received contacts request for region: "+ regionid);
		List<? extends ContactInformation> contacts = regionDao.getContacts(regionid, type);
		for(ContactInformation c: contacts){
			c.addLinksWithId(uriInfo.getAbsolutePath().toString());
		}
		GenericEntity<List<? extends ContactInformation>> entity = new GenericEntity<List<? extends ContactInformation>>(contacts){};
		return Response.ok(entity).build();
	}

	

	@POST
	@Path("/{regionid}/contacts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	public Response addContactInformation(@Context UriInfo uriInfo, @PathParam("regionid") long regionid, ContactInformation contactInfo) throws URISyntaxException {
		Response res = null;
		
		ContactInformation newContact = regionDao.addContactInforamtion(regionid, contactInfo );
		newContact.addLinksWithId(uriInfo.getAbsolutePath().toString());
		res = Response.created(new URI(uriInfo.getAbsolutePath().toString()+ "/"+  newContact.getId())).entity(newContact).build();
		return res;
	
	}
	
	
	


	@GET
	@Path("/{regionid}/contacts/{contactid}")
	@Produces("application/hal+json")
	public Response getContactInfo(@Context UriInfo uriInfo,  @PathParam("regionid") long regionid, @PathParam("contactid") long contactId) throws URISyntaxException{
		
		ContactInformation contactInfo = regionDao.getContactInfo(contactId);
		contactInfo.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
		return Response.ok(contactInfo).build();

	}
	
	@PATCH
	@Path("/{regionid}/contacts/{contactid}")
	@Produces("application/hal+json")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateContactInformation(@Context UriInfo uriInfo,  @PathParam("regionid") long regionid, @PathParam("contactid") long contactId, ContactInformation updated) throws URISyntaxException{
		ContactInformation updatedContact =  regionDao.updateContactInformation(contactId, updated);
		updatedContact.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
		return Response.created(new URI(uriInfo.getAbsolutePath().toString())).entity(updatedContact).build();
	}

	@DELETE
	@Path("/{regionid}/contacts/{contactid}")
	public Response deleteContact(@Context UriInfo uriInfo, @PathParam("regionid") long regionid, @PathParam("contactid") long contactId) {
		regionDao.deleteContact(contactId);
		return Response.noContent().build();
	}
	
}
