package org.fiteagle.dm.federation.auth;

import org.fiteagle.dm.federation.dao.EndpointDAO;
import org.fiteagle.dm.federation.dao.RegionDAO;
import org.fiteagle.dm.federation.model.Endpoint;
import org.fiteagle.dm.federation.model.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

public class RequestValidator {

    HttpServletRequest request;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private RegionDAO regionDAO;
    private EndpointDAO endPointDAO;


    public RequestValidator(HttpServletRequest request) {
        this.request = request;
    }


    public String getToken() {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("token")) {
                    token = c.getValue();
                    break;
                }
            }

        } else {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                String[] splittedAuth = authHeader.split("Bearer");
                token = splittedAuth[1].trim();
            }
        }
        return token;
    }


    public boolean validate(User u) {
        boolean returnVal = false;
        if (u != null) {
            List<Organization> userOrgs = u.getOrganizations();

            if (userOrgs != null && userOrgs.size() > 0) {
                for (Organization organization : userOrgs) {
                    List<Role> userRoles = organization.getRoles();
                    if (userRoles != null && userRoles.size() > 0) {
                        for (Role r : userRoles) {

                            if (r.getName() != null) {
                                if (RoleEnum.contains(r.getName())) {
                                    String requestMethod = request.getMethod();
                                    switch (requestMethod) {
                                        case "GET":
                                            returnVal = true;
                                            break;
                                        case "POST":
                                            returnVal = evaluate(returnVal, r, organization);
                                            break;
                                        case "PATCH":
                                            returnVal = evaluate(returnVal, r,organization);
                                            break;
                                        case "DELETE":
                                            returnVal = evaluate(returnVal, r,organization);
                                            break;
                                        default:
                                            System.out.println("ERROR");

                                    }

                                }
                            }

                        }
                    }

                }
            }
        }


        return returnVal;
    }

    private boolean evaluate(boolean returnVal, Role r, Organization organization) {
        if (greaterThan(RoleEnum.IOMember, r)) {

            if (request.getPathInfo().contains("/api/v3/services") && !greaterThan(RoleEnum.IOAdmin, r)) {

                return returnVal;
            }

            String[] pathSplit = request.getPathInfo().split("/");
            if (pathSplit.length > 3) {
                if (pathSplit[3].equals("endpoints")) {
                    //TODO getEndpoint > getRegionId > getOwner > user.orgs contains owner?
                    if (pathSplit.length > 4) {
                        String endpointId = pathSplit[4];
                        Endpoint endpoint = endPointDAO.findEndpoint(Long.valueOf(endpointId));
                        if(endpoint != null){
                            long regionId = endpoint.getRegionId();
                            Region region = regionDAO.findRegion(regionId);
                            if(region != null){
                                if(region.getAdminUsername().equals(organization.getDisplayName()))
                                    returnVal=true;
                            }
                        }

                    }

                } else if (pathSplit[3].equals("regions")) {
                    if (pathSplit.length > 4) {

                        String regionId = pathSplit[4];
                        Region region = regionDAO.findRegion(Long.valueOf(regionId));

                        if (region != null) {
                            if(region.getAdminUsername().equals(organization.getDisplayName()))
                                return true;
                        } else {
                            return false;
                        }

                    }else{
                        returnVal = true;
                    }
                }else if(pathSplit[3].equals("services")){
                    returnVal = true;
                }



            }


        }
        return returnVal;
    }

    private boolean greaterThan(RoleEnum target, Role role) {
        String roleName = role.getName();
        RoleEnum roleEnum = RoleEnum.valueOf(roleName);

        if (target.compareTo(roleEnum) < 0) {
            return true;
        }
        return false;
    }

    public void setRegionDAO(RegionDAO regionDAO) {
        this.regionDAO = regionDAO;
    }

    public void setEndpointDAO(EndpointDAO endpointDAO) {
        this.endPointDAO = endpointDAO;
    }
}
