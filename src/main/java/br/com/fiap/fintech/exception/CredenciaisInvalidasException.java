package br.com.fiap.fintech.exception;

public class CredenciaisInvalidasException extends FintechException {

    public CredenciaisInvalidasException() {
        super("E-mail ou senha inválidos.");
    }
}
