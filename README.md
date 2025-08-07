# Sistema_De_Controle_Financeiro(JAVA)
Aplicação web para gestão financeira pessoal com autenticação de usuários, CRUD de receitas/despesas e dashboard interativo. Tecnologias: Backend: Java Servlets + MVC  Frontend: JSP/Bootstrap + Chart.js 

<h1 align="center">Sistema de Controle Financeiro 💸</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=jsp&logoColor=white"/>
  <img src="https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white"/>
  <img src="https://img.shields.io/badge/Chart.js-FF6384?style=for-the-badge&logo=chartdotjs&logoColor=white"/>
</p>

<p align="center">
  Aplicação web para gestão financeira pessoal com autenticação de usuários, CRUD de receitas/despesas e dashboard interativo.
</p>

<p align="center">
 <a href="#-sobre-o-projeto">Sobre</a> •
 <a href="#-funcionalidades">Funcionalidades</a> •
 <a href="#-tecnologias">Tecnologias</a> •
 <a href="#-como-usar">Como usar</a> •
 <a href="#-contribuindo">Contribuindo</a> •
 <a href="#-padronização-de-commits">Padronização de Commits</a> •
 <a href="#-licença">Licença</a>
</p>

---

## ✨ Sobre o projeto

O **Sistema de Controle Financeiro** nasceu da necessidade de organizar minhas finanças pessoais, então decidi unir o útil ao agradável e aplicar meus conhecimentos em Java e desenvolvimento web para criar essa solução.  
Aqui, você pode cadastrar receitas, despesas, visualizar gráficos dinâmicos e acompanhar sua saúde financeira de forma simples e intuitiva.

---

## 🚀 Funcionalidades

- Cadastro e autenticação de usuários
- CRUD de receitas e despesas
- Dashboard com gráficos interativos (Chart.js)
- Relatórios financeiros
- Interface responsiva (JSP + Bootstrap)
- Filtros por período, categoria, tipo de transação
- Segurança com sessões

---

## 🛠 Tecnologias

- **Backend:** Java Servlets + MVC
- **Frontend:** JSP, Bootstrap
- **Gráficos:** Chart.js
- **Banco de Dados:** (Preencha conforme utilizado: MySQL, PostgreSQL, etc.)
- **Gerenciamento de dependências:** Maven/Gradle

---

## 🏁 Como usar

1. Clone o repositório:
   ```bash
   git clone https://github.com/Famel-svg/Sistema_De_Controle_Financeiro.git
   ```

2. Importe o projeto em sua IDE Java favorita (Eclipse, IntelliJ, VS Code).

3. Configure o banco de dados:
   - Crie um banco de dados e ajuste as credenciais em `/src/main/resources/db.properties` (ou arquivo equivalente).

4. Compile e execute o projeto em um servidor compatível com Java Servlets (ex: Apache Tomcat).

5. Acesse `http://localhost:8080/Sistema_De_Controle_Financeiro` no navegador.

---

## 🤝 Contribuindo

Contribuições são super bem-vindas!  
Siga os passos abaixo para contribuir:

1. Fork este repositório
2. Crie sua branch: `git checkout -b minha-feature`
3. Faça suas alterações e commit: `git commit -m 'feat: minha nova feature'`
4. Push na branch: `git push origin minha-feature`
5. Abra um Pull Request

---

## 📓 Padronização de Commits

Este projeto segue o padrão de commits baseado no [@iuricode/padroes-de-commits](https://github.com/iuricode/padroes-de-commits).

Exemplos de prefixos:

| Tipo     | Descrição                           |
|----------|-------------------------------------|
| feat     | Nova feature                        |
| fix      | Correção de bug                     |
| docs     | Documentação                        |
| style    | Formatação, sem alteração de código |
| refactor | Refatoração de código               |
| test     | Adicionando testes                  |
| chore    | Manutenção                          |

Exemplo de commit:
```
feat: adicionar filtro por categoria na listagem de despesas
```

---

## 📄 Licença

Este projeto está sob a licença MIT.  
Veja em [LICENSE](./LICENSE) para mais detalhes.

---

<p align="center">
  Feito com 💙 por <a href="https://github.com/Famel-svg">Famel-svg</a>
</p>
