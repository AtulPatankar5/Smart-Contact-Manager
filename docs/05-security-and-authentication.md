# 05. Security & Authentication

Smart Contact Manager utilizes **Spring Security** to provide authentication and authorization.

---

## 🛡 Configuration Setup (`com.smart.config.MyConfig`)

The security architecture relies on standard Spring Security bean configurations:

```java
@Configuration
@EnableWebSecurity
public class MyConfig {

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
}
```

---

## 🔑 Authentication Workflow

1. **User Request**: User submits credentials via `/signin` form.
2. **User Details Service (`UserDetailsServiceImpl`)**:
   - Queries `UserRepository` by email: `userRepository.getUserByUserName(username)`.
   - Returns a `CustomUserDetails` wrapper around the `User` entity.
3. **Password Encoder (`BCryptPasswordEncoder`)**:
   - Compares raw submitted password against stored BCrypt hash.
4. **Security Context**:
   - Upon successful verification, Spring Security populates the Security Context with `ROLE_USER` authorities.
   - User is granted access to protected `/user/**` routes.

---

## 🔐 Route Access Control Rules

| Route Pattern | Access Control |
| :--- | :--- |
| `/admin/**` | Restricted to `ROLE_ADMIN` |
| `/user/**` | Restricted to `ROLE_USER` |
| `/**` (Home, About, Signup, Forgot Password, Static Resources) | Public access (`permitAll`) |

---

## 🛡 Data Isolation & Authorization Security

To prevent users from accessing or modifying other users' contacts (e.g. via URL ID manipulation), controllers perform explicit ownership validation:

```java
// Example check in UserController
String name = principal.getName();
User user = this.userRepository.getUserByUserName(name);
Contact contact = this.contactRepository.findById(cId).get();

if (user.getId() == contact.getUser().getId()) {
    model.addAttribute("contact", contact);
}
```
