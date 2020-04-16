FROM openjdk:8
COPY config.yml /opt/multi/
COPY target/DropWizardExample-0.0.1-SNAPSHOT.jar /opt/multi/app.jar
WORKDIR /opt/multi
CMD java -jar app.jar server config.yml
