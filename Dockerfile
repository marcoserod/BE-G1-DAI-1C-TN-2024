FROM amazoncorretto:17-alpine-jdk

COPY target/dai-1.1.8.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
