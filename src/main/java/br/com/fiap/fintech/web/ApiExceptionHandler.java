package br.com.fiap.fintech.web;

import br.com.fiap.fintech.exception.CredenciaisInvalidasException;
import br.com.fiap.fintech.exception.EmailJaCadastradoException;
import br.com.fiap.fintech.exception.RecursoNaoEncontradoException;
import br.com.fiap.fintech.exception.RegraNegocioException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Padroniza todos os erros da API como ProblemDetail (RFC 7807). Os erros do próprio Spring MVC
 * (JSON malformado, tipo de parâmetro inválido, 404, 405...) são tratados pela classe base.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Um ou mais campos são inválidos.");
        problema.setTitle("Requisição inválida");
        List<Map<String, String>> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(ApiExceptionHandler::paraErroDeCampo)
                .toList();
        problema.setProperty("erros", erros);
        return handleExceptionInternal(ex, problema, headers, HttpStatus.BAD_REQUEST, request);
    }

    private static Map<String, String> paraErroDeCampo(FieldError erro) {
        String mensagem = erro.getDefaultMessage() == null ? "inválido" : erro.getDefaultMessage();
        return Map.of("campo", erro.getField(), "mensagem", mensagem);
    }

    @ExceptionHandler(RegraNegocioException.class)
    ProblemDetail regraNegocio(RegraNegocioException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Requisição inválida", ex.getMessage());
    }

    @ExceptionHandler(PropertyReferenceException.class)
    ProblemDetail ordenacaoInvalida(PropertyReferenceException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Requisição inválida",
                "Propriedade de ordenação inválida: " + ex.getPropertyName());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    ProblemDetail naoEncontrado(RecursoNaoEncontradoException ex) {
        return problema(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    ResponseEntity<ProblemDetail> credenciaisInvalidas(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(problema(HttpStatus.UNAUTHORIZED, "Não autenticado", ex.getMessage()));
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    ProblemDetail emailJaCadastrado(EmailJaCadastradoException ex) {
        return problema(HttpStatus.CONFLICT, "Conflito", ex.getMessage());
    }

    /** Corrida entre dois cadastros com o mesmo e-mail termina na constraint unique. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail violacaoDeIntegridade(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade: {}", ex.getMostSpecificCause().getMessage());
        return problema(HttpStatus.CONFLICT, "Conflito", "A operação viola uma restrição de dados.");
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail erroInesperado(Exception ex) {
        log.error("Erro inesperado", ex);
        return problema(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    private static ProblemDetail problema(HttpStatus status, String titulo, String detalhe) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setTitle(titulo);
        return problema;
    }
}
