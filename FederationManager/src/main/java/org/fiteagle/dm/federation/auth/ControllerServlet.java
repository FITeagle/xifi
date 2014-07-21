package org.fiteagle.dm.federation.auth;

import org.fiteagle.dm.federation.configuration.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerServlet extends HttpServlet {

	@EJB
	Userstore store;
	
	@EJB 
	Config config;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

        if(config.getUserRole().equals("production")){


            User user = store.getUserFromRequest(req);
            if (user != null) {
                System.out.println("path ");
                //resp.getOutputStream().write(user.getEmail().getBytes());
                req.getRequestDispatcher("/index.html").forward(req, resp);
            } else {

            }
        }else{

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
            store.addUser("developer",u);
            req.getRequestDispatcher("/index.html").forward(req, resp);

        }

	}
}
