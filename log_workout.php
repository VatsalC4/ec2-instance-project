<?php
session_start();
require 'db_connect.php'; // Make sure you have a database connection file

// Check if the user is logged in
if (!isset($_SESSION['user_id'])) {
    $_SESSION['error'] = "You must log in to log a workout.";
    header("Location: login.php");
    exit();
}

// Handle form submission
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $user_id = $_SESSION['user_id'];
    $workout_type = $_POST['workout_type'];
    $duration = $_POST['duration'];
    $date = $_POST['date'];

    // Validate inputs
    if (empty($workout_type) || empty($duration) || empty($date)) {
        $_SESSION['error'] = "All fields are required.";
        header("Location: dashboard.php");
        exit();
    }

    // Insert workout into database
    $stmt = $conn->prepare("INSERT INTO workouts (user_id, workout_type, duration, date) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("isss", $user_id, $workout_type, $duration, $date);

    if ($stmt->execute()) {
        $_SESSION['success'] = "Workout logged successfully!";
    } else {
        $_SESSION['error'] = "Error logging workout.";
    }

    $stmt->close();
    $conn->close();

    header("Location: dashboard.php"); // Redirect back to the dashboard
    exit();
} else {
    // Redirect if accessed directly
    header("Location: dashboard.php");
    exit();
}
?>
