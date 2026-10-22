package br.com.fiap.fintech.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.fiap.fintech.repository.ClienteRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

    private static final String SENHA = "senha-de-teste-123";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClienteRepository clienteRepository;

    private String email;

    @BeforeEach
    void cadastrarCliente() throws Exception {
        email = novoEmail();
        cadastrar(email);
    }

    @Test
    void semCredencialRetorna401ComProblemDetail() throws Exception {
        mvc.perform(get("/api/receitas"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", startsWith("Basic")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(401));
        mvc.perform(get("/api/resumo")).andExpect(status().isUnauthorized());
    }

    @Test
    void senhaErradaNoBasicRetorna401() throws Exception {
        mvc.perform(get("/api/gastos").with(httpBasic(email, "outra-senha")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cadastroGuardaSomenteHash() throws Exception {
        String hash = clienteRepository.findByEmail(email).orElseThrow().getSenhaHash();
        assertThat(hash).startsWith("$2").isNotEqualTo(SENHA);
    }

    @Test
    void cadastroDuplicadoRetorna409() throws Exception {
        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson(email.toUpperCase())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void loginRetorna200ComCredencialCorretaE401CasoContrario() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson(email, SENHA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(jsonPath("$.senhaHash").doesNotExist());

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson(email, "senha-errada")))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson(novoEmail(), SENHA)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void crudDeReceitaComCredencial() throws Exception {
        String location = mvc.perform(post("/api/receitas").with(auth()).contentType(MediaType.APPLICATION_JSON)
                        .content(lancamentoJson("Salário", "Mensal", "5000.00", "2026-01-05")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/receitas/")))
                .andExpect(jsonPath("$.tipo").value("RECEITA"))
                .andExpect(jsonPath("$.valor").value(5000.00))
                .andExpect(jsonPath("$.data").value("2026-01-05"))
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(get(location).with(auth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Salário"));

        mvc.perform(put(location).with(auth()).contentType(MediaType.APPLICATION_JSON)
                        .content(lancamentoJson("Salário", "Mensal com bônus", "5500.00", "2026-01-05")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(5500.00))
                .andExpect(jsonPath("$.descricao").value("Mensal com bônus"));

        // Uma receita não é acessível pela rota de gastos.
        String id = location.substring(location.lastIndexOf('/') + 1);
        mvc.perform(get("/api/gastos/" + id).with(auth())).andExpect(status().isNotFound());

        mvc.perform(delete(location).with(auth())).andExpect(status().isNoContent());
        mvc.perform(get(location).with(auth()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void listagemFiltraPorPeriodoEPagina() throws Exception {
        criarGasto("10.00", "2026-01-01");
        criarGasto("20.00", "2026-01-15");
        criarGasto("30.00", "2026-01-31");
        criarGasto("40.00", "2026-02-01");

        mvc.perform(get("/api/gastos").with(auth())
                        .param("dataInicio", "2026-01-01").param("dataFim", "2026-01-31")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].data").value("2026-01-31"))
                .andExpect(jsonPath("$.page.totalElements").value(3))
                .andExpect(jsonPath("$.page.totalPages").value(2));
    }

    @Test
    void resumoCalculaSaldoDoPeriodo() throws Exception {
        mvc.perform(post("/api/receitas").with(auth()).contentType(MediaType.APPLICATION_JSON)
                        .content(lancamentoJson("Salário", "Mensal", "3000.00", "2026-03-05")))
                .andExpect(status().isCreated());
        criarGasto("1200.50", "2026-03-10");
        criarGasto("99.50", "2026-04-01");

        mvc.perform(get("/api/resumo").with(auth()).param("dataInicio", "2026-03-01").param("dataFim", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReceitas").value(3000.00))
                .andExpect(jsonPath("$.totalGastos").value(1200.50))
                .andExpect(jsonPath("$.saldo").value(1799.50));
    }

    @Test
    void lancamentoDeOutroClienteRetorna404() throws Exception {
        String location = mvc.perform(post("/api/gastos").with(auth()).contentType(MediaType.APPLICATION_JSON)
                        .content(lancamentoJson("Aluguel", "Março", "1500.00", "2026-03-01")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String outro = novoEmail();
        cadastrar(outro);
        mvc.perform(get(location).with(httpBasic(outro, SENHA))).andExpect(status().isNotFound());
        mvc.perform(delete(location).with(httpBasic(outro, SENHA))).andExpect(status().isNotFound());
        mvc.perform(get(location).with(auth())).andExpect(status().isOk());
    }

    @Test
    void payloadInvalidoRetorna400ComProblemDetail() throws Exception {
        mvc.perform(post("/api/gastos").with(auth()).contentType(MediaType.APPLICATION_JSON)
                        .content(lancamentoJson("Mercado", "", "-5.00", "2026-01-10")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Requisição inválida"))
                .andExpect(jsonPath("$.erros[*].campo", hasItem("valor")))
                .andExpect(jsonPath("$.erros[*].campo", hasItem("descricao")));
    }

    @Test
    void jsonMalformadoEPeriodoInvertidoRetornam400() throws Exception {
        mvc.perform(post("/api/receitas").with(auth()).contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));

        mvc.perform(get("/api/resumo").with(auth()).param("dataInicio", "2026-02-01").param("dataFim", "2026-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("dataInicio deve ser anterior ou igual a dataFim."));

        mvc.perform(get("/api/receitas").with(auth()).param("dataInicio", "ontem"))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/receitas").with(auth()).param("sort", "inexistente"))
                .andExpect(status().isBadRequest());
    }

    private void criarGasto(String valor, String data) throws Exception {
        mvc.perform(post("/api/gastos").with(auth()).contentType(MediaType.APPLICATION_JSON)
                        .content(lancamentoJson("Gasto", "Teste", valor, data)))
                .andExpect(status().isCreated());
    }

    private void cadastrar(String emailCliente) throws Exception {
        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(clienteJson(emailCliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(emailCliente))
                .andExpect(jsonPath("$.senhaHash").doesNotExist());
    }

    private RequestPostProcessor auth() {
        return httpBasic(email, SENHA);
    }

    private static String novoEmail() {
        return "cliente-" + UUID.randomUUID() + "@example.com";
    }

    private static String clienteJson(String email) {
        return """
                {"nome":"Cliente Teste","email":"%s","senha":"%s"}""".formatted(email, SENHA);
    }

    private static String loginJson(String email, String senha) {
        return """
                {"email":"%s","senha":"%s"}""".formatted(email, senha);
    }

    private static String lancamentoJson(String nome, String descricao, String valor, String data) {
        return """
                {"nome":"%s","descricao":"%s","valor":%s,"data":"%s"}""".formatted(nome, descricao, valor, data);
    }
}
