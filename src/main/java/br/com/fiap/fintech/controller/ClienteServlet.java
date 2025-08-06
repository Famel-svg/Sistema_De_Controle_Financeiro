package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dao.ClienteDAO;
import br.com.fiap.fintech.dao.DAOFactory;
import br.com.fiap.fintech.model.Cliente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
    private ClienteDAO clienteDAO;

    @Override
    public void init() { clienteDAO = DAOFactory.getClienteDAO(); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cliente cliente = new Cliente();
        cliente.setNome(request.getParameter("nome"));
        cliente.setEmail(request.getParameter("email"));
        cliente.setSenha(request.getParameter("senha"));

        try {
            clienteDAO.cadastrar(cliente);
            Cliente clienteComId = clienteDAO.buscarPorEmail(cliente.getEmail());
            request.getSession().setAttribute("idCliente", clienteComId.getId());
            response.sendRedirect("dashboard");
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao cadastrar.");
            request.getRequestDispatcher("cadastrar.jsp").forward(request, response);
        }
    }
}