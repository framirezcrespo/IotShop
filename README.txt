To Build and run this application you need
 
 - Java (11 or above)
 - Apache Maven

To launch the application run this command from the application root folder:

	mvn clean spring-boot:run

To run the tests:

	mvn test

API:

	http://localhost:8080/swagger-ui/

The database used is H2.
When started, the script data.sql is run and 1000 record of iot devices and another 1000 of sim cards are inserted.
Half of the iot devices are "configured" (status=READY, sim card assigned and temperature between 25 and 85ºC)

  