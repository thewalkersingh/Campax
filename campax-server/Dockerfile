## Use Java 21 on Alpine for small image size
#FROM openjdk:21-jdk-slim AS build
#
#WORKDIR /app
#
## Copy Maven files and download dependencies
#COPY pom.xml .
#RUN mvn dependency:go-offline -B
#
## Copy source code and build
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Stage 2: Run the JAR
#FROM openjdk:21-jdk-slim
#
#WORKDIR /app
#
## Copy the built JAR
#COPY --from=build /app/target/*.jar app.jar
#
## Expose the port
#EXPOSE 8080
#
## Run the JAR with the 'prod' profile
#ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]