SMARTSTORE MANAGEMENT SYSTEM
Task 5: Add and Delete Products/Buyers

---

PROJECT OVERVIEW

This is a Java Swing-based desktop application for managing products and buyers using MySQL and JDBC.

The project implements CRUD operations (Create, Read, Delete) with proper validation, confirmation dialogs, and structured architecture.

---

FEATURES

1. Authentication

   * Login system connected to MySQL
   * Credential verification using PreparedStatement
   * Error handling for invalid login attempts

2. Product Management

   * Add new products
   * Fields: Name, Category, Price, Quantity, Description
   * Input validation (numeric + required fields)
   * View products in non-editable table
   * Delete single product
   * Bulk delete support
   * Confirmation dialog before deletion
   * Auto-refresh after operations

3. Buyer Management

   * Add new buyers
   * Fields: Name, Email, Phone, Address
   * Email format validation
   * View buyers in table format
   * Bulk delete support
   * Confirmation dialog before deletion

---

PROJECT STRUCTURE

inventoryManagementSystem
│
├── DatabaseConnection.java
├── LoginForm.java
├── HomePage.java
│
├── model
│   ├── Product.java
│   └── Buyer.java
│
├── dao
│   ├── ProductDAO.java
│   └── BuyerDAO.java
│
└── ui
├── AddProductForm.java
├── ViewProductsForm.java
├── AddBuyerForm.java
└── ViewBuyersForm.java

---

DESIGN PRINCIPLES USED

* DAO Pattern (Data Access Object)
* Separation of UI and Database Logic
* MVC-like structure
* Exception handling
* Non-editable JTable implementation

---

TECH STACK

* Java (Swing)
* MySQL
* JDBC
* NetBeans IDE

---

DATABASE SETUP

Database Name: store_db

Tables:

* users
* products
* buyers

The file "store_db.sql" is included to recreate the database with sample data.

---

HOW TO RUN

1. Start MySQL server.
2. Execute store_db.sql in MySQL.
3. Open project in NetBeans.
4. Run LoginForm.java.

---

SAMPLE LOGIN

Username: ebbo
Password: 123

---

CONCLUSION

The SmartStore Management System successfully fulfills all Task 5 requirements including adding, displaying, validating, and deleting product and buyer records with proper confirmation and feedback. The project follows a structured and maintainable design suitable for academic submission and internship-level demonstration.

---

