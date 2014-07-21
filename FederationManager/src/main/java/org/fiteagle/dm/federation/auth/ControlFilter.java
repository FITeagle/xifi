package org.fiteagle.dm.federation.auth;

import org.fiteagle.dm.federation.configuration.Config;
import org.fiteagle.dm.federation.dao.EndpointDAO;
import org.fiteagle.dm.federation.dao.RegionDAO;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class ControlFilter implements Filter {

    @EJB
    Userstore userstore;
    @EJB
    RegionDAO regionDAO;

    @EJB
    EndpointDAO endpointDAO;
    @EJB
    Config config;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		String path = uri.substring(req.getContextPath().length());
		
		if (path.startsWith("/resources")){ //|| path.startsWith("/api/v3")) {
			chain.doFilter(request, response); // Goes to container's own
												// default servlet.
		} else {
			RequestValidator validator = new RequestValidator(req);
			String token = validator.getToken();
			if(token != null){
                if(path.startsWith("/api/v3")){
				    User u =userstore.getUser(token);
                    validator.setRegionDAO(regionDAO);
                    validator.setEndpointDAO(endpointDAO);
                    if(config.getUserRole().equals("production")){
                        if(validator.validate(u))
                            chain.doFilter(request, response);
                    }else{
                        chain.doFilter(request, response);
                    }


                }else {
                    request.getRequestDispatcher("/pages" + uri).forward(request,
                            response); // Goes to controller servlet.
                }
			}else{
				
				if(path.endsWith("/callback")){
					request.getRequestDispatcher("/callback").forward(request,
							response);
				}else{
				request.getRequestDispatcher("/login").forward(request,
						response);
				}
			} 
			
		}
	}



	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
