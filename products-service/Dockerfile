FROM gradle:8.6.0-jdk17 as builder
WORKDIR /usr/src/app
COPY src ./src
COPY build.gradle.kts .
RUN gradle bootJar --no-daemon

EXPOSE 8082

FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /usr/src/app/${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
