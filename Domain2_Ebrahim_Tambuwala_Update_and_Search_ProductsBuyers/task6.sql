-- =============================================
-- SmartStore Management System Database Setup
-- =============================================

-- 1️ Create Database
CREATE DATABASE IF NOT EXISTS store_db;
USE store_db;

-- =============================================
-- 2️ Users Table (With Role Support)
-- =============================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'seller',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Sample Users
INSERT INTO users (username, email, password, role) VALUES
('ebbo', 'ebbo@gmail.com', '123', 'admin'),
('seller1', 'seller1@gmail.com', '123', 'seller');

-- =============================================
-- 3️⃣ Products Table
-- =============================================
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    description TEXT
);

-- Sample Products
INSERT INTO products (name, category, price, quantity, description) VALUES
('Laptop', 'Electronics', 55000, 10, '15-inch i5 laptop'),
('Smartphone', 'Electronics', 25000, 20, 'Android 8GB RAM'),
('Keyboard', 'Accessories', 1200, 50, 'Mechanical keyboard'),
('Mouse', 'Accessories', 700, 60, 'Wireless optical mouse'),
('Headphones', 'Accessories', 1500, 40, 'Noise cancelling'),
('Monitor', 'Electronics', 12000, 15, '24-inch LED monitor'),
('Printer', 'Electronics', 9000, 8, 'All-in-one printer'),
('USB Drive', 'Accessories', 600, 100, '32GB USB 3.0'),
('External HDD', 'Storage', 4500, 25, '1TB external hard drive'),
('Webcam', 'Accessories', 1800, 30, 'HD 1080p webcam');

-- =============================================
-- 4️⃣ Buyers Table
-- =============================================
CREATE TABLE IF NOT EXISTS buyers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT
);

-- Sample Buyers
INSERT INTO buyers (name, email, phone, address) VALUES
('Rahul Sharma', 'rahul@test.com', '9876543210', 'Mumbai, India'),
('Anita Patel', 'anita@test.com', '9123456789', 'Pune, India'),
('Amit Verma', 'amit@test.com', '9988776655', 'Delhi, India');

-- =============================================
-- Database Setup Completed
-- =============================================