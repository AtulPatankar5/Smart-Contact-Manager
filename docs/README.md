# Smart Contact Manager - Documentation Hub 📚

Welcome to the documentation suite for **Smart Contact Manager**, a full-featured Spring Boot web application for managing user contacts securely in the cloud.

---

## 📂 Documentation Table of Contents

| Document | Description |
| :--- | :--- |
| **[01. Overview & Architecture](01-overview-and-architecture.md)** | High-level system design, technology stack, directory structure, and component overview. |
| **[02. Getting Started & Setup](02-getting-started-and-setup.md)** | Local environment requirements, Maven/IDE setup, MySQL config, and Docker deployment. |
| **[03. Database & Domain Models](03-database-and-domain-models.md)** | Entity mappings (`User`, `Contact`), ORM relationships, table schemas, and ER diagram references. |
| **[04. Controllers & API Routes](04-controllers-and-api-routes.md)** | Map of all HTTP endpoints across `HomeController`, `UserController`, and `ForgotController`. |
| **[05. Security & Authentication](05-security-and-authentication.md)** | Spring Security configuration, BCrypt hashing, custom UserDetailsService, and access rules. |
| **[06. User Guide & Features](06-user-guide-and-features.md)** | Comprehensive user instructions for registration, contact management, pagination, profile, and OTP password recovery. |

---

## 🛠 Project Tech Stack Summary

- **Backend Framework**: Spring Boot 4.0.6 (Java 17)
- **Security**: Spring Security 6+ with BCrypt Password Encoder
- **Database**: MySQL 8.x with Hibernate JPA ORM
- **Templating Engine**: Thymeleaf
- **Mail Service**: JavaMailSender (SMTP)
- **Build Tool**: Apache Maven
- **Containerization**: Docker
