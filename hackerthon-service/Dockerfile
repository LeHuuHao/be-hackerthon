# base image: linux alpine os with open jdk 8
FROM eclipse-temurin:11-jre-alpine
#copy jar from local into docker image
COPY target/hackerthon-service-1.0.jar hackerthon-service-1.0.jar
#command line to run jar
ENTRYPOINT ["java","/hackerthon-service-1.0.jar"]
