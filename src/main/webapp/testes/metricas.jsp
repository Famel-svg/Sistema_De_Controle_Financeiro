<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%
    // Recupera os atributos definidos pelo MetricasServlet
    Double receitasAttr = (Double) request.getAttribute("totalReceitas");
    Double gastosAttr = (Double) request.getAttribute("totalGastos");

    double totalReceitas = receitasAttr != null ? receitasAttr : 0.0;
    double totalGastos = gastosAttr != null ? gastosAttr : 0.0;
    double total = totalReceitas - totalGastos;

    // Formatação em moeda brasileira
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
%>

<div class="row g-2 mb-2">
    <div class="col-6 col-md-3">
        <div class="card-metric bg-success text-white">
            <h6>Total</h6>
            <h4><%= formatter.format(total) %></h4>
        </div>
    </div>
    <div class="col-6 col-md-3">
        <div class="card-metric bg-success-subtle text-success">
            <h6>Total de Receitas</h6>
            <h4><%= formatter.format(totalReceitas) %></h4>
        </div>
    </div>
    <div class="col-6 col-md-3">
        <div class="card-metric bg-danger-subtle text-danger">
            <h6>Total de Gastos</h6>
            <h4><%= formatter.format(totalGastos) %></h4>
        </div>
    </div>
    <div class="col-6 col-md-3">
        <div class="card-metric bg-warning-subtle text-warning">
            <h6>Metas</h6>
            <h4>Acompanhe suas metas!</h4>
        </div>
    </div>
</div>