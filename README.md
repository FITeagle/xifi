xifi
====

Delivery Mechanism: XIFI Federation

Installation Guide
------------------
* Compile
``` cd FederationManager
mvn clean install
```

* Run WildFly container
 cd ../../server/wildfly
``` ./bin/standalone.sh ```

* MySql Setup

	*log into WildFly AS at localhost:9990 with username "admin" and password "admin"
	*under the Profile tab add a new datasource and follow the steps listed in the pictures below
	![alt text](doc/addDatasource1.png "Add datasource")

