FROM openjdk:12.0.2

EXPOSE 8080 8002

ADD ./build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dagentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8002", "-jar", "/app.jar"]