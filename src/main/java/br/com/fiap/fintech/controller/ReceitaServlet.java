package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dao.ReceitaDAO;
import br.com.fiap.fintech.exception.FintechException;
import br.com.fiap.fintech.model.Receita;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/receita")
public class ReceitaServlet extends HttpServlet {
    private ReceitaDAO receitaDAO;

    @Override
    public void init() { receitaDAO = new ReceitaDAO(); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCliente = IdSession.getClienteId(request);
            String nomeDaReceita = request.getParameter("nomeDaReceita");
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

            Receita receita = new Receita();
            receita.setIdCliente(idCliente);
            receita.setNomeDaReceita(nomeDaReceita);
            receita.setValor(valor);
            receita.setDescricao(descricao);
            receita.setData(data);

            receitaDAO.inserir(receita);
            response.sendRedirect("dashboard");
        } catch (FintechException e) {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Receita> lista = receitaDAO.listarPorCliente(IdSession.getClienteId(request));
            request.setAttribute("listaReceitas", lista);
            request.getRequestDispatcher("lista-receitas.jsp").forward(request, response);
        } catch (FintechException e) {
            response.sendRedirect("login.jsp");
        }
    }
}