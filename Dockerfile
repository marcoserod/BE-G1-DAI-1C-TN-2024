FROM amazoncorretto:17-alpine-jdk

COPY target/dai-2.0.8.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
