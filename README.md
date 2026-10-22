# Sistema de Controle Financeiro

API REST para controle de finanças pessoais: cadastro de clientes, receitas e gastos com
filtro por período e paginação, e resumo de saldo (total de receitas menos total de gastos).

**Stack:** Java 21, Spring Boot 4.0.6 (Web MVC, Data JPA, Validation, Security), Flyway,
PostgreSQL. Testes com JUnit 5 e H2 em modo PostgreSQL.

## Como subir

Pré-requisitos: JDK 21, Maven 3.9+ (ou o `./mvnw` do repositório) e um PostgreSQL acessível.

A conexão vem **somente de variáveis de ambiente**; não há valores padrão no código:

| Variável      | Exemplo de formato                               |
|---------------|--------------------------------------------------|
| `DB_URL`      | `jdbc:postgresql://localhost:5432/fintech`       |
| `DB_USERNAME` | usuário do banco                                 |
| `DB_PASSWORD` | senha do banco (defina no ambiente, nunca no git)|

```sh
export DB_URL=jdbc:postgresql://localhost:5432/fintech
export DB_USERNAME=<usuario>
export DB_PASSWORD=<senha>
mvn spring-boot:run
# ou
mvn -B package && java -jar target/fintechh1_0-1.0-SNAPSHOT.jar
```

Na subida o Flyway aplica `src/main/resources/db/migration/V1__schema.sql` e o Hibernate apenas
valida o esquema (`ddl-auto=validate`). A API escuta em `http://localhost:8080`.

## Como rodar os testes

```sh
mvn -B test
```

Os testes não precisam de banco externo nem de Docker: usam H2 em memória em modo PostgreSQL
(perfil `test`, `src/test/resources/application-test.yml`), com o esquema criado pela mesma
migration Flyway de produção.

## Autenticação

- `POST /api/clientes` (público) cadastra o cliente. A senha é gravada apenas como hash BCrypt.
- `POST /api/auth/login` (público) recebe `{"email","senha"}` e responde **200** com os dados do
  cliente ou **401**. Não emite token nesta versão; serve para validar credenciais.
- Todas as demais rotas exigem **HTTP Basic** (`Authorization: Basic base64(email:senha)`) em
  cada requisição. A API é stateless: não cria sessão nem cookie, por isso o CSRF está desligado.
  Use sempre HTTPS fora do ambiente local, já que o Basic transmite a senha em Base64.

Sem credencial válida a resposta é `401` com `WWW-Authenticate: Basic` e corpo ProblemDetail.

## Endpoints principais

| Método | Rota                     | Descrição                                         | Sucesso |
|--------|--------------------------|---------------------------------------------------|---------|
| POST   | `/api/clientes`          | Cadastrar cliente (`nome`, `email`, `senha`)      | 201     |
| GET    | `/api/clientes/me`       | Dados do cliente autenticado                      | 200     |
| POST   | `/api/auth/login`        | Validar e-mail e senha                            | 200     |
| GET    | `/api/receitas`          | Listar receitas (filtro de período + paginação)   | 200     |
| GET    | `/api/receitas/{id}`     | Buscar receita                                    | 200     |
| POST   | `/api/receitas`          | Criar receita                                     | 201     |
| PUT    | `/api/receitas/{id}`     | Atualizar receita                                 | 200     |
| DELETE | `/api/receitas/{id}`     | Excluir receita                                   | 204     |
| *      | `/api/gastos[/{id}]`     | Mesmas operações para gastos                      |         |
| GET    | `/api/resumo`            | Total de receitas, total de gastos e saldo        | 200     |

Corpo de criação/atualização de receita ou gasto:

```json
{ "nome": "Salário", "descricao": "Pagamento mensal", "valor": 5000.00, "data": "2026-01-05" }
```

Regras: `nome` e `descricao` obrigatórios; `valor` maior que zero, com no máximo 2 casas
decimais; `data` no formato `yyyy-MM-dd`.

Parâmetros de listagem e resumo (todos opcionais):

- `dataInicio`, `dataFim` (`yyyy-MM-dd`, inclusivos). Sem eles o período é aberto; `dataInicio`
  depois de `dataFim` gera 400.
- `page` (a partir de 0), `size` (padrão 20, máximo 100), `sort` (padrão `data,desc`).

```sh
curl -u "$EMAIL:$SENHA" "http://localhost:8080/api/gastos?dataInicio=2026-01-01&dataFim=2026-01-31&page=0&size=20"
curl -u "$EMAIL:$SENHA" "http://localhost:8080/api/resumo?dataInicio=2026-01-01&dataFim=2026-01-31"
```

Cada cliente só enxerga os próprios lançamentos: um id de outro cliente responde 404.

### Erros

Todos os erros seguem ProblemDetail (RFC 7807), `Content-Type: application/problem+json`.
Erros de validação trazem a lista `erros` com `campo` e `mensagem`:

```json
{
  "type": "about:blank",
  "title": "Requisição inválida",
  "status": 400,
  "detail": "Um ou mais campos são inválidos.",
  "erros": [ { "campo": "valor", "mensagem": "deve ser maior que zero" } ]
}
```

Códigos usados: 400 (validação, JSON malformado, período ou ordenação inválidos), 401 (sem
credencial ou credencial inválida), 404 (recurso inexistente ou de outro cliente), 409 (e-mail já
cadastrado), 500 (erro inesperado, sem detalhes internos na resposta).

## Estrutura

```
br.com.fiap.fintech
├── domain       entidades JPA: Cliente, Lancamento, TipoLancamento
├── repository   Spring Data JPA
├── service      regras de negócio (cadastro/login, lançamentos, saldo, período)
├── security     HTTP Basic, UserDetailsService, BCrypt, 401 em ProblemDetail
├── web          controllers REST, DTOs e ApiExceptionHandler
└── exception    exceções de negócio (base FintechException)
```

Camadas: controller → service → repository. Controllers só convertem HTTP em chamadas ao service.

## Migração

O que mudou em relação à versão com servlets/JSP e JDBC:

- **Aplicação:** de WAR com servlets e JSP (para Tomcat externo) para uma API REST JSON em
  Spring Boot com servidor embutido, empacotada como JAR executável. As telas JSP foram
  removidas; um front-end passa a consumir a API.
- **Rotas:** `/login` → `POST /api/auth/login`; `/cliente` → `POST /api/clientes`;
  `/receita`, `/gasto` e `/front-controller` → `POST /api/receitas` e `POST /api/gastos`;
  `/transacao` (editar/excluir) → `PUT`/`DELETE /api/{receitas|gastos}/{id}`;
  `/dashboard` e `/carregar-metricas` → `GET /api/{receitas|gastos}` e `GET /api/resumo`.
- **Banco:** de JDBC manual (`ConnectionFactory`, DAOs, tabelas `T_CLIENTE`, `T_RECEITA`,
  `T_GASTOS`) para PostgreSQL com JPA/Spring Data. O esquema agora é versionado por Flyway.
  Dados do banco anterior não são migrados automaticamente.
- **Modelo:** `T_RECEITA` e `T_GASTOS` tinham as mesmas colunas e o código já as unificava em
  `Transacao`; agora há uma única tabela `lancamento` com a coluna `tipo` (`RECEITA`/`GASTO`).
  A API continua expondo receitas e gastos em rotas separadas. `valor` passou de `double` para
  `NUMERIC(15,2)`/`BigDecimal` (sem erro de arredondamento) e `data` para `LocalDate`.
  `descricao` passou a ser obrigatória.
- **Segurança:** as senhas eram gravadas e comparadas em texto puro; agora são hash BCrypt via
  `PasswordEncoder`. Senhas do banco anterior não funcionam como hash: os clientes precisam se
  recadastrar (ou ter a senha redefinida) no novo banco. A sessão HTTP (`idCliente` na sessão)
  foi substituída por HTTP Basic stateless, e toda operação é restrita ao cliente autenticado
  (antes, editar/excluir por id não verificava o dono).
- **Erros:** em vez de redirecionar para `erro.jsp`/`login.jsp` e engolir exceções nos DAOs, a API
  responde com status HTTP corretos e corpo ProblemDetail.
- **Build:** Java 21 e `spring-boot-starter-parent`; saíram o driver JDBC anterior, Weld, JSTL e
  `servlet-api`.

O código antigo continua disponível no histórico do git.

## Licença

Este projeto está sob a licença MIT.
