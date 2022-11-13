FROM amazoncorretto:11-alpine-jdk
EXPOSE 9091
COPY target/*.jar service-ewm.jar
ENTRYPOINT ["java","-jar","/service-ewm.jar"]