FROM amazoncorretto:17-alpine-jdk

COPY target/dai-2.1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
