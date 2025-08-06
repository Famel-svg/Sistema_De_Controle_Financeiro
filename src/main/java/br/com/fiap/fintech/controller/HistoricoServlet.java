package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dao.GastoDAO;
import br.com.fiap.fintech.dao.ReceitaDAO;
import br.com.fiap.fintech.model.Gasto;
import br.com.fiap.fintech.model.Receita;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

@WebServlet("/transacao")
public class HistoricoServlet extends HttpServlet {
    private final GastoDAO gastoDAO = new GastoDAO();
    private final ReceitaDAO receitaDAO = new ReceitaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect("dashboard");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String action = req.getParameter("action");
        String tipo = req.getParameter("tipo");
        String idStr = req.getParameter("id");

        if (idStr == null || idStr.trim().isEmpty()) {
            res.sendRedirect("dashboard");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            if ("editar".equalsIgnoreCase(action)) editarTransacao(req, tipo, id);
            else if ("excluir".equalsIgnoreCase(action)) excluirTransacao(tipo, id);
            res.sendRedirect("dashboard");
        } catch (Exception e) {
            throw new ServletException("Erro ao processar transação", e);
        }
    }

    private void editarTransacao(HttpServletRequest req, String tipo, int id) throws Exception {
        String nome = req.getParameter("nome");
        double valor = Double.parseDouble(req.getParameter("valor"));
        String descricao = req.getParameter("descricao");
        java.util.Date data = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("data"));

        if ("GASTO".equalsIgnoreCase(tipo)) {
            Gasto gasto = gastoDAO.buscarPorId(id);
            gasto.setNomeDoGasto(nome);
            gasto.setValor(valor);
            gasto.setDescricao(descricao);
            gasto.setData(data);
            gastoDAO.atualizar(gasto);
        } else {
            Receita receita = receitaDAO.buscarPorId(id);
            receita.setNomeDaReceita(nome);
            receita.setValor(valor);
            receita.setDescricao(descricao);
            receita.setData(data);
            receitaDAO.atualizar(receita);
        }
    }

    private void excluirTransacao(String tipo, int id) {
        if ("GASTO".equalsIgnoreCase(tipo)) gastoDAO.deletar(id);
        else receitaDAO.deletar(id);
    }
}