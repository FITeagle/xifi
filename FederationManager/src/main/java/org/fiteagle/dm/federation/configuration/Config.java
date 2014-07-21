package org.fiteagle.dm.federation.configuration;

/**
 * Created by dne on 14.04.14.
 */
public interface Config {
    String getTokenLocation();

    String getClientId();

    String getClientSecret();

    String getRedirectURI();

    String getErrorPage();

    String getAuthLocation();

    String getUserLocation();

    String getUserRole();
}
