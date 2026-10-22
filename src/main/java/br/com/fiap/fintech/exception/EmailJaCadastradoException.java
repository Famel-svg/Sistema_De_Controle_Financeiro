package br.com.fiap.fintech.exception;

public class EmailJaCadastradoException extends FintechException {

    public EmailJaCadastradoException() {
        super("Já existe um cliente cadastrado com este e-mail.");
    }
}
