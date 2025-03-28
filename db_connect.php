<?php
// Enable error reporting for debugging (Remove in production)
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

$host = getenv("DB_HOST") ?: "database-lab4.ccxog0o0ag95.us-east-1.rds.amazonaws.com"; 
$username = getenv("DB_USER") ?: "admin";
$password = getenv("DB_PASS") ?: "vatsal123$";
$database = getenv("DB_NAME") ?: "fitconnect";

// Create a connection
$conn = new mysqli($host, $username, $password, $database);

// Check for connection errors
if ($conn->connect_error) {
    error_log("Database connection failed: " . $conn->connect_error); // Log the error
    die("Database connection error. Please try again later.");
}
?>
