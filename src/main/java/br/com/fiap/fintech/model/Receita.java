package br.com.fiap.fintech.model;

import java.util.Date;

public class Receita {
    private int idReceita;
    private int idCliente;
    private String nomeDaReceita;
    private double valor;
    private String descricao;
    private Date data;
    private String tipo;

    public Receita() {}

    public Receita(int idReceita, int idCliente, String nomeDaReceita, double valor, String descricao, Date data) {
        this.idReceita = idReceita;
        this.idCliente = idCliente;
        this.nomeDaReceita = nomeDaReceita;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }

    public int getIdReceita() { return idReceita; }
    public void setIdReceita(int idReceita) { this.idReceita = idReceita; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNomeDaReceita() { return nomeDaReceita; }
    public void setNomeDaReceita(String nomeDaReceita) { this.nomeDaReceita = nomeDaReceita; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getId() { return idReceita; }
    public String getNome() { return nomeDaReceita; }
}