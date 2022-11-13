FROM amazoncorretto:11-alpine-jdk
EXPOSE 8080
ADD ./target/api-gateway-ewm.jar api-gateway-ewm.jar
ENTRYPOINT ["java","-jar","/api-gateway-ewm.jar"]