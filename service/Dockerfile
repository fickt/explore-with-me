FROM amazoncorretto:11-alpine-jdk
EXPOSE 9091
ADD ./target/service-ewm.jar service-ewm.jar
ENTRYPOINT ["java","-jar","service-ewm.jar"]