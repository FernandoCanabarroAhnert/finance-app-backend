# Projeto Full Stack: Site para Gerenciamento de Finanças 💰 (back-end)

![Java](https://img.shields.io/badge/java-FF5722.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-003B6F?style=for-the-badge&logo=postgresql&logoColor=white)
![PgAdmin](https://img.shields.io/badge/PgAdmin-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![SpringWebFlux](https://img.shields.io/badge/Spring%20WebFlux-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-F80000?style=for-the-badge&logo=openid&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-6A6A6A?style=for-the-badge&logo=keycloak&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

## O que é o projeto? 🤔

Este repositório contém o backend de uma aplicação full-stack de controle de finanças pessoais, responsável por prover APIs seguras, escaláveis e reativas para o gerenciamento de categorias, carteiras financeiras e transações, além do fornecimento de dados consolidados para a geração de relatórios no dashboard.

A aplicação foi desenvolvida em Java com Spring Boot, utilizando Spring WebFlux para explorar o modelo de programação reativo e não-bloqueante, garantindo melhor desempenho, escalabilidade e uso eficiente de recursos. O backend se comunica com um banco de dados PostgreSQL, provisionado no Azure PostgreSQL Flexible Server, e integra-se ao Keycloak para autenticação e controle de acesso.

## Funcionalides

- Cadastro e Gerenciamento de Categorias: Permite criar, editar e excluir categorias de despesas, organizando as transações de forma eficiente.
- Cadastro e Gerenciamento de Carteiras: Os usuários podem adicionar, editar e excluir carteiras financeiras, facilitando a organização das contas e fontes de receita.
- Cadastro e Gerenciamento de Transações: A aplicação possibilita o registro de transações financeiras, associando-as a categorias e carteiras, com a opção de definir valores, datas e descrições.
- Dashboard de Relatórios: Uma tela interativa com gráficos dinâmicos para exibir (na parte do front-end):
- Relatório de categorias (visualização do total gasto por categoria)
- Relatório de carteiras (comparação entre diferentes carteiras financeiras)
- Relatório de saldo por mês (evolução de receitas e despesas ao longo do tempo)
- Autenticação e Registro com Keycloak: A aplicação utiliza o Keycloak para gerenciar autenticação e controle de acesso, garantindo que apenas usuários autorizados possam acessar e modificar os dados financeiros.
- Deploy e Automação: A aplicação foi containerizada utilizando Docker e Nginx, e o deploy é realizado automaticamente através de uma pipeline CI/CD no GitHub Actions, facilitando atualizações e melhorias contínuas. Traefik é utilizado para gerenciar o tráfego HTTP/HTTPS e gerar certificados SSL.

## Tecnologias 💻
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [PostgreSQL](https://www.postgresql.org/)
- [PgAdmin](https://www.pgadmin.org/)
- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Spring Data + R2DBC](https://docs.spring.io/spring-framework/reference/data-access/r2dbc.html)
- [Docker](https://www.docker.com/)
- [KeyCloak](https://www.keycloak.org/)
- [GithubActions](https://docs.github.com/pt/actions)
- [Bean Validation](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html)

## Como executar 🎉

1.Clonar repositório git:

```text
git clone https://github.com/FernandoCanabarroAhnert/finance-app-backend.git
```

2.Instalar dependências.

```text
mvn clean install
```

3.Executar a aplicação Spring Boot.

### Usando Docker 🐳

- Clonar repositório git
- Construir o projeto:
```
./mvnw clean package
```
- Construir a imagem:
```
./mvnw spring-boot:build-image
```
- Executar o container:
```
docker run --name finance-app-backend -p 8080:8080  -d finance-app-backend:0.0.1-SNAPSHOT
```
