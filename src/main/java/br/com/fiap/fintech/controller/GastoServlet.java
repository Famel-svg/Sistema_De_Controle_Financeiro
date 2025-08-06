package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dao.GastoDAO;
import br.com.fiap.fintech.exception.FintechException;
import br.com.fiap.fintech.model.Gasto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/gasto")
public class GastoServlet extends HttpServlet {
    private GastoDAO gastoDAO;

    @Override
    public void init() { gastoDAO = new GastoDAO(); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCliente = IdSession.getClienteId(request);
            String nomeDoGasto = request.getParameter("nomeDoGasto");
            double valor = Double.parseDouble(request.getParameter("valor"));
            String descricao = request.getParameter("descricao") != null ? request.getParameter("descricao") : "";
            String dataStr = request.getParameter("data");

            Date data = null;
            if (dataStr != null && !dataStr.trim().isEmpty()) {
                try {
                    data = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(dataStr).getTime());
                } catch (ParseException e) {
                    throw new FintechException("Erro ao converter a data: " + dataStr, e);
                }
            } else {
                throw new FintechException("Campo 'data' está vazio ou nulo.");
            }

            Gasto gasto = new Gasto();
            gasto.setIdCliente(idCliente);
            gasto.setNomeDoGasto(nomeDoGasto);
            gasto.setValor(valor);
            gasto.setDescricao(descricao);
            gasto.setData(data);

            gastoDAO.inserir(gasto);
            response.sendRedirect("dashboard");
        } catch (FintechException e) {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            List<Gasto> lista = gastoDAO.listarPorCliente(IdSession.getClienteId(request));
            request.setAttribute("listaGastos", lista);
            request.getRequestDispatcher("lista-gastos.jsp").forward(request, response);
        } catch (FintechException e) {
            response.sendRedirect("login.jsp");
        }
    }
}