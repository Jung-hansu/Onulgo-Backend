FROM openjdk:21
WORKDIR /app
VOLUME /tmp

# ARG JAR_FILE=build/libs/*.jar
ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
