<?php
session_start();
include "config/db.php";

$message = "";
$type = "error";

if ($_SERVER["REQUEST_METHOD"] === "POST") {

    $user = trim($_POST["user"]);
    $pass = $_POST["password"];

    if(empty($user) || empty($pass)){
        $message = "All fields are required";
    } else {

        $stmt = mysqli_prepare($conn, "SELECT id, password FROM users WHERE email=? OR username=?");
        mysqli_stmt_bind_param($stmt, "ss", $user, $user);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_bind_result($stmt, $id, $hashed);
        mysqli_stmt_fetch($stmt);

        if ($id && password_verify($pass, $hashed)) {

            session_regenerate_id(true);
            $_SESSION["user_id"] = $id;
            $_SESSION["username"] = $user;
            header("Location: dashboard.php");
            exit;

        } else {
            $message = "Incorrect username/email or password";
        }
    }
}
?>
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<link rel="stylesheet" href="css/style.css?v=2">
</head>

<body class="auth-body">

<div class="auth-container">

<form method="POST" class="auth-form">

<h2>Welcome Back</h2>
<p class="subtitle">Login to continue</p>

<div class="input-group">
<input type="text" name="user" placeholder=" " required>
<label>Username or Email</label>
</div>

<div class="input-group">
<input type="password" name="password" placeholder=" " required>
<label>Password</label>
</div>

<button type="submit" class="btn-primary">Login</button>

<?php if($message): ?>
<p class="<?= $type ?>"><?= htmlspecialchars($message) ?></p>
<?php endif; ?>

<p class="switch">
Don't have an account?
<a href="signup.php">Create Account</a>
</p>

</form>
</div>

</body>
</html>
