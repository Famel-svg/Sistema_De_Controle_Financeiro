package br.com.fiap.fintech.exception;

public class FintechException extends RuntimeException {

    public FintechException() {
        super("Erro inesperado na aplicação Fintech.");
    }

    public FintechException(String message) {
        super("[FINTECH ERROR] " + message);
    }

    public FintechException(String message, Throwable cause) {
        super("[FINTECH ERROR] " + message, cause);
    }

    public FintechException(Throwable cause) {
        super("[FINTECH ERROR] Erro interno.", cause);
    }

    public FintechException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("[FINTECH ERROR] " + message, cause, enableSuppression, writableStackTrace);
    }
}
