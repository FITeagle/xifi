package org.fiteagle.dm.federation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Member extends LinkableEntity {
	@Id
	@GeneratedValue
	private long id;
	
	private long tenant_id;
	@ManyToOne(targetEntity=Region.class)
	Region region;
	
	public long getUUID(){
		return this.id;
	}
	
	
	public long getTenant_id(){
		return this.tenant_id;
	}
	
	public void setTenant_id(long tenant_id){
		this.tenant_id = tenant_id;
	}
	@XmlTransient
	public Region getRegion(){
		return this.region;
	}
	public void setRegion(Region region) {
		this.region = region;
		
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if(obj instanceof Member){
			Member newMember = (Member) obj;
			if(newMember.getTenant_id() == this.getTenant_id())
				ret = true;
		}
		return ret;
	}

}
