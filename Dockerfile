# Base Image
FROM eclipse-temurin:17-jdk

# Working directory
WORKDIR /app

# Copy jar
COPY target/*.jar app.jar

# Expose Spring Boot Port
EXPOSE 8080

# Run Application
ENTRYPOINT ["java","-jar","app.jar"]