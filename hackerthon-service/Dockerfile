#base image: linux alpine os with open jdk 8
# FROM eclipse-temurin:11-jre-alpine
# #copy jar from local into docker image
# COPY target/sim-service-1.0.jar sim-service-1.0.jar
# #command line to run jar
# ENTRYPOINT ["java","-Dspring.profiles.active=dev", "-jar","/sim-service-1.0.jar"]

FROM maven:3.8.5-openjdk-11-slim as BUILDER
ARG VERSION=0.0.1-SNAPSHOT
WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src/

RUN mvn clean package
COPY target/hackerthon-service-${VERSION}.jar target/application.jar

FROM openjdk:11.0.11-jre-slim
WORKDIR /app/

COPY --from=BUILDER /build/target/application.jar /app/
CMD java -jar /app/application.jar