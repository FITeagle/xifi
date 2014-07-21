package org.fiteagle.dm.federation.location;

import java.util.List;

/**
 * Created by dne
 */
public class LocationResult {
	
	

    public String getAdm2Name() {
		return adm2Name;
	}

	public void setAdm2Name(String adm2Name) {
		this.adm2Name = adm2Name;
	}

	public String getGtopo30() {
		return gtopo30;
	}

	public void setGtopo30(String gtopo30) {
		this.gtopo30 = gtopo30;
	}

	public String getElevation() {
		return elevation;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	double distance;
    String name;
    String adm1Code;
    String adm2Code;
    String adm3Code;
    String adm4Code;
    String adm1Name;
    String adm2Name;
    String adm3Name;
    String adm4Name;
    String asciiName;
    String countryCode;
    String featureClass;
    String featureCode;
    String featureId;
    String population;
    String timezone;
    double lat;
    double lng;
    String placeType;
    String gtopo30;
    String elevation;	
    boolean oneWay;
    double length;
    List<String> zipCodes;
    String google_map_url;
    String yahoo_map_url;
    String country_flag_url;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdm1Code() {
        return adm1Code;
    }

    public void setAdm1Code(String adm1Code) {
        this.adm1Code = adm1Code;
    }

    public String getAdm2Code() {
        return adm2Code;
    }

    public void setAdm2Code(String adm2Code) {
        this.adm2Code = adm2Code;
    }

    public String getAdm3Code() {
        return adm3Code;
    }

    public void setAdm3Code(String adm3Code) {
        this.adm3Code = adm3Code;
    }

    public String getAdm4Code() {
        return adm4Code;
    }

    public void setAdm4Code(String adm4Code) {
        this.adm4Code = adm4Code;
    }

    public String getAdm1Name() {
        return adm1Name;
    }

    public void setAdm1Name(String adm1Name) {
        this.adm1Name = adm1Name;
    }

    public String getAdm3Name() {
        return adm3Name;
    }

    public void setAdm3Name(String adm3Name) {
        this.adm3Name = adm3Name;
    }

    public String getAdm4Name() {
        return adm4Name;
    }

    public void setAdm4Name(String adm4Name) {
        this.adm4Name = adm4Name;
    }

    public String getAsciiName() {
        return asciiName;
    }

    public void setAsciiName(String asciiName) {
        this.asciiName = asciiName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFeatureClass() {
        return featureClass;
    }

    public void setFeatureClass(String featureClass) {
        this.featureClass = featureClass;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public List<String> getZipCodes() {
        return zipCodes;
    }

    public void setZipCodes(List<String> zipCodes) {
        this.zipCodes = zipCodes;
    }

    public String getGoogle_map_url() {
        return google_map_url;
    }

    public void setGoogle_map_url(String google_map_url) {
        this.google_map_url = google_map_url;
    }

    public String getYahoo_map_url() {
        return yahoo_map_url;
    }

    public void setYahoo_map_url(String yahoo_map_url) {
        this.yahoo_map_url = yahoo_map_url;
    }

    public String getCountry_flag_url() {
        return country_flag_url;
    }

    public void setCountry_flag_url(String country_flag_url) {
        this.country_flag_url = country_flag_url;
    }
}

