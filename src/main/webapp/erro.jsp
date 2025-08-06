<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Erro no Sistema</title>
    <link rel="stylesheet" href="resources/css/bootstrap.css">
    <style>
        body { background-color: #f8f9fa; }
        .stacktrace {
            text-align: left;
            font-size: 0.85em;
            max-height: 400px;
            overflow-y: auto;
        }
    </style>
</head>
<body class="text-center">
<div class="container mt-5">
    <h1 class="text-danger">⚠️ Ocorreu um erro inesperado</h1>
    <p class="lead">A aplicação encontrou um problema ao processar sua solicitação.</p>

    <div class="alert alert-warning text-start mx-auto mt-4" style="max-width: 800px;">
        <p><strong>Tipo de exceção:</strong>
            <%= exception != null ? exception.getClass().getName() : "Não informado" %>
        </p>

        <p><strong>Mensagem:</strong>
            <%= exception != null ? exception.getMessage() : "Sem detalhes." %>
        </p>

        <% Throwable causa = exception != null ? exception.getCause() : null; %>
        <% if (causa != null) { %>
        <p><strong>Causa raiz:</strong>
            <%= causa.getClass().getName() %> — <%= causa.getMessage() %>
        </p>
        <% } %>
    </div>

    <details class="bg-white border rounded p-3 mx-auto mt-3" style="max-width: 800px;">
        <summary><strong>📋 Stack Trace Completo</strong></summary>
        <pre class="stacktrace">
<%
    if (exception != null) {
        for (StackTraceElement element : exception.getStackTrace()) {
            out.println(element.toString());
        }
    } else {
        out.println("Sem exceção capturada.");
    }
%>
        </pre>
    </details>

    <div class="mt-4">
        <a href="dashboard.jsp" class="btn btn-primary">🔙 Voltar ao Início</a>
        <a href="javascript:history.back()" class="btn btn-secondary">⬅️ Voltar para a página anterior</a>
    </div>
</div>
</body>
</html>