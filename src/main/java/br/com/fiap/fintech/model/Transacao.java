package br.com.fiap.fintech.model;

import java.util.Date;

public class Transacao {
    private int id;
    private int idCliente;
    private String nome;
    private double valor;
    private String descricao;
    private Date data;
    private String tipo;

    public Transacao() {}

    public Transacao(int id, int idCliente, String nome, double valor, String descricao, Date data, String tipo) {
        this.id = id;
        this.idCliente = idCliente;
        this.nome = nome;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}