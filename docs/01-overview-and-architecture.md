# 01. Overview & Architecture

## System Overview

**Smart Contact Manager** is a cloud-accessible, web-based contact management application. It provides registered users with a personal environment to create, update, delete, view, search, and manage their contact lists.

Key capabilities include:
- Secure authentication & account creation with input validation.
- User-isolated contact lists (users can only view and manage their own contacts).
- Image uploads for contact profile photos.
- Contact search functionality.
- Paginated listing of contacts.
- Password recovery using 4-digit OTP delivered via email.

---

## 🏗 High-Level Architecture

The project follows a standard **MVC (Model-View-Controller)** pattern combined with Spring Data JPA for persistence:

```
                  ┌─────────────────────────────────┐
                  │          Client Browser         │
                  └────────────────┬────────────────┘
                                   │ HTTP Request / Response
                                   ▼
                  ┌─────────────────────────────────┐
                  │       Thymeleaf UI Views        │
                  │  (HTML5, CSS3, JS, Bootstrap)   │
                  └────────────────┬────────────────┘
                                   │
                                   ▼
                  ┌─────────────────────────────────┐
                  │        Spring Controllers       │
                  │ (Home, User, Forgot Controllers)│
                  └────────────────┬────────────────┘
                                   │
                                   ▼
                  ┌─────────────────────────────────┐
                  │       Spring Security Layer     │
                  │ (BCrypt, CustomUserDetailsService)│
                  └────────────────┬────────────────┘
                                   │
                                   ▼
                  ┌─────────────────────────────────┐
                  │     Spring Data JPA Repos       │
                  │ (UserRepository, ContactRepo)   │
                  └────────────────┬────────────────┘
                                   │
                                   ▼
                  ┌─────────────────────────────────┐
                  │           MySQL Database        │
                  │        (User & Contact Tables)  │
                  └─────────────────────────────────┘
```

---

## 🧰 Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 17 |
| **Framework** | Spring Boot 4.0.6 |
| **Web & Templating** | Spring MVC, Thymeleaf |
| **Security** | Spring Security |
| **ORM / Data Access** | Spring Data JPA / Hibernate |
| **Database** | MySQL Database |
| **Validation** | Hibernate Validator / `javax.validation` / `jakarta.validation` |
| **Email Service** | Spring Starter Mail (`JavaMailSender` over SMTP) |
| **Build & Tooling** | Apache Maven, Spring Boot DevTools |
| **Containerization** | Docker |

---

## 📁 Repository Directory Structure

```
Smart-Contact-Manager/
├── Dockerfile                         # Container build definition
├── pom.xml                            # Maven dependencies & build configuration
├── src/
│   ├── main/
│   │   ├── java/com/smart/
│   │   │   ├── SmartContactManagerApplication.java  # Main Boot entry point
│   │   │   ├── config/                # Spring Security & UserDetails configurations
│   │   │   ├── controller/            # MVC Web Controllers (Home, User, Forgot)
│   │   │   ├── dao/                   # Spring Data JPA Repository Interfaces
│   │   │   ├── entities/              # JPA Entities (User, Contact)
│   │   │   └── helper/                # Utility classes (Message alert helpers)
│   │   └── resources/
│   │       ├── application.properties # Spring Boot configuration
│   │       ├── static/                # Static assets (images, CSS, JS)
│   │       └── templates/             # Thymeleaf HTML views
│   └── test/                          # Unit and Integration Tests
└── docs/                              # Project Documentation Suite
```
