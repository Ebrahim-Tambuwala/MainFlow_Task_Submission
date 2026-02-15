<?php
include "config/db.php";
$message = "";
$type = ""; // success or error

if ($_SERVER["REQUEST_METHOD"] === "POST") {

    $username = trim($_POST["username"]);
    $email    = trim($_POST["email"]);
    $pass     = $_POST["password"];
    $cpass    = $_POST["confirm_password"];

    if (empty($username) || empty($email) || empty($pass) || empty($cpass)) {
        $message = "All fields are required";
        $type = "error";

    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $message = "Invalid email format";
        $type = "error";

    } elseif ($pass !== $cpass) {
        $message = "Passwords do not match";
        $type = "error";

    } else {

        $check = mysqli_prepare($conn, "SELECT id FROM users WHERE email=? OR username=?");
        mysqli_stmt_bind_param($check, "ss", $email, $username);
        mysqli_stmt_execute($check);
        mysqli_stmt_store_result($check);

        if (mysqli_stmt_num_rows($check) > 0) {
            $message = "Username or Email already exists";
            $type = "error";
        } else {

            $hashed = password_hash($pass, PASSWORD_DEFAULT);

            $stmt = mysqli_prepare($conn, "INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
            mysqli_stmt_bind_param($stmt, "sss", $username, $email, $hashed);
            mysqli_stmt_execute($stmt);

            $message = "Signup successful! You can login now.";
            $type = "success";
        }
    }
}
?>
<!DOCTYPE html>
<html>
<head>
<title>Signup</title>
<link rel="stylesheet" href="css/style.css?v=2">
</head>

<body class="auth-body">

<div class="auth-container">

<form method="POST" class="auth-form">

<h2>Create Account</h2>
<p class="subtitle">Join SmartStore today</p>

<div class="input-group">
<input type="text" name="username" placeholder=" " required>
<label>Username</label>
</div>

<div class="input-group">
<input type="email" name="email" placeholder=" " required>
<label>Email Address</label>
</div>

<div class="input-group">
<input type="password" name="password" placeholder=" " required>
<label>Password</label>
</div>

<div class="input-group">
<input type="password" name="confirm_password" placeholder=" " required>
<label>Confirm Password</label>
</div>

<button type="submit" class="btn-primary">Create Account</button>

<?php if($message): ?>
<p class="<?= $type ?>"><?= htmlspecialchars($message) ?></p>
<?php endif; ?>

<p class="switch">
Already have an account?
<a href="login.php">Login</a>
</p>

</form>
</div>

</body>
</html>
