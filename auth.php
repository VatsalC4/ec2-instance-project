<?php
session_start();

// Database Connection
$host = "localhost";
$dbname = "fitconnect";
$username = "root"; // Default XAMPP username
$password = "vatsal123"; // Update with actual password

$conn = new mysqli($host, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Database connection failed: " . $conn->connect_error);
}

// Handle User Sign-Up
if (isset($_POST['signup'])) {
    $name = trim($_POST['name']);
    $email = trim($_POST['email']);
    $password = $_POST['password']; // No trim() on password!

    // Check if email exists
    $stmt = $conn->prepare("SELECT id FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $_SESSION['message'] = "Email already exists. Try logging in.";
        header("Location: signup.html"); // Redirect to signup page
        exit();
    } else {
        // Hash password securely
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        
        // Insert new user
        $stmt = $conn->prepare("INSERT INTO users (name, email, password) VALUES (?, ?, ?)");
        $stmt->bind_param("sss", $name, $email, $hashed_password);

        if ($stmt->execute()) {
            $_SESSION['message'] = "Sign-up successful! You can now log in.";
            header("Location: login.html");
            exit();
        } else {
            $_SESSION['message'] = "Error: " . $stmt->error;
            header("Location: signup.html");
            exit();
        }
    }
    $stmt->close();
}

session_start();
$host = "localhost";
$dbname = "fitconnect";
$username = "root";
$password = "vatsal123"; 

// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Handle User Login
if (isset($_POST['email']) && isset($_POST['password'])) {
    $email = trim($_POST['email']);
    $password = $_POST['password'];

    $stmt = $conn->prepare("SELECT id, name, password FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($id, $name, $hashed_password);
        $stmt->fetch();

        if (password_verify($password, $hashed_password)) {
            $_SESSION['user_id'] = $id;
            $_SESSION['user_name'] = $name;

            // Redirect using JavaScript (better UX)
            echo "<script>
                    alert('Login successful! Welcome, $name');
                    window.location.href = 'index.html';
                  </script>";
            exit();
        } else {
            echo "<script>alert('Invalid password. Try again.');</script>";
        }
    } else {
        echo "<script>alert('No account found with this email.');</script>";
    }
    $stmt->close();
}
$conn->close();

?>
