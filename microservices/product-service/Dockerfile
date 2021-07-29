FROM openjdk:12.0.2

EXPOSE 8080 8000

ADD ./build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "-jar", "/app.jar"]