<?php
$host = "localhost";
$user = "root";
$pass = "Ebbo@5405"; // your MySQL password
$db   = "user_auth";

$conn = mysqli_connect($host, $user, $pass, $db);

if (!$conn) {
    die("Database connection failed");
}
?>
