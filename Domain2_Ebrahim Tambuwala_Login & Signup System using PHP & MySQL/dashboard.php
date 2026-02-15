<?php
session_start();

if (!isset($_SESSION["user_id"])) {
    header("Location: login.php");
    exit;
}

$username = htmlspecialchars($_SESSION["username"] ?? "User");
?>
<!doctype html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/style_dashboard.css">

<title>SmartStore | Dashboard</title>
</head>

<body>

<header class="header">
  <div class="logo">
    <img src="images/logo.png" alt="SmartStore Logo">
  </div>

  <div class="menu-icon" id="mobile-btn">☰</div>

  <nav class="navbar">
    <ul class="nav-links" id="nav-menu">

      <li><a href="#home">Home</a></li>

      <li class="dropdown">
        <a href="#" class="dropdown-link">Features</a>
        <ul class="dropdown-menu">
          <li><a href="#features">Inventory</a></li>
          <li><a href="#features">Billing</a></li>
          <li><a href="#features">Reports</a></li>
        </ul>
      </li>

      <li class="dropdown">
        <a href="#" class="dropdown-link">Services</a>
        <ul class="dropdown-menu">
          <li><a href="#">Installation</a></li>
          <li><a href="#">Training</a></li>
          <li><a href="#">Support</a></li>
        </ul>
      </li>

      <li><a href="#contact">Contact</a></li>

      <li class="welcome">Hi, <?= $username ?></li>
      <li><a href="logout.php" class="logout-btn">Logout</a></li>

    </ul>
  </nav>
</header>

<!-- HERO -->
<section class="hero" id="home">
  <div class="hero-content">
    <h1>Welcome <?= $username ?> 👋</h1>
    <p>Your SmartStore dashboard is ready to use</p>
    <button class="cta" id="cta-btn">Explore Features</button>
  </div>
</section>

<!-- FEATURES -->
<section class="features" id="features">
  <h2>Your System Modules</h2>

  <div class="feature-box">
    <div class="feature">
      <img src="images/inventory.png" alt="Inventory">
      <h3>Inventory Management</h3>
      <p>Track stock, update items and manage products easily.</p>
    </div>

    <div class="feature">
      <img src="images/billing.png" alt="Billing">
      <h3>Billing System</h3>
      <p>Generate invoices and calculate totals automatically.</p>
    </div>

    <div class="feature">
      <img src="images/security.png" alt="Security">
      <h3>Secure Access</h3>
      <p>Your account is protected with authentication.</p>
    </div>
  </div>
</section>

<!-- CONTACT -->
<section class="contact" id="contact">
  <h2>Contact Us</h2>
  <p>Email: support@smartstore.com</p>
</section>

<footer class="footer">
  <p>© 2026 SmartStore. All rights reserved.</p>
</footer>

<script>
const mobileBtn = document.getElementById("mobile-btn");
const navMenu = document.getElementById("nav-menu");
const dropdownLinks = document.querySelectorAll(".dropdown-link");
const ctaBtn = document.getElementById("cta-btn");

mobileBtn.onclick = () => navMenu.classList.toggle("active");

dropdownLinks.forEach(link=>{
  link.addEventListener("click",e=>{
    if(window.innerWidth<=768){
      e.preventDefault();
      const parent = link.parentElement;
      document.querySelectorAll(".dropdown.active")
      .forEach(d=>d!==parent && d.classList.remove("active"));
      parent.classList.toggle("active");
    }
  });
});

ctaBtn.onclick=()=>document.querySelector("#features")
.scrollIntoView({behavior:"smooth"});

document.querySelectorAll(".nav-links a").forEach(link=>{
 link.addEventListener("click",()=>navMenu.classList.remove("active"));
});
</script>

</body>
</html>
