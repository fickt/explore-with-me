FROM amazoncorretto:11-alpine-jdk
EXPOSE 8080
COPY target/*.jar api-gateway-ewm.jar
ENTRYPOINT ["java","-jar","/api-gateway-ewm.jar"]