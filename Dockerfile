FROM openjdk:latest as build
MAINTAINER uit.com
COPY target/microservice-gateway-0.0.1-SNAPSHOT.jar microservice-gateway-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/microservice-gateway-0.0.1-SNAPSHOT.jar"]