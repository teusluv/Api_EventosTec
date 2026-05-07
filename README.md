{
  "project": {
    "name": "API EventosTec",
    "subtitle": "Gestão Avançada de Eventos de Tecnologia",
    "overview": "A API EventosTec é uma robusta solução backend RESTful desenvolvida para orquestrar o ciclo de vida completo de eventos de tecnologia. Projetada para suportar desde pequenos meetups até grandes conferências, a plataforma automatiza o cadastro de usuários, o controle rigoroso de acessos, a gestão de vagas em tempo real e o armazenamento em nuvem de mídias promocionais.",
    "badges": [
      "Java 17+",
      "Spring Boot 3.x",
      "PostgreSQL",
      "Supabase",
      "Spring Security",
      "JWT"
    ]
  },
  "features": {
    "users_and_access": [
      "Role-Based Access Control (RBAC): Diferenciação clara entre ROLE_ADMIN e ROLE_USER.",
      "Autenticação Stateless: Implementação de tokens JWT para sessões seguras e escaláveis.",
      "Self-Registration: Fluxo simplificado e protegido para novos participantes."
    ],
    "event_management": [
      "CRUD de Eventos: Organizadores podem criar, editar e excluir eventos.",
      "Upload de Banners: Integração com Supabase Storage para processamento de imagens.",
      "Filtros de Busca: Listagem inteligente de eventos ativos com paginação."
    ],
    "enrollment_system": [
      "Controle de Lotação: Validação automática da disponibilidade de vagas.",
      "Prevenção de Duplicidade: Impedimento de inscrições múltiplas no mesmo evento pelo mesmo usuário.",
      "Histórico de Participação: Endpoint para consulta de eventos confirmados."
    ]
  },
  "architecture_and_security": [
    "Padrão MVC: Separação estrita entre Controllers, Services e Repositories.",
    "Filtros de Segurança: Interceptação via Spring Security e validação JWT; senhas com hash BCrypt.",
    "Prevenção N+1: Otimização de consultas com Spring Data JPA.",
    "Global Exception Handling: @ControllerAdvice para respostas HTTP padronizadas (400, 401, 403, 404, 500).",
    "DTOs: Abstração de dados e validação com Hibernate Validator."
  ],
  "technologies": {
    "backend": "Java 17+, Spring Boot 3.x",
    "security": "Spring Security, JWT (JSON Web Tokens)",
    "database": "PostgreSQL, Spring Data JPA, Hibernate",
    "cloud_storage": "Supabase",
    "build_tool": "Maven"
  },
  "setup": {
    "prerequisites": [
      "JDK 17+",
      "Maven 3.8+",
      "PostgreSQL",
      "Conta no Supabase (bucket público criado: eventos-banners)"
    ],
    "steps": [
      {
        "step": 1,
        "action": "Clonar o Repositório",
        "commands": [
          "git clone https://github.com/teusluv/Api_EventosTec.git",
          "cd Api_EventosTec"
        ]
      },
      {
        "step": 2,
        "action": "Configurar application.properties",
        "variables": {
          "spring.datasource.url": "jdbc:postgresql://localhost:5432/eventostec_db",
          "spring.datasource.username": "seu_usuario",
          "spring.datasource.password": "sua_senha",
          "api.security.token.secret": "SUA_CHAVE_SECRETA_JWT",
          "supabase.url": "https://seu-projeto.supabase.co",
          "supabase.key": "sua-anon-key-ou-service-role",
          "supabase.bucket.name": "eventos-banners"
        }
      },
      {
        "step": 3,
        "action": "Compilar e Executar",
        "commands": [
          "mvn clean install",
          "mvn spring-boot:run"
        ]
      }
    ]
  },
  "api_endpoints": {
    "authentication": [
      {
        "method": "POST",
        "route": "/api/auth/register",
        "access": "Público",
        "description": "Registra um novo usuário."
      },
      {
        "method": "POST",
        "route": "/api/auth/login",
        "access": "Público",
        "description": "Autentica usuário e retorna o token JWT."
      }
    ],
    "events": [
      {
        "method": "POST",
        "route": "/api/eventos",
        "access": "ADMIN",
        "description": "Cria um novo evento (Requer Multipart Form-Data para imagem)."
      },
      {
        "method": "GET",
        "route": "/api/eventos",
        "access": "Público / USER",
        "description": "Lista paginada de eventos ativos."
      },
      {
        "method": "GET",
        "route": "/api/eventos/{id}",
        "access": "Público / USER",
        "description": "Detalhes de um evento específico."
      }
    ],
    "enrollments": [
      {
        "method": "POST",
        "route": "/api/eventos/{id}/inscricao",
        "access": "USER",
        "description": "Realiza a inscrição do usuário autenticado no evento especificado."
      }
    ]
  },
  "directory_structure": {
    "root": "src/main/java/com/teusluv/eventostec",
    "folders": {
      "config": "Configurações globais (CORS, Swagger, Beans)",
      "controllers": "Controladores REST (Entrypoints da API)",
      "domain": "Entidades JPA e Modelos de domínio",
      "dtos": "Data Transfer Objects (Isolamento da camada de persistência)",
      "exceptions": "Handlers globais de erros",
      "repositories": "Interfaces de acesso a dados (Spring Data JPA)",
      "security": "Lógica de Filtros JWT, Provider de Autenticação e Rotas",
      "services": "Regras de negócio, cálculos e integração externa (Supabase)"
    }
  },
  "author": {
    "name": "Mateus Nunes",
    "role": "Desenvolvedor Full Stack",
    "education": "Estudante de Ciência da Computação - UNIFACS",
    "github": "https://github.com/teusluv"
  }
}
