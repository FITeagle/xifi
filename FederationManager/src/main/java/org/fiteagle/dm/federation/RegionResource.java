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
import org.fiteagle.dm.federation.dao.RegionDAO;
import org.fiteagle.dm.federation.location.LocationServiceClient;
import org.fiteagle.dm.federation.model.ContactInformation;
import org.fiteagle.dm.federation.model.Member;
import org.fiteagle.dm.federation.model.Region;
import org.fiteagle.dm.federation.model.RegionStatus;



@Stateless
@Path("/regions")
public class RegionResource {
	private static final Logger LOGGER = Logger.getLogger(RegionResource.class.getName());
	@EJB
	RegionDAO regionDao;
	
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
		//LocationServiceClient locationService = new LocationServiceClient();
		
	//	String regionId = locationService.getLocation(region.getLongitude(), region.getLatitude());
	//	region.setRegionId(regionId);
		Region newRegion = regionDao.createRegion(region);
		newRegion.addLinksWithId(uriInfo.getAbsolutePath().toString());
		return Response.created(new URI(uriInfo.getAbsolutePath().toString() + "/" + newRegion.getUUID())).entity(newRegion).build();
		
	}


	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	@Path("/{regionid}")
	public Response updateRegion(@Context UriInfo uriInfo,@PathParam("regionid") long id, Region r) throws URISyntaxException {
		LOGGER.log(Level.INFO, "Received patch request ");
		Response res;
		if((r.getUUID() != 0 && id != r.getUUID())){
			res = Response.status(400).build();
		}else{
			if(r.getUUID() == 0)
				r.setUUID(id);
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
	public Response getAllRegions(@Context UriInfo uriInfo,@QueryParam("country") String country,@QueryParam("status") String status, @QueryParam("public") String publicNode, @QueryParam("organizationName") String organizationName, @QueryParam("page")String page, @QueryParam("per_page") String per_page) {
		LOGGER.log(Level.INFO, "Received find All Regions");
		List<Region> regions	 = regionDao.findRegions(country, status, publicNode, organizationName);
		
		for(Region r: regions){
			r.addLinksWithId(uriInfo.getAbsolutePath().toString());

		}
		GenericEntity<List<Region>> entity = new GenericEntity<List<Region>>(regions){};
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


		return Response.created(uriInfo.getAbsolutePath()).entity(newRegionStatus).build();
		
	}
	
	@GET
	@Path("/{regionid}/contacts")
	@Produces("application/hal+json")
	public Response getRegionContacts(@Context UriInfo uriInfo,@PathParam("regionid") long regionid, @QueryParam("type") String type){
		LOGGER.log(Level.INFO, "Received contacts request for region: "+ regionid);
		List<ContactInformation> contacts = regionDao.getContacts(regionid, type);
		for(ContactInformation c: contacts){
			c.addLinksWithId(uriInfo.getAbsolutePath().toString());
		}
		GenericEntity<List<ContactInformation>> entity = new GenericEntity<List<ContactInformation>>(contacts){};
		return Response.ok(entity).build();
	}

	

	@POST
	@Path("/{regionid}/contacts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	public Response addContactInformation(@Context UriInfo uriInfo, @PathParam("regionid") long regionid,ContactInformation contactInfo) throws URISyntaxException {
		Response res = null;
		ContactInformation newContact = regionDao.addContactInformation(regionid, contactInfo );
		newContact.addLinksWithId(uriInfo.getAbsolutePath().toString());
		res = Response.created(new URI(uriInfo.getAbsolutePath().toString()+ "/"+  newContact.getUUID())).entity(newContact).build();
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
	
	@GET
	@Path("/{regionid}/members")
	@Produces("application/hal+json")
	public Response getRegionMembers(@Context UriInfo uriInfo,@PathParam("regionid") long regionid){
		LOGGER.log(Level.INFO, "Received contacts request for region: "+ regionid);
		List<Member> members = regionDao.getMembers(regionid);
		for(Member m: members){
			m.addLinksWithId(uriInfo.getAbsolutePath().toString());
		}
		GenericEntity<List<Member>> entity = new GenericEntity<List<Member>>(members){};
		return Response.ok(entity).build();
	}

	

	@POST
	@Path("/{regionid}/members")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/hal+json")
	public Response addMember(@Context UriInfo uriInfo, @PathParam("regionid") long regionid,Member member) throws URISyntaxException {
		Response res = null;
		Member newMember = regionDao.addMember(regionid, member);
		newMember.addLinksWithId(uriInfo.getAbsolutePath().toString());
		res = Response.created(new URI(uriInfo.getAbsolutePath().toString()+ "/"+  newMember.getUUID())).entity(newMember).build();
		return res;
	
	}
	
	
	


	@GET
	@Path("/{regionid}/members/{member_id}")
	@Produces("application/hal+json")
	public Response getMember(@Context UriInfo uriInfo,  @PathParam("regionid") long regionid, @PathParam("member_id") long memberid) throws URISyntaxException{
		
		Member member = regionDao.getMember(memberid);
		member.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
		return Response.ok(member).build();

	}
	
	@PATCH
	@Path("/{regionid}/members/{member_id}")
	@Produces("application/hal+json")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMember(@Context UriInfo uriInfo, @PathParam("regionid") long regionid, @PathParam("member_id") long member_id, Member updated) throws URISyntaxException{
		Member updatedMember =  regionDao.updateMember(member_id, updated);
		updatedMember.addLinksWithoutId(uriInfo.getAbsolutePath().toString());
		return Response.created(new URI(uriInfo.getAbsolutePath().toString())).entity(updatedMember).build();
	}

	@DELETE
	@Path("/{regionid}/members/{member_id}")
	public Response deleteMember(@Context UriInfo uriInfo, @PathParam("regionid") long regionid, @PathParam("member_id") long member_id) {
		regionDao.deleteMember(member_id);
		return Response.noContent().build();
	}
	
}
