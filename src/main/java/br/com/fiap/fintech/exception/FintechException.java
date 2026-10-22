package br.com.fiap.fintech.exception;

/**
 * Base das exceções de negócio da aplicação. Cada subclasse vira uma resposta
 * ProblemDetail (RFC 7807) no ApiExceptionHandler; a mensagem vai para o campo "detail".
 */
public abstract class FintechException extends RuntimeException {

    protected FintechException(String message) {
        super(message);
    }
}
