<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="card equal-height">
    <div class="card-header"><strong>Adicionar Receita / Despesa</strong></div>
    <div class="card-body p-2">
        <!-- Formulário de Despesa -->
        <form method="post" action="${pageContext.request.contextPath}/front-controller">
            <input type="hidden" name="acao" value="processar-transacao">
            <input type="hidden" name="tipo" value="Despesa">

            <div class="mb-2">
                <label><strong>Despesa</strong></label>
                <input type="number" class="form-control" name="valor" placeholder="Valor (R$)" step="0.01" required>
            </div>

            <div class="mb-2">
                <input type="text" class="form-control" name="nomeDoGasto" placeholder="Nome do Gasto" required>
            </div>

            <div class="mb-2">
                <textarea class="form-control" name="descricao" rows="2"
                          placeholder="Descrição adicional (opcional)"></textarea>
            </div>

            <div class="mb-2">
                <label><strong>Data</strong></label>
                <input type="date" class="form-control" name="data" required>
            </div>

            <button class="btn btn-danger w-100 mb-3" type="submit">Adicionar Despesa</button>
        </form>

        <!-- Formulário de Receita -->
        <form method="post" action="${pageContext.request.contextPath}/front-controller">
            <input type="hidden" name="acao" value="processar-transacao">
            <input type="hidden" name="tipo" value="Receita">

            <div class="mb-2">
                <label><strong>Receita</strong></label>
                <input type="number" class="form-control" name="valor" placeholder="Valor (R$)" step="0.01" required>
            </div>

            <div class="mb-2">
                <input type="text" class="form-control" name="nomeDaReceita" placeholder="Nome da Receita" required>
            </div>

            <div class="mb-2">
                <textarea class="form-control" name="descricao" rows="2"
                          placeholder="Descrição adicional (opcional)"></textarea>
            </div>

            <div class="mb-2">
                <label for="dataReceita"><strong>Data</strong></label>
                <input type="date" class="form-control" name="data" id="dataReceita" required>
            </div>

            <button class="btn btn-success w-100" type="submit">Adicionar Receita</button>
        </form>
    </div>
</div>