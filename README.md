# Gateway (BFF) - API de Trade

Este serviço implementa o padrão **Backend for Frontend (BFF)**. Ele serve como o ponto de entrada único e seguro para a aplicação cliente (frontend), centralizando o acesso aos microsserviços downstream (API de Propostas e API de Relatórios).

Suas principais responsabilidades são:
1.  Servir como um Gateway de API, expondo endpoints voltados para o cliente na porta `8095`.
2.  Centralizar a segurança, autenticando todas as requisições recebidas via Keycloak (OIDC).
3.  Propagar com segurança o Token JWT do usuário autenticado para as APIs internas.
4.  Abstrair e rotear requisições: os endpoints do cliente (ex: `/api/trade`) são mapeados para os serviços internos correspondentes (ex: `http://localhost:8081/api/proposal`).
5.  Realizar transformação de dados: especificamente, ele busca dados JSON do serviço de Relatório e os transforma em um arquivo CSV para download.

## ✨ Funcionalidades

* **Gateway de API:** Centraliza todas as requisições do frontend em `http://localhost:8095`.
* **Segurança Centralizada:** Protege todos os endpoints expostos ao cliente com Keycloak, aplicando autorização baseada em Roles (ex: `manager`, `user`, `proposal-customer`).
* **Propagação de Token (JWT):** Utiliza o `AccessTokenRequestReactiveFilter` para repassar o token de autenticação do usuário para os serviços internos de Proposta e Relatório.
* **Abstração de Rotas:** Mapeia os endpoints do BFF (ex: `/api/trade`) para os serviços de domínio corretos.
* **Transformação de Dados (JSON para CSV):** Oferece um endpoint (`GET /api/opportunity/report`) que consome dados JSON do serviço de Relatório e os converte em um arquivo `.csv` para download.

## 🚀 Tecnologias Utilizadas

* **Java 17+**
* **Quarkus**
* **Quarkus OIDC:** Para segurança de endpoint e autenticação.
* **Quarkus REST Client Reactive:** Para comunicação síncrona com outros microsserviços.
* **Token Propagation (Reactive):** Para encaminhamento seguro do JWT.
* **Apache Commons CSV:** Para geração do relatório em formato CSV.

## 📋 Pré-requisitos

* JDK 17 ou superior
* Maven 3.8+
* Docker
* Uma instância do **Keycloak** rodando (em `http://localhost:8180`).
* A **API de Propostas** deve estar rodando (configurada para `http://localhost:8081`, conforme `application.properties`).
* A **API de Relatórios** deve estar rodando (configurada para `http://localhost:8091`).

## ⚙️ Como Executar

1.  **Inicie todas as dependências:**
    Certifique-se de que o Keycloak, as bases de dados (PostgreSQL), o Kafka, a API de Propostas (na porta 8081) e a API de Relatórios (na porta 8091) estejam todos em execução.

2.  **Execute a aplicação BFF (Gateway):**

    Abra um terminal na raiz deste projeto e execute:
    ```bash
    ./mvnw quarkus:dev
    ```
    A aplicação estará disponível em `http://localhost:8095`.

## 📡 Endpoints da API (Expostos ao Cliente)

Todos os endpoints são servidos em `http://localhost:8095` e requerem um Token JWT (Bearer Token) válido.

### API de Trade (Propostas)
URL Base: `http://localhost:8095/api/trade`

| Método | Endpoint | Descrição | Role Requerida |
| :--- | :--- | :--- | :--- |
| `POST` | `/` | Cria uma nova proposta (roteia para o serviço de Propostas). | `proposal-customer` |
| `GET` | `/{id}` | Busca detalhes de uma proposta por ID. | `user` ou `manager` |
| `DELETE` | `/remove/{id}` | Remove uma proposta por ID. | `manager` |

### API de Oportunidades (Relatórios)
URL Base: `http://localhost:8095/api/opportunity`

| Método | Endpoint | Descrição | Role Requerida |
| :--- | :--- | :--- | :--- |
| `GET` | `/data` | Retorna os dados de oportunidades em formato JSON. | `user` ou `manager` |
| `GET` | `/report` | Gera e força o download de um relatório de oportunidades em formato **CSV**. | `user` ou `manager` |