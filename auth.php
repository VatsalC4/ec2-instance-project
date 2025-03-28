<?php
session_start();
include 'db_connect.php'; // Connect to the database

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = trim($_POST['email']);
    $password = $_POST['password'];

    // Prepare query to fetch user details
    if ($stmt = $conn->prepare("SELECT id, name, password FROM users WHERE email = ?")) {
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();

        // Check if user exists
        if ($stmt->num_rows > 0) {
            $stmt->bind_result($id, $name, $hashed_password);
            $stmt->fetch();

            // Verify password using password_verify()
            if (password_verify($password, $hashed_password)) {
                // Store user session
                $_SESSION['user_id'] = $id;
                $_SESSION['user_name'] = $name;

                // Redirect to homepage
                header("Location: index.html");
                exit();
            } else {
                $_SESSION['error'] = "Invalid password. Try again.";
            }
        } else {
            $_SESSION['error'] = "No account found with this email.";
        }
        $stmt->close();
    } else {
        $_SESSION['error'] = "Database error. Please try again.";
    }
    $conn->close();
    
    // Redirect back to login page with error
    header("Location: login.html");
    exit();
}
?>
