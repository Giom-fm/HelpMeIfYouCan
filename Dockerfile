FROM debian:stable

# Update container
RUN apt-get update && apt-get -y upgrade
# Install system dependencies
RUN apt-get -y install openjdk-11-jdk-headless maven

COPY . /opt/helpmeifyoucan/
WORKDIR /opt/helpmeifyoucan/

# Install application dependencies
#RUN  mvn dependency:resolve-plugins && mvn dependency:resolve
# Build application
RUN mvn package -Dmaven.test.skip=true

ENTRYPOINT [ "java", "-jar", "./target/helpmeifyoucan-0.0.1-SNAPSHOT.jar" ]




