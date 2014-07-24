xifi
====

Delivery Mechanism: XIFI Federation

Installation Guide
------------------
Compile
---
```sh 
cd FederationManager
mvn clean install
```

Run WildFly container
---
 ```sh
 cd ../../server/wildfly
 ./bin/standalone.sh
 ```

MySql Setup
---
* log into WildFly AS at localhost:9990 with username "admin" and password "admin"
* under the Profile tab add a new datasource and follow the steps listed in the pictures below
![alt text](doc/addDatasource1.png "Add datasource")
![alt text](doc/addDatasource2.png "Add datasource jndi")
* edit the username and password regarding your MySql server installation
![alt text](doc/addDatasource3.png "Add datasource connection")
* the registered datasource might have to be enabled
	
Preferences
---
The Federation Manager application defines a set of preferences for configuration. 
	* tokenLocation - url to fetch oauth access token, example https://account.lab.fi-ware.org/oauth2/token
	* redirectURI   - callback url registered at IDM
	* userLocation  - url to fetch user based on access token
	* clientId     -  client id assigned by IDM
	* environment -  defines the role of the user to use, either "dev_member", "dev_admin", "dev_fm" for development environment or "production" if to be used with IDM authentication/authorization
	* errorPage - defines the URL for an error page
	* clientSecret - client secret assigend by IDM
	* authLocation - url of oauth entry point