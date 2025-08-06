package br.com.fiap.fintech.model;

import java.util.Date;

public class Gasto {
    private int idGasto;
    private int idCliente;
    private String nomeDoGasto;
    private double valor;
    private String descricao;
    private Date data;
    private String tipo;

    public Gasto() {}

    public Gasto(int idGasto, int idCliente, String nomeDoGasto, double valor, String descricao, Date data) {
        this.idGasto = idGasto;
        this.idCliente = idCliente;
        this.nomeDoGasto = nomeDoGasto;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }

    public int getIdGasto() { return idGasto; }
    public void setIdGasto(int idGasto) { this.idGasto = idGasto; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNomeDoGasto() { return nomeDoGasto; }
    public void setNomeDoGasto(String nomeDoGasto) { this.nomeDoGasto = nomeDoGasto; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getId() { return idGasto; }
    public String getNome() { return nomeDoGasto; }
}