package org.fiteagle.dm.federation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
public class RegionStatus {

	@Id
	long region;
	
	
	long timestamp;
	
	String status;


	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getRegion() {
		return region;
	}
	


	public void setRegion(long id) {
		this.region = id;
	}

	
	 @Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if(this.getClass().equals(obj.getClass())){
			RegionStatus toCompare = (RegionStatus) obj;
			if(this.region == toCompare.getRegion() && this.getStatus().equals(toCompare.getStatus()) && this.getTimestamp() == toCompare.getTimestamp()){
				ret = true;
			}
		}
		return ret;
	}



}
