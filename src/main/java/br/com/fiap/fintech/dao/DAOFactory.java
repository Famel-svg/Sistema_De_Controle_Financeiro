package br.com.fiap.fintech.dao;

public class DAOFactory {
    private DAOFactory() {}

    public static ClienteDAO getClienteDAO() { return new ClienteDAO(); }
    public static GastoDAO getGastoDAO() { return new GastoDAO(); }
    public static ReceitaDAO getReceitaDAO() { return new ReceitaDAO(); }
}