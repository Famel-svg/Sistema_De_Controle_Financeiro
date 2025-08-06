<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Finanças Pessoais</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        html, body {
            margin: 0;
            padding: 0;
            height: 100vh;
            font-family: 'Segoe UI', sans-serif;
            overflow: hidden;
            background-color: #e9ecef;
        }

        .sidebar {
            position: fixed;
            top: 0;
            bottom: 0;
            left: 0;
            width: 80px;
            background: #ffffff;
            box-shadow: 2px 0 10px rgba(0,0,0,0.1);
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-top: 10px;
            z-index: 100;
        }

        .sidebar .logo {
            width: 50px;
            height: 50px;
            background-color: #ddd;
            border-radius: 50%;
            margin-bottom: 20px;
        }

        .sidebar a {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
            padding: 10px 0;
            color: #555;
            text-decoration: none;
            font-size: 12px;
            width: 100%;
        }

        .sidebar a:hover {
            background: #f0f0f0;
            color: #007bff;
        }

        .main-content {
            margin-left: 80px;
            padding: 10px;
            width: calc(100vw - 80px);
            height: calc(100vh - 20px);
        }

        .card-metric {
            border-radius: 10px;
            text-align: center;
            font-weight: bold;
            padding: 10px;
        }

        .equal-height {
            height: 100%;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .pie-container {
            width: 100%;
            max-width: 250px;
            margin: 0 auto;
        }

        .pie-container canvas {
            width: 100% !important;
            height: auto !important;
        }

        .remove-section {
            margin-top: 20px;
        }

        .remove-section h6 {
            margin-bottom: 10px;
        }

        .negative-value {
            color: #dc3545 !important;
        }
    </style>
</head>
<body>