# 04. Controllers & API Routes

The application exposes web endpoints handled by three primary Spring MVC Controllers.

---

## 🌐 1. Public Routes (`HomeController`)

Handles user registration, login routing, and general static pages.

| HTTP Method | Route Endpoint | Method Name | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | `home(Model model)` | Displays home page template (`home.html`). |
| `GET` | `/about` | `about(Model model)` | Displays about page (`about.html`). |
| `GET` | `/signup` | `signup(Model model)` | Displays registration form (`signup.html`). |
| `POST` | `/do_register` | `registerUser(...)` | Processes user registration, validates input, encrypts password, and saves user. |
| `GET` | `/signin` | `customLogin(Model model)` | Displays custom login page (`login.html`). |

---

## 🔒 2. Authenticated User Routes (`UserController`)

Base URL prefix: `/user`  
Requires authentication (`ROLE_USER`).

| HTTP Method | Route Endpoint | Method Name | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/user/index` | `dashboard(Model model, Principal principal)` | User dashboard displaying profile overview. |
| `GET` | `/user/add-contact` | `openAddContactForm(Model model)` | Displays add contact form (`add_contact_form.html`). |
| `POST` | `/user/process-contact` | `processContact(...)` | Handles contact creation and optional image file upload. |
| `GET` | `/user/show-contacts/{page}`| `showContacts(...)` | Retrieves paginated contacts (5 contacts per page). |
| `GET` | `/user/{cId}/contact` | `showContactDetail(...)` | Displays detail view for a specific contact. |
| `GET` | `/user/delete/{cid}` | `deleteContact(...)` | Deletes contact (ensuring ownership check). |
| `POST` | `/user/update-contact/{cid}`| `updateForm(...)` | Displays pre-filled update contact form. |
| `POST` | `/user/process-update` | `updateHandler(...)` | Saves contact edits and handles image replacement. |
| `GET` | `/user/profile` | `yourProfile(Model model)` | Renders user profile page (`profile.html`). |

---

## 🔑 3. Forgot Password Routes (`ForgotController`)

Handles email-based 4-digit OTP password reset functionality.

| HTTP Method | Route Endpoint | Method Name | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/forgot` | `openEmailForm()` | Renders email entry form (`forgot_email_form.html`). |
| `POST` | `/send-otp` | `sendOTP(...)` | Generates 4-digit random OTP, sends it to user email, and stores OTP in HTTP Session. |
| `POST` | `/verify-otp` | `verifyOtp(...)` | Compares user-entered OTP with session OTP; redirects to password change page if valid. |
| `POST` | `/change-password` | `changePassword(...)` | Encrypts new password with BCrypt and updates user account. |
