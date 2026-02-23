-- 1. Create and select the database
CREATE DATABASE store_db;
USE store_db;

-- 2. Create the Product Table
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    category VARCHAR(100),
    price DOUBLE,
    quantity INT,
    description TEXT
);

-- 3. Create the Buyer Table
CREATE TABLE buyers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT
);

USE store_db;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
Insert into users (username,email,password)  values('ebbo','ebbo@gmail.com', '123');
select * from users;

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

INSERT INTO buyers (name, email, phone, address) VALUES
('Rahul Sharma', 'rahul@test.com', '9876543210', 'Mumbai, India'),
('Anita Patel', 'anita@test.com', '9123456789', 'Pune, India'),
('Amit Verma', 'amit@test.com', '9988776655', 'Delhi, India');