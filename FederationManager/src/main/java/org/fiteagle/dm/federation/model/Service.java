package org.fiteagle.dm.federation.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement
@Entity
@NamedQueries({
	@NamedQuery(name="Service.findAll", query="SELECT s FROM Service s"),
	@NamedQuery(name="Service.findByType", query="SELECT s FROM Service s WHERE s.type = :type")
})
public class Service extends LinkableEntity{

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @XmlElement(name="id")
	long id;
	
	String type;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
    @XmlTransient
	public long getUUID() {
		return id;
	}

	public void setId(long serviceId) {
		this.id = serviceId;
		
	}

	

	
	
}
