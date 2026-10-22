package br.com.fiap.fintech.exception;

/** Violação de regra de negócio causada pela entrada do cliente (resposta 400). */
public class RegraNegocioException extends FintechException {

    public RegraNegocioException(String message) {
        super(message);
    }
}
