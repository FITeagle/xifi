package org.fiteagle.dm.federation.auth;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.fiteagle.dm.federation.configuration.Config;

public class LoginServlet extends HttpServlet {

	@EJB
    Config config;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {

            if(config.getUserRole().equals("production")) {
                OAuthClientRequest oauthRequest = OAuthClientRequest.authorizationLocation(config.getAuthLocation()).setClientId(config.getClientId()).setRedirectURI(config.getRedirectURI()).setResponseType("code").buildQueryMessage();
                resp.sendRedirect(oauthRequest.getLocationUri());
            }else{
                resp.addCookie(new Cookie("token", "developer"));
                resp.sendRedirect("/");
            }

		} catch (OAuthSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
