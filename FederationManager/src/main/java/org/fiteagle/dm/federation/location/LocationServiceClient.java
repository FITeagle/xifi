package org.fiteagle.dm.federation.location;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import takahikoKawasaki.CountryCode;

/**
 * Created by dne on 14.04.14.
 */
public class LocationServiceClient {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public String getLocation(String longitude, String latitude) {
		Client restClient = ClientBuilder.newClient();

		WebTarget target = restClient
				.target("http://services.gisgraphy.com/geoloc/findnearbylocation?lat="
						+ latitude
						+ "&lng="
						+ longitude
						+ "&format=json&placetype=city");

		Response restResp = target.request().get();
		LocationResultList value = restResp
				.readEntity(LocationResultList.class);
		restResp.close();
		String ret = null;
		if(value.getResult() != null && value.getResult().size()>1){
			LocationResult result = value.getResult().get(0);
			ret = result.getName() + " - "
					+ CountryCode.getByCode(result.getCountryCode()).getName();
		}
		
		return ret;
	}
}
