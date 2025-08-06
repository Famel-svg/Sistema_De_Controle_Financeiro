<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Ubuntu:wght@300;400;500;700&display=swap" rel="stylesheet">

    <style>
        body {
            background: url('resources/backgroound.png') no-repeat center center fixed;
            background-size: cover;
            background-color: #3E363F;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            font-family: 'Ubuntu', sans-serif;
        }

        .login-container {
            background-color: #FFFCE8;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }

        .logo {
            font-size: 2.5rem;
            font-weight: bold;
            color: #212529;
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-bottom: 20px;
        }

        .logo img {
            height: 50px;
            margin-bottom: 10px;
        }

        .social-buttons {
            display: flex;
            justify-content: space-between;
            gap: 10px;
        }

        .btn-social {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fff;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .btn-social img {
            height: 20px;
            margin-right: 8px;
        }

        .btn-social:hover {
            transform: scale(1.1);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        button.btn-primary {
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        button.btn-primary:hover {
            background-color: #fff;
            color: #007bff;
        }

        .error {
            color: red;
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="login-container text-center">
    <div class="logo">
        <img width="70" src="https://img.icons8.com/material-sharp/100/57b943/thick-arrow-pointing-up.png" alt="logo"/>
        <div>Save Money</div>
    </div>

    <form action="login" method="post">
        <div class="mb-3">
            <input type="email" class="form-control" name="email" placeholder="Email" required>
        </div>

        <div class="mb-3">
            <input type="password" class="form-control" name="senha" placeholder="Senha" required>
            <div class="text-end">
                <a href="#" class="text-decoration-none small">Esqueci minha senha</a>
            </div>
        </div>

        <button type="submit" class="btn btn-primary w-100">Entrar</button>
    </form>

    <c:if test="${not empty erro}">
        <div class="error">${erro}</div>
    </c:if>

    <div class="mt-3 social-buttons">
        <button class="btn-social">
            <img src="https://img.icons8.com/ios-glyphs/30/facebook-new.png" alt="facebook-new"/>
            Facebook
        </button>
        <button class="btn-social">
            <img src="https://img.icons8.com/ios-filled/50/google-logo.png" alt="google-logo"/>
            Google
        </button>
        <button class="btn-social">
            <img src="https://img.icons8.com/ios-glyphs/240/mac-os.png" alt="mac-os"/>
            Apple
        </button>
    </div>

    <div class="mt-3">
        <a href="cadastrar.jsp" class="text-decoration-none">Cadastrar-se</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>