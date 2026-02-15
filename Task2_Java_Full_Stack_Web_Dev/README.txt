**LOGIN & SIGNUP SYSTEM (PHP + MySQL)**

---

### HOW TO RUN THE PROJECT

1. Copy the project folder into **htdocs** (XAMPP/LAMPP)
2. Start **Apache** and **MySQL**
3. Open **phpMyAdmin**
4. Create database:
   `user_auth`
5. Import `database.sql`
6. Open in browser:
   `http://localhost/Task2_Java_Full_Stack_Web_Dev/login.php`

---

### SYSTEM FLOW

* New user → Signup
* Existing user → Login
* After login → redirected to **index.php (protected website)**
* Session keeps user logged in
* Logout → session destroyed → redirected to login
* Direct access to index.php without login → blocked

---

### FEATURES IMPLEMENTED

* User Registration (username, email, password)
* Login Authentication (username/email + password)
* Password Hashing using `password_hash()` & `password_verify()`
* Prepared Statements (SQL Injection protection)
* Session Management
* Protected Website Access
* Secure Logout
* Error Handling & Input Validation
* Responsive UI Forms

---

### TECHNOLOGIES USED

Frontend: HTML, CSS
Backend: PHP
Database: MySQL
