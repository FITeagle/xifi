package org.fiteagle.dm.federation.auth;

import org.fiteagle.dm.federation.configuration.Config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;


@Singleton
public class Userstore {

    @EJB
    Config config;

	private Map<String, User> store;
	
	@PostConstruct
	private void init(){
		this.store = new HashMap<>();
	}
	
	public void addUser(String token, User u){
		store.put(token, u);
	}
	
	public User getUser(String token){
		User u  =  store.get(token);
        if(u == null){
            u = getUserFromIDM(token);
            addUser(token,u);
        }
        return u;
    }

	public void deleteUser(String token) {
		store.remove(token);
		
	}

    public User getUserFromRequest(HttpServletRequest req){

        RequestValidator validator = new RequestValidator(req);

        String token = validator.getToken(); //TODO add getToken from Header if no cookie provided

        User u =  getUser(token);

        return u;
    }

    private User getUserFromIDM(String token){

        String path = config.getUserLocation();

        Client restClient = ClientBuilder.newClient();

        WebTarget target  = restClient.target(path+ "?access_token="+token);

        Response restResp = target.request().get();

        User value = restResp.readEntity(User.class);

        restResp.close();
        return value;
    }
}
