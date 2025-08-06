<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Modal de Edição -->
<div id="editModal" class="modal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background-color:rgba(0,0,0,0.5); z-index:1050;">
    <div style="background:#fff; margin:10% auto; padding:20px; width:400px; border-radius:8px; position:relative;">
        <h5 class="mb-3">Editar Transação</h5>
        <form action="transacao" method="post">
            <input type="hidden" name="action" value="editar">
            <input type="hidden" name="id" id="modal-id">
            <input type="hidden" name="tipo" id="modal-tipo">

            <div class="mb-2">
                <label for="modal-nome" class="form-label">Nome do Gasto/Receita</label>
                <input type="text" class="form-control" id="modal-nome" name="nome" required>
            </div>

            <div class="mb-2">
                <label for="modal-valor" class="form-label">Valor</label>
                <input type="number" step="0.01" class="form-control" id="modal-valor" name="valor" required>
            </div>

            <div class="mb-2">
                <label for="modal-descricao" class="form-label">Descrição (opcional)</label>
                <input type="text" class="form-control" id="modal-descricao" name="descricao">
            </div>

            <div class="mb-3">
                <label for="modal-data" class="form-label">Data</label>
                <input type="date" class="form-control" id="modal-data" name="data" required>
            </div>

            <div class="d-flex justify-content-end">
                <button type="button" class="btn btn-secondary me-2" onclick="closeEditModal()">Cancelar</button>
                <button type="submit" class="btn btn-primary">Salvar</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal de Confirmação de Exclusão -->
<div id="deleteModal" class="modal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background-color:rgba(0,0,0,0.5); z-index:1050;">
    <div style="background:#fff; margin:10% auto; padding:20px; width:400px; border-radius:8px; position:relative;">
        <h5 class="mb-3">Deseja apagar essa transação/gasto?</h5>
        <div id="delete-info" class="mb-3"></div>

        <form id="deleteForm" action="transacao" method="post">
            <input type="hidden" name="action" value="excluir">
            <input type="hidden" name="tipo" id="delete-tipo">
            <input type="hidden" name="id" id="delete-id">

            <div class="d-flex justify-content-end">
                <button type="button" class="btn btn-light text-primary me-2" onclick="closeDeleteModal()">Cancelar</button>
                <button type="submit" class="btn btn-danger">Apagar</button>
            </div>
        </form>
    </div>
</div>

<!-- Lista de Transações -->
<div class="card equal-height">
    <div class="card-header"><strong>Últimas Transações</strong></div>
    <div class="card-body p-2">
        <div class="remove-section">
            <ul class="list-group small">
                <c:forEach var="transacao" items="${transacoes}">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <div>
                            <strong>
                                <fmt:formatDate value="${transacao.data}" pattern="dd/MM/yyyy" />
                            </strong>
                            <br />
                            <span>${transacao.nome}</span>
                        </div>

                        <div class="d-flex align-items-center">
                            <span class="${transacao.tipo == 'RECEITA' ? 'text-success' : 'text-danger'} me-3">
                                <c:choose>
                                    <c:when test="${transacao.tipo == 'RECEITA'}">+ </c:when>
                                    <c:otherwise>- </c:otherwise>
                                </c:choose>
                                <fmt:formatNumber value="${transacao.valor}" type="currency" currencySymbol="R$ " />
                            </span>

                            <!-- Botão Editar -->
                            <button type="button" class="btn btn-sm btn-outline-secondary p-1 me-2"
                                    onclick="openEditModal(
                                            '${transacao.id}',
                                            '${transacao.tipo}',
                                            '${transacao.nome}',
                                            '${transacao.valor}',
                                            '${transacao.descricao}',
                                            '<fmt:formatDate value="${transacao.data}" pattern="yyyy-MM-dd" />'
                                            )">
                                <img width="20" height="20" src="https://img.icons8.com/material-two-tone/24/edit--v1.png" alt="edit"/>
                            </button>

                            <!-- Botão Deletar -->
                            <button type="button" class="btn btn-sm btn-outline-danger p-1"
                                    onclick="openDeleteModal(
                                            '${transacao.id}',
                                            '${transacao.tipo}',
                                            '${transacao.nome}',
                                            '${transacao.valor}',
                                            '${transacao.descricao}'
                                            )">
                                <img width="20" height="20" src="https://img.icons8.com/material-outlined/50/delete-sign.png" alt="delete"/>
                            </button>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>

<!-- JavaScript para Modais -->
<script>
    function openEditModal(id, tipo, nome, valor, descricao, data) {
        document.getElementById("modal-id").value = id;
        document.getElementById("modal-tipo").value = tipo;
        document.getElementById("modal-nome").value = nome;
        document.getElementById("modal-valor").value = valor;
        document.getElementById("modal-descricao").value = descricao || "";
        document.getElementById("modal-data").value = data;
        document.getElementById("editModal").style.display = "block";
    }

    function closeEditModal() {
        document.getElementById("editModal").style.display = "none";
    }

    function openDeleteModal(id, tipo, nome, valor, descricao) {
        document.getElementById("delete-id").value = id;
        document.getElementById("delete-tipo").value = tipo;

        document.getElementById("delete-info").innerHTML =
            "<strong>" + nome + "</strong><br>" +
            "Valor: R$ " + parseFloat(valor).toFixed(2) + "<br>" +
            (descricao ? "Descrição: " + descricao : "");

        document.getElementById("deleteModal").style.display = "block";
    }

    function closeDeleteModal() {
        document.getElementById("deleteModal").style.display = "none";
    }
</script>