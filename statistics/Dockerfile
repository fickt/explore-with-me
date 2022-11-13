FROM amazoncorretto:11-alpine-jdk
EXPOSE 9090
ADD target/stats-ewm.jar stats-ewm.jar
ENTRYPOINT ["java","-jar","/stats-ewm.jar"]