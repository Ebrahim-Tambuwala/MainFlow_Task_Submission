-- =====================================================
-- SMARTSTORE MANAGEMENT SYSTEM - FULL DATABASE SETUP
-- Supports Task 7 + Task 8 (Viewing & Reporting)
-- =====================================================

-- ================================
-- 1️ Create Database
-- ================================
DROP DATABASE IF EXISTS store_db;
CREATE DATABASE store_db;
USE store_db;

-- ================================
-- 2️ Users Table
-- ================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin','seller') NOT NULL DEFAULT 'seller',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, email, password, role) VALUES
('ebbo', 'ebbo@gmail.com', '123', 'admin'),
('seller1', 'seller1@gmail.com', '123', 'seller');

-- ================================
-- 3️ Products Table
-- ================================
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

-- ================================
-- 4️ Buyers Table
-- ================================
CREATE TABLE buyers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO buyers (name, email, phone, address) VALUES
('Rahul Sharma', 'rahul@test.com', '9876543210', 'Mumbai, India'),
('Anita Patel', 'anita@test.com', '9123456789', 'Pune, India'),
('Amit Verma', 'amit@test.com', '9988776655', 'Delhi, India');

-- ================================
-- 5️ Transactions Table
-- ================================
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    buyer_id INT NOT NULL,
    payment_method ENUM('Cash','Card','UPI') NOT NULL,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_status ENUM('Completed','Pending','Cancelled') DEFAULT 'Completed',
    notes TEXT,

    FOREIGN KEY (buyer_id) REFERENCES buyers(id)
        ON DELETE CASCADE
);

-- ================================
-- 6️ Transaction Items Table
-- ================================
CREATE TABLE transaction_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DOUBLE NOT NULL CHECK (unit_price >= 0),

    FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id)
        ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
        ON DELETE CASCADE
);

-- ================================
-- 7️ Sample Transactions
-- ================================

-- Transaction 1
INSERT INTO transactions (buyer_id, payment_method, order_status, notes)
VALUES (1, 'Card', 'Completed', 'Delivered successfully');

INSERT INTO transaction_items (transaction_id, product_id, quantity, unit_price)
VALUES 
(1, 1, 1, 55000),
(1, 3, 2, 1200);

-- Transaction 2
INSERT INTO transactions (buyer_id, payment_method, order_status, notes)
VALUES (2, 'UPI', 'Completed', 'Online payment');

INSERT INTO transaction_items (transaction_id, product_id, quantity, unit_price)
VALUES
(2, 2, 1, 25000),
(2, 4, 1, 700);

-- Transaction 3
INSERT INTO transactions (buyer_id, payment_method, order_status, notes)
VALUES (1, 'Cash', 'Pending', 'Awaiting pickup');

INSERT INTO transaction_items (transaction_id, product_id, quantity, unit_price)
VALUES
(3, 5, 2, 1500);

-- ================================
-- 8️ Performance Indexes
-- ================================
CREATE INDEX idx_buyer_name ON buyers(name);
CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_transaction_date ON transactions(purchase_date);

-- ================================
-- 9️ Useful Reporting Views (Optional but Professional)
-- ================================

-- Buyer Purchase Summary
CREATE VIEW buyer_purchase_summary AS
SELECT 
    b.id,
    b.name,
    COUNT(DISTINCT t.transaction_id) AS total_transactions,
    IFNULL(SUM(ti.quantity * ti.unit_price), 0) AS total_spent
FROM buyers b
LEFT JOIN transactions t ON b.id = t.buyer_id
LEFT JOIN transaction_items ti ON t.transaction_id = ti.transaction_id
GROUP BY b.id;

-- Full Transaction Details
CREATE VIEW transaction_full_details AS
SELECT 
    t.transaction_id,
    b.name AS buyer_name,
    p.name AS product_name,
    t.purchase_date,
    t.payment_method,
    t.order_status,
    ti.quantity,
    ti.unit_price,
    (ti.quantity * ti.unit_price) AS total_amount
FROM transactions t
JOIN buyers b ON t.buyer_id = b.id
JOIN transaction_items ti ON t.transaction_id = ti.transaction_id
JOIN products p ON ti.product_id = p.id;

-- =====================================================
-- DATABASE FULLY READY FOR TASK 8
-- =====================================================