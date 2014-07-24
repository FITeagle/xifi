package org.fiteagle.dm.federation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
public class ContactInformation extends LinkableEntity{

	@Id
	@Column(name="UUID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long uuid;	
	public long getUUID() {
		return uuid;
	}


	String name;
	String country;
	String fax;
	String phone;
	String email;
	String type;
	String location;
	@ManyToOne(targetEntity=Region.class)
	@XmlTransient
	Region region;
	
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String address) {
		this.location = address;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		boolean ret =false;
		if(obj.getClass().equals(this.getClass())){
			ContactInformation toCompare  = (ContactInformation) obj;
			if(this.getLocation().equals(toCompare.getLocation())&&
			   this.getCountry().equals(toCompare.getCountry())&&
			   this.getEmail().equals(toCompare.getEmail())&&
			   this.getFax().equals(toCompare.getFax())&&
			   this.getName().equals(toCompare.getName())&&
			   this.getPhone().equals(toCompare.getPhone())&&
			   this.getType().equals(toCompare.getType())){
				ret = true;
			}
		}
		return ret;
	}
	

	

	@Override
	public void addLinksWithoutId(String uriInfo) {
	
		
	}
	
}
