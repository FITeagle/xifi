package org.fiteagle.dm.federation.configuration;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

@Singleton
public class ConfigImpl implements  Config {

    private Preferences prefs;


    @PostConstruct
    private void init() {
        prefs = Preferences.userNodeForPackage(Config.class);
        prefs.put("tokenLocation", getTokenLocation());
        prefs.put("clientId", getClientId());
        prefs.put("clientSecret", getClientSecret());
        prefs.put("redirectURI", getRedirectURI());
        prefs.put("errorPage", getErrorPage());
        prefs.put("authLocation", getAuthLocation());
        prefs.put("userLocation", getUserLocation());

    }

    @Override
    public String getTokenLocation() {
        return prefs.get("tokenLocation", "http://localhost/token");
    }


    @Override
    public String getClientId() {
        return prefs.get("clientId", "1");
    }

    @Override
    public String getClientSecret() {
        return prefs.get("clientSecret", "abc");
    }

    @Override
    public String getRedirectURI() {
        return prefs.get("redirectURI", "http://localhost/callback");
    }

    @Override
    public String getErrorPage() {
        return prefs.get("errorPage", "http://localhost/error");
    }

    @Override
    public String getAuthLocation() {
        return  prefs.get("authLocation", "http://localhost/authorize");
    }

    @Override
    public String getUserLocation() {
        return prefs.get("userLocation", "http://localhost/user");
    }

    @Override
    public String getUserRole() {
        return prefs.get("developer", "developer");
    }


}
