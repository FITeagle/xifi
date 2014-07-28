package org.fiteagle.dm.federation.auth;

import org.fiteagle.dm.federation.configuration.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            if(token.equals("developer")){
                addDeveloper();
                u = store.get("developer");
            }else{
                u = getUserFromIDM(token);
                addUser(token,u);
            }

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

    public void addDeveloper(){
        User u = new User();
        u.setId("1");
        u.setActorId("1");
        u.setDisplayName("developer");
        u.setEmail("d@de.de");
        u.setNickName("dev");
        Organization o  = new Organization();
        o.setDisplayName("TUB");
        o.setActorId(1);
        o.setId(1);
        List<Organization> oList = new ArrayList<>();
        oList.add(o);
        u.setOrganizations(oList);
        Role r = new Role();
        r.setId("1");

        if(config.getUserRole().equals("dev_member")){
            r.setName("IOMember");

        }
        if(config.getUserRole().equals("dev_admin")){
            r.setName("IOAdmin");
        }
        if(config.getUserRole().equals("dev_fm")){
            r.setName("Provider");
        }

        List<Role> rList = new ArrayList<>();
        rList.add(r);
        o.setRoles(rList);
        this.addUser("developer",u);
    }
}
