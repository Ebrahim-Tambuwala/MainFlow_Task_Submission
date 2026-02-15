<?php
$host = "localhost";
$user = "root";
$pass = ""; // your MySQL password
$db   = "user_auth";

$conn = mysqli_connect($host, $user, $pass, $db);

if (!$conn) {
    die("Database connection failed");
}
?>
