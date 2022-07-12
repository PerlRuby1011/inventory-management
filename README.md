# inventory-management

**Tools/Technology used**:

Spring-Boot

Spring Security

Spring Data - JPA

Hibernate ORM

Open API Docs Swagger

Liquibase

Docker-compose

PostgreSQL

Lombok

Java Streams

Custom Exceptions

Maven


**Running the Project in Docker:**

**Build the Java application:**

The Java application is a Spring Boot app with Maven as the build tool. To build the application, simply run "**mvn clean install -DskipTests**" from the root of the project. This command will clean out the previous build directory (target/) and rebuild the project. Of note, this command builds the JAR file we will use to deploy the application within Docker. We can use "**mvn clean install**" if an instance of postgresql db is running already via "**docker compose up postgres**".

**Spin up the Project:**

The docker-compose.yml file orchestrates all the containers needed for the project to run. The two containers that make up the project are:

**postgres** - contains the Postgres database
**api** - contains the Java Spring Boot application

To spin up the project, run "docker compose up". This will build the appropriate images and spin up the containers. Individual containers can also be selectively spun up if you are actively working on a certain component. For example, docker compose up postgres will launch just the Postgres container allowing the Java application to be run locally.

In the default configuration, the API will be available at http://localhost:8080/.


**Liquibase:**

Liquibase is used in this project to keep track of RDBMS changes and to avoid manual effort during deployments into various environments. liquibase.properties found under the "resources" directory is used to generate the initial change log, it won't be used any further. Files found under "/inventory-management/src/main/resources/db/changelog/" are executed during the first run and we can verify the db changes applied via the following tables: "inventory.databasechangelog" and "inventory.databasechangeloglock".

**Database:**

Postgres when spun is using "01.sql" init scripts to create the schema "inventory" and it is used further by Hibernate entities and Liquibase sqls.

**Unit Test Cases:**

Unit test cases are created for the DrugController. Test cases cover both postive and negative scenarios in most cases.

**Db Table Auditing:** 

"Auditable.java" file is used to track the audit information. If a new table wants to keep track of create user/timestamp, update user/timestamp entires, it can simply extend this "Auditable.java". User info will be populated into the database via Spring security Context (please see "JPAAuditConfig.java").

**Swagger:**

Swagger UI has been configured to take a wholistic view of the end points being supported by this application. Swagger helps QA teams and other teams to figure the end points being implemented in any given project. here's the url: http://localhost:8080/inventory/swagger-ui/#/drug-controller 

NOTE: Swagger container is not configured in the docker, hence above url can be accessed only when Spring boot is ran outside docker.

**Scaling the application:**

Since it is mentioned that traffic would be high at 12am everyday, we can probabaly do a scheduled Auto-scaling group. There are other options available as well:
1. Create API gateway and Lambda would be a perfect combination for the update operations.
2. In case the above services can't be used, we can employ an SQS in front of EC2 instances and create a decoupled resilient architecture.
3. Since we're using Docker, Kubernetes would be an excellent solution too.


**POSTMAN:**

Postman collection to test the endpoints are available in the following location: "/inventory-management/Postman Collection/DrugInventory.postman_collection.json"

**Things to Improve:**

1. There's an annoying error occuring then postgresql is spun up, but it is not a showstopper. Haven't got a chance to look into the rootcause as yet. "FATAL:  database "inventory" does not exist".
2. It kinda little weird that we had to create 2 endpoints for UID and Manufacturer. It certainly can be handled differently. I believe GraphQL would handle this situation better than REST.
3. There's still lot of room to improve the docker-compose file and unit testing scenarios.
