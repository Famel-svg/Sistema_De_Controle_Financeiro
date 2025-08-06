<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-4">
    <div class="card">
        <div class="card-header bg-primary text-white">
            Gráfico de Receitas e Gastos
        </div>
        <div class="card-body">
            <canvas id="graficoReceitasGastos" width="100%" height="40"></canvas>
        </div>
    </div>
</div>

<script>
    const ctx = document.getElementById('graficoReceitasGastos').getContext('2d');
    const receitas = ${totalReceitas != null ? totalReceitas : 0};
    const gastos = ${totalGastos != null ? totalGastos : 0};

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Receitas', 'Gastos'],
            datasets: [{
                label: 'Valores (R$)',
                data: [receitas, gastos],
                backgroundColor: [
                    'rgba(40, 167, 69, 0.7)',  // verde (receita)
                    'rgba(220, 53, 69, 0.7)'   // vermelho (gasto)
                ],
                borderColor: [
                    'rgba(40, 167, 69, 1)',
                    'rgba(220, 53, 69, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return 'R$ ' + value.toFixed(2).replace('.', ',');
                        }
                    }
                }
            },
            plugins: {
                legend: { display: false }
            }
        }
    });
</script>