package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dao.ClienteDAO;
import br.com.fiap.fintech.model.Cliente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private ClienteDAO clienteDAO;

    @Override
    public void init() { clienteDAO = new ClienteDAO(); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        Cliente cliente = clienteDAO.buscarPorEmail(email);

        if (cliente == null || !cliente.getSenha().equals(senha)) {
            request.setAttribute("erro", "Email ou senha inválidos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("idCliente", cliente.getId());
            response.sendRedirect("dashboard");
        }
    }
}