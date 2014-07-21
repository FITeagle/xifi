package org.fiteagle.dm.federation.auth;

import org.easymock.EasyMock;
import org.fiteagle.dm.federation.dao.EndpointDAO;
import org.fiteagle.dm.federation.dao.RegionDAO;
import org.fiteagle.dm.federation.model.Endpoint;
import org.fiteagle.dm.federation.model.Region;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class RequestValidatorTest {

    RequestValidator validator;

    private  void createValidator(HttpServletRequest request){
        if(request == null) {
            request = EasyMock.createMock(HttpServletRequest.class);
            EasyMock.expect(request.getMethod()).andReturn("GET");
            EasyMock.replay(request);
        }
        validator = new RequestValidator(request);

    }

    @Test
    public void testValidateUserNull() throws Exception {
        createValidator(null);
        User user = null;
        boolean result = validator.validate(user);
        assertFalse(result);

    }

    @Test
    public void testValidateUserOrgNull() throws Exception{
        createValidator(null);
        User u  = new User();
        boolean result =validator.validate(u);
        assertFalse(result);
    }

    @Test
    public void testValidateOrgRoleNull(){
        createValidator(null);
        User u =getUser();
        u.getOrganizations().get(0).setRoles(null);
        boolean result =validator.validate(u);
        assertFalse(result);
    }

    @Test
    public void testValidateSingleOrgSingleEmptyRoleUser(){
        createValidator(null);
        User u =getUser();
        boolean result = validator.validate(u);
        assertFalse(result);
    }

    @Test
    public void testValidateSingleOrgSingleRoleUserIOMember(){
        createValidator(null);
        User u =getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("IOMember");
        boolean result = validator.validate(u);
        assertTrue(result);
    }

    @Test
    public void testValidateSingleOrgSingleRoleUserProvider(){
        createValidator(null);
        User u =getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("Provider");
        boolean result = validator.validate(u);
        assertTrue(result);
    }

    @Test
    public void testValidateSingleOrgSingleRoleUserFM(){
        createValidator(null);
        User u =getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("FM");
        boolean result = validator.validate(u);
        assertTrue(result);
    }
    @Test
     public void testValidateSingleOrgSingleRoleUserIOAdmin(){
        createValidator(null);
        User u =getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
        boolean result = validator.validate(u);
        assertTrue(result);
    }
    @Test
    public void testValidateSingleOrgSingleRoleUserInvalidRole(){
        createValidator(null);
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("invalid");
        boolean result = validator.validate(u);
        assertFalse(result);
    }

    @Test
    public void testValidateSingleOrgMultipleRoleAllInvalid(){
        User u = getInvalidUser();
        u.getOrganizations().get(0).getRoles().get(1).setName("alsoInvalid");
        boolean result = validator.validate(u);
        assertFalse(result);
    }



    @Test
    public void testValidateSingleOrgMultipleRoleSecondValid(){
        User u = getInvalidUser();
        u.getOrganizations().get(0).getRoles().get(1).setName("IOAdmin");
        boolean result = validator.validate(u);
        assertTrue(result);
    }
    @Test
    public void testValidateMultiOrgSingleRoleSecondValid(){
        createValidator(null);
        User u = getUser();
        u.getOrganizations().add(new Organization());
        u.getOrganizations().get(1).setRoles(new ArrayList<Role>());
        u.getOrganizations().get(1).getRoles().add(new Role());
        u.getOrganizations().get(0).getRoles().get(0).setName("invalid");

        u.getOrganizations().get(1).getRoles().get(0).setName("IOAdmin");
        boolean result = validator.validate(u);
        assertTrue(result);
    }

    @Test
    public void testGetRegionsRequestInvalidUser(){
        getGETValidator();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("invalid");
        boolean result = validator.validate(u);
        assertFalse(result);
    }


    @Test
    public void testGetRegionsRequestValidUser(){
        getGETValidator();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
        boolean result = validator.validate(u);
        assertTrue(result);
    }

    @Test
    public void testPostRegionsRequestInValidUser(){
        createPostValidator2();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("invalid");
        boolean result = validator.validate(u);
        assertFalse(result);
    }



    @Test
    public void testPostRegionsRequestNotAllowedUser(){
        createPostValidator2();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("IOMember");
        boolean result = validator.validate(u);
        assertFalse(result);
    }


   @Test
   public void testPostServiceNotAllowedUser(){
       createPostValidator();
       User u = getUser();
       u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
       boolean result = validator.validate(u);
       assertFalse(result);
   }
    @Test
    public void testPostServiceAllowedUser(){
        createPostValidator();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("FM");
        boolean result = validator.validate(u);
        assertTrue(result);
    }



    @Test
    public void testPatchRegionNotOwnedAllowedUser(){
        patchRegionRequestValidator();
        User u = getUser();
        u.getOrganizations().get(0).setDisplayName("Something");
        u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
        addRegionDAO();
        boolean result = validator.validate(u);
        assertFalse(result);
    }




    @Test
    public void testPatchRegionOwnedAllowedUser(){
        patchRegionRequestValidator();
        User u = getUser();
        u.getOrganizations().get(0).setDisplayName("BerlinNode");
        u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
        addRegionDAO();

        boolean result = validator.validate(u);
        assertTrue(result);
    }

    @Test
      public  void testDeleteRegionNotOwnedAllowedUser(){
        createDeleteValidator();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
        u.getOrganizations().get(0).setDisplayName("Something");
        boolean result = validator.validate(u);
        assertFalse(result);


    }



    @Test
    public  void testDeleteRegionOwnedAllowedUser(){
        createDeleteValidator();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
        u.getOrganizations().get(0).setDisplayName("BerlinNode");
        boolean result = validator.validate(u);
        assertTrue(result);

    }
    @Test
    public void testPatchEndpointOwnedAllowedUser(){

        patchEndpointRequestValidator();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
        u.getOrganizations().get(0).setDisplayName("BerlinNode");
        addRegionDAO();
        addAllowedEndpointDAO();
        boolean result = validator.validate(u);
        assertTrue(result);

    }
    @Test
    public void testPatchEndpointNotOwnedAllowedUser(){

        patchEndpointRequestValidator();
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("IOAdmin");
        u.getOrganizations().get(0).setDisplayName("BerlinNode");
        addRegionDAO();
        addNotAllowedEndpointDAO();
        boolean result = validator.validate(u);
        assertFalse(result);

    }
    private void addRegionDAO() {
        RegionDAO regionDAO = EasyMock.createMock(RegionDAO.class);
        Region region = new Region();
        region.setAdminUsername("BerlinNode");
        EasyMock.expect(regionDAO.findRegion(1)).andReturn(region).anyTimes();
        EasyMock.expect(regionDAO.findRegion(2)).andReturn(null).anyTimes();
        EasyMock.replay(regionDAO);
        validator.setRegionDAO(regionDAO);
    }

    private void addAllowedEndpointDAO(){
        EndpointDAO endpointDAO = EasyMock.createMock(EndpointDAO.class);
        Endpoint endpoint = new Endpoint();
        endpoint.setId(1);
        endpoint.setRegionId(1);
        EasyMock.expect(endpointDAO.findEndpoint(1)).andReturn(endpoint);
        EasyMock.replay(endpointDAO);
        validator.setEndpointDAO(endpointDAO);
    }
    private void addNotAllowedEndpointDAO(){
        EndpointDAO endpointDAO = EasyMock.createMock(EndpointDAO.class);
        Endpoint endpoint = new Endpoint();
        endpoint.setId(1);
        endpoint.setRegionId(2);
        EasyMock.expect(endpointDAO.findEndpoint(1)).andReturn(endpoint);
        EasyMock.replay(endpointDAO);
        validator.setEndpointDAO(endpointDAO);
    }
    private void patchRegionRequestValidator() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getPathInfo()).andReturn("/api/v3/regions/1/");
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("PATCH");
        EasyMock.replay(request);
        createValidator(request);
    }
    private void patchEndpointRequestValidator(){
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getPathInfo()).andReturn("/api/v3/endpoints/1/");
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("PATCH");
        EasyMock.replay(request);
        createValidator(request);
    }
    private User getUser() {
        User u =new User();
        Organization o = new Organization();
        List<Organization> os = new LinkedList<>();
        List<Role> rs =  new LinkedList<>();
        Role role  = new Role();

        rs.add(role);
        o.setRoles(rs);

        os.add(o);
        u.setOrganizations(os);
        return u;
    }
    private void createDeleteValidator() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getPathInfo()).andReturn("/api/v3/regions/1/");
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("DELETE");
        EasyMock.replay(request);
        createValidator(request);
        addRegionDAO();
    }

    private void createPostValidator() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getPathInfo()).andReturn("/api/v3/services");
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("POST");
        EasyMock.replay(request);
        createValidator(request);
    }

    private void createPostValidator2() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getPathInfo()).andReturn("/api/v3/regions");
        EasyMock.expect(request.getMethod()).andReturn("POST");
        EasyMock.replay(request);
        createValidator(request);
    }


    private void getGETValidator() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getPathInfo()).andReturn("/api/v3/regions");
        EasyMock.expect(request.getMethod()).andReturn("GET");
        EasyMock.replay(request);
        createValidator(request);
    }

    private User getInvalidUser() {
        createValidator(null);
        User u = getUser();
        u.getOrganizations().get(0).getRoles().get(0).setName("invalid");
        u.getOrganizations().get(0).getRoles().add(new Role());
        return u;
    }

}