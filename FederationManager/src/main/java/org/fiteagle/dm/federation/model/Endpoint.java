package org.fiteagle.dm.federation.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

@Entity
public class Endpoint {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@XmlElement(name="interface")
	private String interfaceType;
	private long service_id;
	private String name;
	private String url;
	@Transient
	private Map<String,LinkInfo> _links;	
	@XmlElement(name="region")
	private long regionId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@XmlElement(name="interface")
	public String getInterfaceType() {
		return interfaceType;
	}
	
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	public long getService_id() {
		return service_id;
	}
	public void setService_id(long serviceId) {
		this.service_id = serviceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, LinkInfo> get_links() {
		return _links;
	}
	public void set_links(Map<String, LinkInfo> links) {
		this._links = links;
	}
	
	public void addLink(String name,LinkInfo l){
		if(this._links == null)
			_links =  new HashMap<String, LinkInfo>();
			
		_links.put(name,l);
	}
	public long getRegionId() {
		return regionId;
	}
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void addLink(String name, String string) {
		addLink(name, new LinkInfo(string));
		
	}
	
	
	
	
	
}
