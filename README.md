src/main/java/com/teusluv/eventostec
├── config/           # Configurações globais (CORS, Swagger, Beans)
├── controllers/      # Controladores REST (Entrypoints da API)
├── domain/           # Entidades JPA e Modelos de domínio
├── dtos/             # Data Transfer Objects (Isolamento da camada de persistência)
├── exceptions/       # Handlers globais de erros
├── repositories/     # Interfaces de acesso a dados (Spring Data JPA)
├── security/         # Lógica de Filtros JWT, Provider de Autenticação e Rotas
└── services/         # Regras de negócio, cálculos e integração externa (Supabase)
```markdown
# 🚀 API EventosTec - Gestão Avançada de Eventos de Tecnologia

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Managed-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Supabase](https://img.shields.io/badge/Supabase-Cloud_Storage-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)](https://supabase.com/)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-Auth-black?style=for-the-badge&logo=json-web-tokens&logoColor=white)](https://jwt.io/)

## 📌 Visão Geral

A **API EventosTec** é uma robusta solução backend RESTful desenvolvida para orquestrar o ciclo de vida completo de eventos de tecnologia. Projetada para suportar desde pequenos meetups até grandes conferências, a plataforma automatiza o cadastro de usuários, o controle rigoroso de acessos, a gestão de vagas em tempo real e o armazenamento em nuvem de mídias promocionais.

O sistema foi construído aplicando princípios de **Clean Code**, **S.O.L.I.D.** e **Arquitetura em Camadas**, garantindo integração fluida com aplicações frontend modernas.

---

## ✨ Funcionalidades Principais

### 👤 Gestão de Usuários & Acesso
*   **Role-Based Access Control (RBAC):** Diferenciação clara entre `ROLE_ADMIN` (organizadores com permissão total) e `ROLE_USER` (participantes).
*   **Autenticação Stateless:** Implementação de tokens JWT para sessões seguras e escaláveis.
*   **Self-Registration:** Fluxo simplificado e protegido para novos participantes criarem suas contas.

### 📅 Gestão de Eventos
*   **CRUD de Eventos:** Organizadores podem criar, editar e excluir eventos.
*   **Upload de Banners:** Integração direta com os buckets do **Supabase Storage** para processamento de imagens, retornando URLs públicas otimizadas.
*   **Filtros de Busca:** Listagem inteligente de eventos ativos com paginação para otimização de performance.

### 🎟 Sistema de Inscrições
*   **Controle de Lotação:** Validação automática da disponibilidade de vagas no momento exato da inscrição.
*   **Prevenção de Duplicidade:** Impedimento de inscrições múltiplas no mesmo evento pelo mesmo usuário.
*   **Histórico de Participação:** Endpoint dedicado para consulta de eventos confirmados.

---

## ⚙️ Arquitetura e Segurança

*   **Padrão MVC:** Separação estrita entre `Controllers`, `Services` e `Repositories`.
*   **Filtros de Segurança:** Interceptação via Spring Security e validação JWT; senhas com hash BCrypt.
*   **Prevenção N+1:** Otimização de consultas com Spring Data JPA.
*   **Global Exception Handling:** `@ControllerAdvice` para respostas HTTP padronizadas (400, 401, 403, 404, 500).
*   **DTOs (Data Transfer Objects):** Abstração de dados e validação com Hibernate Validator.

---

## 🛠 Tecnologias e Ferramentas

*   **Backend:** Java 17+, Spring Boot 3.x
*   **Segurança:** Spring Security, JWT (JSON Web Tokens)
*   **Persistência de Dados:** Spring Data JPA, Hibernate
*   **Bancos de Dados:** PostgreSQL
*   **Cloud Storage:** Supabase
*   **Build Tool:** Maven

---

## 🚀 Guia de Instalação e Execução

### Pré-requisitos
*   JDK 17 ou superior
*   Maven 3.8+
*   PostgreSQL rodando localmente (ou via Docker)
*   Conta no Supabase (com um bucket público criado, ex: `eventos-banners`)

### Passo a Passo

1.  **Clonar o Repositório**
    ```bash
    git clone https://github.com/teusluv/Api_EventosTec.git
    cd Api_EventosTec
    ```

2.  **Configurar Variáveis de Ambiente**
    Configure o arquivo `src/main/resources/application.properties` com as suas credenciais:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/eventostec_db
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    
    api.security.token.secret=SUA_CHAVE_SECRETA_JWT
    
    supabase.url=https://seu-projeto.supabase.co
    supabase.key=sua-anon-key-ou-service-role
    supabase.bucket.name=eventos-banners
    ```

3.  **Compilar e Executar**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

---

## 📡 Endpoints da API

### 🔐 Autenticação
*   **`POST /api/auth/register`**: Registra um novo usuário (Acesso Público).
*   **`POST /api/auth/login`**: Autentica usuário e retorna o token JWT (Acesso Público).

### 📅 Eventos
*   **`POST /api/eventos`**: Cria um novo evento, requer Multipart Form-Data para imagem (Acesso `ADMIN`).
*   **`GET /api/eventos`**: Lista paginada de eventos ativos (Acesso Público / `USER`).
*   **`GET /api/eventos/{id}`**: Detalhes de um evento específico (Acesso Público / `USER`).

### 🎟 Inscrições
*   **`POST /api/eventos/{id}/inscricao`**: Realiza a inscrição do usuário autenticado no evento especificado (Acesso `USER`).

---

## 📂 Estrutura de Diretórios

```text
src/main/java/com/teusluv/eventostec
├── config/           # Configurações globais (CORS, Swagger, Beans)
├── controllers/      # Controladores REST (Entrypoints da API)
├── domain/           # Entidades JPA e Modelos de domínio
├── dtos/             # Data Transfer Objects (Isolamento da camada de persistência)
├── exceptions/       # Handlers globais de erros
├── repositories/     # Interfaces de acesso a dados (Spring Data JPA)
├── security/         # Lógica de Filtros JWT, Provider de Autenticação e Rotas
└── services/         # Regras de negócio, cálculos e integração externa (Supabase)

