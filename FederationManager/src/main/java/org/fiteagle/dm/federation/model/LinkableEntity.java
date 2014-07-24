package org.fiteagle.dm.federation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;


public abstract class LinkableEntity {

	
	public void addLinksWithId(String uriInfo) {
		this.addLink("self",  new LinkInfo(trimURI(uriInfo)+ "/" + this.getUUID()));
		
	}


	public abstract long getUUID() ;


	public void addLinksWithoutId(String uriInfo) {
		// TODO Auto-generated method stub
		
	}
	protected String trimURI(String uri) {

		if (uri.lastIndexOf("/") == uri.length() - 1) {
			uri = uri.subSequence(0, uri.length() - 1).toString();
		}
		return uri;
	}
	
	@Transient
	Map<String, List<LinkInfo>> _links;
	
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
}
