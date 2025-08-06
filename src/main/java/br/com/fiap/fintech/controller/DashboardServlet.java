package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dao.DAOFactory;
import br.com.fiap.fintech.dao.GastoDAO;
import br.com.fiap.fintech.dao.ReceitaDAO;
import br.com.fiap.fintech.model.Gasto;
import br.com.fiap.fintech.model.Receita;
import br.com.fiap.fintech.model.Transacao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private GastoDAO gastoDAO;
    private ReceitaDAO receitaDAO;

    @Override
    public void init() {
        gastoDAO = DAOFactory.getGastoDAO();
        receitaDAO = DAOFactory.getReceitaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer idCliente = (Integer) session.getAttribute("idCliente");

        if (idCliente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Gasto> gastos = gastoDAO.listarPorCliente(idCliente);
            List<Receita> receitas = receitaDAO.listarPorCliente(idCliente);
            List<Transacao> transacoes = new ArrayList<>();

            gastos.forEach(g -> {
                g.setTipo("GASTO");
                transacoes.add(new Transacao(
                        g.getIdGasto(), g.getIdCliente(), g.getNomeDoGasto(),
                        g.getValor(), g.getDescricao(), g.getData(), "GASTO"
                ));
            });

            receitas.forEach(r -> {
                r.setTipo("RECEITA");
                transacoes.add(new Transacao(
                        r.getIdReceita(), r.getIdCliente(), r.getNomeDaReceita(),
                        r.getValor(), r.getDescricao(), r.getData(), "RECEITA"
                ));
            });

            transacoes.sort((a, b) -> b.getData().compareTo(a.getData()));

            request.setAttribute("gastos", gastos);
            request.setAttribute("receitas", receitas);
            request.setAttribute("totalGastos", gastos.stream().mapToDouble(Gasto::getValor).sum());
            request.setAttribute("totalReceitas", receitas.stream().mapToDouble(Receita::getValor).sum());
            request.setAttribute("transacoes", transacoes);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Erro ao processar dashboard", e);
        }
    }
}