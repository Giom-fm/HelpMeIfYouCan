### Help Me If You Can

## Dependencies
* maven 3.6.3
* Java 11

## Start
Set environment variable *DATABASE_PASSWORD* with your credentials.  
Set environment variable *PROFILE* for your Spring profile.  
Available Profiles:
* local
* development
* production

`set -lx DATABASE_PASSWORD <PASSWORD> && set -lx PROFILE <PROFILE> && ./mvnw spring-boot:run`