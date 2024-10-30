FROM openjdk:17-jdk-alpine
EXPOSE 8089
#ADD target/tp-foyer-5.0.0.jar foyer-5.0.0.jar

#Nexus credentials & artefact link
ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD
ARG ARTIFACT_URL="http://192.168.1.26:8081/repository/maven-releases/tn/esprit/tp-foyer/5.0.0/tp-foyer-5.0.0.jar"
#Pull artefact from nexus
RUN apk add --no-cache curl && \
    curl -u $NEXUS_USERNAME:$NEXUS_PASSWORD -o foyer-5.0.0.jar $ARTIFACT_URL

ENTRYPOINT ["java","-jar","/foyer-5.0.0.jar"]