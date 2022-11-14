FROM amazoncorretto:11-alpine-jdk
EXPOSE 9090
ADD ./stats-ewm.jar stats-ewm.jar
ENTRYPOINT ["java","-jar","stats-ewm.jar"]