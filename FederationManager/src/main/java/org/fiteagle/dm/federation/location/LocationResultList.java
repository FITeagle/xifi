package org.fiteagle.dm.federation.location;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dne
 */

@XmlRootElement
public class LocationResultList {

	@XmlAttribute
    private int numFound;
	@XmlAttribute
    private int QTime;
    private List<LocationResult> result;

    @XmlAttribute
    private String error;
    
    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public int getQTime() {
        return QTime;
    }

    public void setQTime(int QTime) {
        this.QTime = QTime;
    }

    public List<LocationResult> getResult() {
        return result;
    }

    public void setResult(List<LocationResult> result) {
        this.result = result;
    }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}


