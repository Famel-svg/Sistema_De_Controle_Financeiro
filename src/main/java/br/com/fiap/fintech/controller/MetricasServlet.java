package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dao.GastoDAO;
import br.com.fiap.fintech.dao.ReceitaDAO;
import br.com.fiap.fintech.exception.FintechException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/carregar-metricas")
public class MetricasServlet extends HttpServlet {
    private ReceitaDAO receitaDAO;
    private GastoDAO gastoDAO;

    @Override
    public void init() {
        receitaDAO = new ReceitaDAO();
        gastoDAO = new GastoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idCliente = IdSession.getClienteId(request);
            request.setAttribute("totalReceitas", receitaDAO.calcularTotalReceitas(idCliente));
            request.setAttribute("totalGastos", gastoDAO.calcularTotalGastos(idCliente));
            request.getRequestDispatcher("metricas.jsp").forward(request, response);
        } catch (FintechException e) {
            response.sendRedirect("login.jsp");
        }
    }
}