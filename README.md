# Help Me If You Can

## Dependencies
* maven 3.6.3
* Java 11
* Docker 19.03.8-ce
* Docker-Compose 1.25.4

## Start
Set environment variable *DATABASE_PASSWORD* with your credentials.  
Set environment variable *PROFILE* for your Spring profile.  
Available Profiles:
* local
* development
* production

`set -lx DATABASE_PASSWORD <PASSWORD> && set -lx PROFILE <PROFILE> && ./mvnw spring-boot:run`


## Deploy

### Local
To deploy the application on localhost run the preconfigured docker-compose script.  
This will setup the application listening on *localhost:8080* with an MongoDB listening on *localhost:27017*

`docker-compose up -d`

