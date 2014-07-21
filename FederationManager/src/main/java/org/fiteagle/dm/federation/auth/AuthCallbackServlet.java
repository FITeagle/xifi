package org.fiteagle.dm.federation.auth;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.fiteagle.dm.federation.configuration.Config;

public class AuthCallbackServlet extends HttpServlet {

	@EJB
	Config config;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			if (req.getParameter("code") != null) {
				OAuthAuthzResponse oar = OAuthAuthzResponse
						.oauthCodeAuthzResponse(req);
				String code = oar.getCode();
				OAuthClientRequest oreq = OAuthClientRequest
						.tokenLocation(config.getTokenLocation())
						.setGrantType(GrantType.AUTHORIZATION_CODE)
						.setClientId(config.getClientId())
						.setClientSecret(
								config.getClientSecret())
						.setRedirectURI(
								config.getRedirectURI())
						.setCode(code).buildQueryMessage();
				OAuthClient client = new OAuthClient(new URLConnectionClient());
				OAuthJSONAccessTokenResponse tokenResp = client
						.accessToken(oreq);
				String accessToken = tokenResp.getAccessToken();

				resp.addCookie(new Cookie("token", accessToken));
				resp.sendRedirect("/federationManager/");

			} else {
				resp.sendRedirect(config.getErrorPage());
			}
		} catch (OAuthProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
