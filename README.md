# 🚀 API EventosTec

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Managed-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Supabase](https://img.shields.io/badge/Supabase-Cloud_Storage-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)](https://supabase.com/)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)](https://spring.io/projects/spring-security)

## 📌 Visão Geral

A **API EventosTec** é uma robusta solução backend RESTful desenvolvida para orquestrar o ciclo de vida completo de eventos de tecnologia. Projetada para suportar desde pequenos meetups até grandes conferências, a plataforma automatiza o cadastro de usuários, o controle rigoroso de acessos, a gestão de vagas em tempo real e o armazenamento em nuvem de mídias promocionais.

O sistema foi construído aplicando princípios de **Clean Code**, **S.O.L.I.D.** e **Arquitetura em Camadas**, refletindo mais de 2 anos de experiência prática em engenharia de software e garantindo integração fluida com aplicações frontend modernas.

---

## ✨ Funcionalidades Principais (Regras de Negócio)

### 👤 Gestão de Usuários & Acesso
*   **Role-Based Access Control (RBAC):** Diferenciação clara entre `ROLE_ADMIN` (organizadores com permissão total) e `ROLE_USER` (participantes).
*   **Autenticação Stateless:** Implementação de tokens JWT (JSON Web Tokens) para sessões seguras e escaláveis.
*   **Self-Registration:** Fluxo simplificado e protegido para novos participantes criarem suas contas.

### 📅 Gestão de Eventos
*   **CRUD de Eventos:** Organizadores podem criar, editar e excluir eventos, detalhando data, local, descrição e limite de vagas.
*   **Upload de Banners:** Integração direta com os buckets do **Supabase Storage** para processamento de imagens `multipart/form-data`, retornando URLs públicas otimizadas persistidas no banco.
*   **Filtros de Busca:** Listagem inteligente de eventos ativos com paginação para otimização de performance.

### 🎟 Sistema de Inscrições
*   **Controle de Lotação:** Validação automática da disponibilidade de vagas no momento exato da inscrição.
*   **Prevenção de Duplicidade:** Lógica de negócio dedicada a impedir que um usuário se inscreva múltiplas vezes no mesmo evento.
*   **Histórico de Participação:** Endpoint para o usuário consultar seus eventos confirmados.

---

## ⚙️ Arquitetura e Segurança

*   **Padrão MVC:** Separação estrita entre `Controllers` (exposição da API), `Services` (regras de negócio) e `Repositories` (persistência).
*   **Filtros de Segurança:** Interceptação de requisições via Spring Security para validar a integridade do JWT. Senhas são transformadas em hash com salt utilizando `BCryptPasswordEncoder`.
*   **Prevenção de Problemas N+1:** Otimização de consultas utilizando o Spring Data JPA.
*   **Global Exception Handling:** Uso de `@ControllerAdvice` para tratar erros de forma elegante e retornar códigos HTTP padronizados (400, 401, 403, 404, 500).
*   **DTOs (Data Transfer Objects):** Camada de abstração garantindo que dados sensíveis não sejam expostos e validando entradas com `Hibernate Validator`.

---

## 🛠 Tecnologias e Ferramentas

*   **Backend:** Java 17+, Spring Boot 3.x
*   **Segurança:** Spring Security, JWT (JSON Web Tokens)
*   **Persistência de Dados:** Spring Data JPA, Hibernate
*   **Bancos de Dados:** PostgreSQL (suporte configurável para MySQL)
*   **Cloud Storage:** Supabase
*   **Gerenciamento de Dependências:** Maven

---

## 🚀 Guia de Instalação e Execução

### Pré-requisitos
*   JDK 17 ou superior
*   Maven 3.8+
*   PostgreSQL rodando localmente (ou via Docker)
*   Conta no Supabase (com um bucket público criado, ex: `eventos-banners`)

### 1. Clonar o Repositório
```bash
git clone https://github.com/teusluv/Api_EventosTec.git
cd Api_EventosTec

