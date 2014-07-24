package org.fiteagle.dm.federation.location;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegionNameSuggestion implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] names;
	
	public String[] getNames() {
		return names;
	}
	
	public void setNames(String[] names) {
		this.names = names;
	}
	
	
	
	
	
}
