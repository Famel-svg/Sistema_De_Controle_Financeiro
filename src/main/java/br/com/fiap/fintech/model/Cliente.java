package br.com.fiap.fintech.model;

public class Cliente {
    private String nome;
    private String email;
    private String senha;
    private int id;

    public Cliente() {}

    public Cliente(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}