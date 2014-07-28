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

            store.addDeveloper();
            req.getRequestDispatcher("/index.html").forward(req, resp);

        }

	}
}
