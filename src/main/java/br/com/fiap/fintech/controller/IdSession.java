package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.exception.FintechException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class IdSession {
    public static int getClienteId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idCliente") == null)
            throw new FintechException("Sessão expirada ou usuário não autenticado.");
        return (int) session.getAttribute("idCliente");
    }
}