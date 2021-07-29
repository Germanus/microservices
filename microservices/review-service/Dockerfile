FROM openjdk:12.0.2

EXPOSE 8080 8001

ADD ./build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
CMD [ "-Dagentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8001" ]