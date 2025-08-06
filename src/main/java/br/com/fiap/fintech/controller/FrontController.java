package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dao.GastoDAO;
import br.com.fiap.fintech.dao.ReceitaDAO;
import br.com.fiap.fintech.exception.FintechException;
import br.com.fiap.fintech.model.Gasto;
import br.com.fiap.fintech.model.Receita;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/front-controller")
public class FrontController extends HttpServlet {
    private GastoDAO gastoDAO;
    private ReceitaDAO receitaDAO;

    @Override
    public void init() {
        gastoDAO = new GastoDAO();
        receitaDAO = new ReceitaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if ("processar-transacao".equals(request.getParameter("acao"))) {
                processarTransacao(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação não reconhecida.");
            }
        } catch (Exception e) {
            request.setAttribute("javax.servlet.error.exception", e);
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        }
    }

    private void processarTransacao(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idCliente") == null) {
            throw new FintechException("Usuário não autenticado.");
        }

        int idCliente = (Integer) session.getAttribute("idCliente");
        String tipo = request.getParameter("tipo");
        double valor = Double.parseDouble(request.getParameter("valor"));
        String nome = request.getParameter("nomeDoGasto") != null ?
                request.getParameter("nomeDoGasto") : request.getParameter("nomeDaReceita");
        String descricao = request.getParameter("descricao") != null ? request.getParameter("descricao") : "";
        String dataStr = request.getParameter("data");

        if (dataStr == null || dataStr.trim().isEmpty())
            throw new FintechException("Campo ‘data’ está vazio.");

        Date data;
        try {
            data = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(dataStr).getTime());
        } catch (ParseException e) {
            throw new FintechException("Formato de data inválido: " + dataStr, e);
        }

        if ("Despesa".equalsIgnoreCase(tipo)) {
            Gasto gasto = new Gasto();
            gasto.setIdCliente(idCliente);
            gasto.setNomeDoGasto(nome);
            gasto.setDescricao(descricao);
            gasto.setValor(valor);
            gasto.setData(data);
            gastoDAO.inserir(gasto);
        } else if ("Receita".equalsIgnoreCase(tipo)) {
            Receita rec = new Receita();
            rec.setIdCliente(idCliente);
            rec.setNomeDaReceita(nome);
            rec.setDescricao(descricao);
            rec.setValor(valor);
            rec.setData(data);
            receitaDAO.inserir(rec);
        } else {
            throw new FintechException("Tipo de transação inválido: " + tipo);
        }
        response.sendRedirect("dashboard");
    }
}