package br.com.fiap.fintech.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;
import org.junit.jupiter.api.Test;

class GastoReceitaTest {

    @Test
    void gastoGuardaValoresDoConstrutor() {
        Date data = new Date(0L);
        Gasto gasto = new Gasto(7, 3, "Mercado", 150.5, "Compras da semana", data);

        assertEquals(7, gasto.getIdGasto());
        assertEquals(7, gasto.getId());
        assertEquals(3, gasto.getIdCliente());
        assertEquals("Mercado", gasto.getNomeDoGasto());
        assertEquals("Mercado", gasto.getNome());
        assertEquals(150.5, gasto.getValor());
        assertEquals("Compras da semana", gasto.getDescricao());
        assertEquals(data, gasto.getData());
        assertNull(gasto.getTipo());
    }

    @Test
    void receitaGuardaValoresDoConstrutor() {
        Date data = new Date(0L);
        Receita receita = new Receita(9, 3, "Salário", 5000.0, "Mensal", data);

        assertEquals(9, receita.getIdReceita());
        assertEquals(9, receita.getId());
        assertEquals("Salário", receita.getNomeDaReceita());
        assertEquals("Salário", receita.getNome());
        assertEquals(5000.0, receita.getValor());
        assertEquals(data, receita.getData());
    }

    @Test
    void transacaoAceitaTipoPorSetter() {
        Transacao transacao = new Transacao();
        transacao.setTipo("GASTO");
        transacao.setValor(42.0);

        assertEquals("GASTO", transacao.getTipo());
        assertEquals(42.0, transacao.getValor());
    }
}
