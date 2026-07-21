# 02. Getting Started & Setup Guide

This guide details how to configure, build, and run **Smart Contact Manager** locally or using Docker.

---

## 📋 Prerequisites

Before setting up the project, ensure you have the following installed:

1. **Java Development Kit (JDK)**: Version 17 or higher.
2. **Apache Maven**: Version 3.8+ (or use the included `./mvnw` wrapper).
3. **MySQL Server**: Version 8.0+.
4. **IDE (Optional)**: Eclipse, IntelliJ IDEA, or VS Code with Java Extension Pack.
5. **Docker (Optional)**: If running via container.

---

## ⚙️ Configuration Setup

### 1. Database Creation

Open your MySQL client and create a database named `smartcontactmanager`:

```sql
CREATE DATABASE smartcontactmanager;
```

### 2. Application Properties (`src/main/resources/application.properties`)

Verify or adjust the configuration properties:

```properties
# Application Name
spring.application.name=smartContactManager

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/smartcontactmanager
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate Properties
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# DevTools & LiveReload
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
spring.thymeleaf.cache=false

# SMTP Mail Configuration (For OTP Password Reset)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.debug=true
```

> **Note**: For Gmail SMTP, generate an **App Password** from your Google Account settings rather than using your primary account password.

---

## 🚀 Running the Application Locally

### Using Maven Command Line

1. Open your terminal in the project root directory.
2. Run the application:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

3. Open your browser and navigate to: [http://localhost:8080](http://localhost:8080)

---

## 🐳 Docker Deployment

The application includes a `Dockerfile` for containerized execution.

### Building and Running Docker Image

1. Build the jar file:
   ```bash
   ./mvnw clean package -DskipTests
   ```
2. Build the Docker image:
   ```bash
   docker build -t smart-contact-manager .
   ```
3. Run the Docker container:
   ```bash
   docker run -p 8080:8080 --name scm-app smart-contact-manager
   ```
