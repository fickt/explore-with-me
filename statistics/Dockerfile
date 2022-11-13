FROM amazoncorretto:11-alpine-jdk
EXPOSE 9090
COPY target/*.jar stats-ewm.jar
ENTRYPOINT ["java","-jar","/stats-ewm.jar"]