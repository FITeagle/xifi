package org.fiteagle.dm.federation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement
@Entity
public class Region extends LinkableEntity implements Serializable{
	
	@Id
	@Column(name="uuid")
	@GeneratedValue
	long uuid;


    @XmlElement(name="id")
    String regionId;
	String country;
	String latitude;
	String longitude;
	String organizationName;
	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	RegionStatus regionStatus;
	String nodeType;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="region_id", referencedColumnName="uuid")
	private List<ContactInformation> contacts;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="region_id", referencedColumnName="uuid")
	private List<Member> members;
	
	@Transient
	Map<String, List<LinkInfo>> _links;
	
	@XmlElement(name="public")
	String publicNode;
	

	
	public String getPublicNode() {
		return publicNode;
	}
	
	public void setPublicNode(String publicNode) {
		this.publicNode = publicNode;
	}
	
	public Map<String,List<LinkInfo>> get_links() {
		return _links;
	}
	
	public void set_links(Map<String, List<LinkInfo>> links) {
		this._links = links;
	}
	
	public void addLink(String name,LinkInfo l){
		List<LinkInfo> list;
		if(this._links == null){
			_links = new HashMap<>();
			list = new ArrayList<>();
			list.add(l);
		}else{
			list = _links.get(name);
			if(list == null)
				list = new ArrayList<>();
			list.add(l);
		}
		_links.put(name,list);
	}



    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String id) {
        this.regionId = id;
    }
	

	@XmlTransient
	public List<ContactInformation> getContacts() {
		return contacts;
	}


	public void setContacts(List<ContactInformation> contacts) {
		this.contacts = contacts;
	}


	public long getUUID() {
		return uuid;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getLatitude() {
		return latitude;
	}


	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}


	public String getLongitude() {
		return longitude;
	}


	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


	public String getOrganizationName() {
		return organizationName;
	}


	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	@XmlTransient
	public RegionStatus getRegionStatus() {
		return regionStatus;
	}


	public void setRegionStatus(RegionStatus registrationStatus) {
		
		this.regionStatus = registrationStatus;
	}


	public String getNodeType() {
		return nodeType;
	}


	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}


	public void setUUID(long uuid) {
		this.uuid =uuid;
		if(null!= regionStatus){
			regionStatus.setRegion(uuid);
		}
	}

	public void addContact(ContactInformation contactInfo) {
		if(this.contacts == null)
			contacts = new ArrayList<>();
			
		contacts.add(contactInfo);
		
	}
	
	@XmlTransient
	public List<Member> getMembers(){
		return this.members;
	}
	
	public void setMembers(List<Member> members){
		this.members = members;
	}
	
	public void addMember(Member member){
		if(this.members ==null){
			this.members = new ArrayList<>();
		}
		this.members.add(member);
	}

	@Override
	public void addLinksWithId(String uriInfo) {
		this.addLink("self",  new LinkInfo(trimURI(uriInfo)+ "/" + this.getUUID()));
		this.addLink("status" , new LinkInfo(trimURI(uriInfo)+ "/" + this.getUUID()+"/status/"));
		if(this.getContacts() != null && this.getContacts().size() > 0){
			for(ContactInformation contactInformation : this.getContacts()){
				this.addLink("contacts",  new LinkInfo(trimURI(uriInfo)+ "/" + this.getUUID()+"/contacts/" + contactInformation.getUUID()));
			}
		}
		if(this.getMembers() != null && this.getMembers().size() > 0){
			for(Member m: this.getMembers()){
				this.addLink("members", new LinkInfo(trimURI(uriInfo)+"/"+this.getUUID() + "/members/"+m.getUUID()));
			}
		}
		
	}

	@Override
	public void addLinksWithoutId(String uriInfo) {
		this.addLink("self",  new LinkInfo(trimURI(uriInfo)));
		this.addLink("status" , new LinkInfo(trimURI(uriInfo)+"/status/"));
		if(this.getContacts() != null && this.getContacts().size() > 0){
			for(ContactInformation contactInformation : this.getContacts()){
				this.addLink("contacts",  new LinkInfo(trimURI(uriInfo) + "/contacts/" + contactInformation.getUUID()));
			}
		}
		if(this.getMembers() != null && this.getMembers().size() > 0){
			for(Member m: this.getMembers()){
				this.addLink("members", new LinkInfo(trimURI(uriInfo)+ "/members/"+m.getUUID()));
			}
		}
		
	}

	
	

}
