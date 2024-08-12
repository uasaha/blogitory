FROM openjdk:21-slim
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} blogitory.jar
ENV APP_PROFILE prod

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${APP_PROFILE}", "/blogitory.jar"]