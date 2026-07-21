# 03. Database & Domain Models

## Entity Relationship Overview

The core domain model consists of two primary entities: **User** and **Contact**.

- A **User** represents an account registered on the system.
- A **Contact** represents an individual contact entry owned by a specific User.
- **Relationship**: `User` **1 : N** `Contact` (One User can have multiple Contacts).

---

## 🗄 Entity Specifications

### 1. `User` Entity (`com.smart.entities.User`)

Represents registered accounts in the system.

| Field Name | Type | Annotations & Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `int` | `@Id`, `@GeneratedValue(strategy = AUTO)` | Primary Key |
| `name` | `String` | `@NotBlank`, `@Size(min=4, max=100)` | User's full name |
| `email` | `String` | `@Column(unique = true)` | User email address (username for auth) |
| `password` | `String` | None | Encrypted password (BCrypt) |
| `role` | `String` | None | Security role (e.g. `ROLE_USER`) |
| `enabled` | `boolean` | None | Account status indicator |
| `imageUrl` | `String` | None | Profile image file path / name |
| `about` | `String` | `@Column(length = 500)` | User bio or description |
| `contacts` | `List<Contact>` | `@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")` | Collection of owned contacts |

---

### 2. `Contact` Entity (`com.smart.entities.Contact`)

Represents individual contact records stored by users.

| Field Name | Type | Annotations & Constraints | Description |
| :--- | :--- | :--- | :--- |
| `cid` | `int` | `@Id`, `@GeneratedValue(strategy = AUTO)` | Contact Primary Key |
| `name` | `String` | None | First name |
| `secondName` | `String` | None | Last name / nickname |
| `work` | `String` | None | Profession or company |
| `email` | `String` | None | Contact email address |
| `phone` | `String` | None | Contact phone number |
| `image` | `String` | None | Uploaded contact image filename |
| `description` | `String` | `@Column(length = 5000)` | Notes or description |
| `user` | `User` | `@ManyToOne` | Reference to parent `User` entity |

---

## 📦 Data Access Objects (Repositories)

Spring Data JPA repositories manage database interactions without requiring custom SQL:

### 1. `UserRepository` (`com.smart.dao.UserRepository`)
- Extends `JpaRepository<User, Integer>`
- Key query method:
  ```java
  @Query("select u from User u where u.email = :email")
  public User getUserByUserName(@Param("email") String email);
  ```

### 2. `ContactRepository` (`com.smart.dao.ContactRepository`)
- Extends `JpaRepository<Contact, Integer>`
- Paginated user-specific contacts retrieval:
  ```java
  @Query("from Contact as c where c.user.id =:userId")
  public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);
  ```
- Search contact functionality:
  ```java
  public List<Contact> findByNameContainingAndUser(String keywords, User user);
  ```
