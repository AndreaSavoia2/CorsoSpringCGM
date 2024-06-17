FROM amazoncorretto:21-alpine-jdk
EXPOSE 8761
ADD target/ms-discovery-0.0.1-SNAPSHOT.jar ms-discovery-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ms-discovery-0.0.1-SNAPSHOT.jar"]