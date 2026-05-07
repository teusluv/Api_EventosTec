# 🚀 API EventosTec

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Managed-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Supabase](https://img.shields.io/badge/Supabase-Cloud_Storage-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)](https://supabase.com/)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)](https://spring.io/projects/spring-security)

## 📌 Sobre o Projeto

A **API EventosTec** é uma solução backend robusta e escalável desenvolvida para a gestão completa de eventos de tecnologia. O sistema foi concebido para centralizar desde o registo de utilizadores até à organização logística de eventos, incluindo o armazenamento de média em nuvem e controlo rigoroso de acessos.

Esta API foi construída seguindo as melhores práticas de desenvolvimento, como **Clean Code**, **S.O.L.I.D.** e **Arquitetura em Camadas**, garantindo uma manutenção facilitada e alta performance.

---

## ✨ Funcionalidades Principais

### 🔒 Segurança e Autenticação
* **Autenticação Stateless com JWT:** Implementação de tokens JSON Web Token para gestão de sessões seguras.
* **Controlo de Acesso (RBAC):** Diferenciação de permissões entre `ROLE_ADMIN` (gestores) e `ROLE_USER` (participantes).
* **Criptografia:** Utilização de BCrypt para o hashing de passwords antes da persistência no banco de dados.

### 📅 Gestão de Eventos
* **CRUD de Eventos:** Criação, leitura, atualização e remoção de eventos tecnológicos.
* **Upload de Banners:** Integração direta com o **Supabase Storage** para upload e armazenamento de imagens promocionais dos eventos.
* **Gestão de Lotação:** Controlo automático de vagas disponíveis por evento.

### 🎟 Inscrições e Participação
* **Fluxo de Inscrição:** Registo de utilizadores em eventos com validação de duplicidade e disponibilidade.
* **Consulta de Perfil:** Endpoints para os utilizadores consultarem os eventos em que estão inscritos.

---
