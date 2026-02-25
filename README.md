# Project Management API

API REST para gerenciamento de projetos, recursos (pessoas), alocações e usuários. Inclui autenticação JWT, CRUD de projetos, recursos e alocações, e endpoint de resumo para dashboard.

## Tecnologias

- **Java 21** · **Spring Boot 4** · **Spring Security** (JWT) · **Spring Data JPA**
- **MySQL 8**
- **Maven**

## Pré-requisitos

- **Java 21** (LTS)
- **Maven 3.8+**
- **MySQL 8** (se for rodar a API localmente sem Docker)
- **Docker e Docker Compose** (se for subir o ambiente completo em containers)

## Instalação

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd project_management
```

### 2. Configuração

As variáveis de ambiente definem banco e JWT. Há duas formas de uso:

**Opção A – Arquivo `.env` (recomendado para Docker)**

```bash
cp .env.example .env
# Edite .env se precisar (usuário/senha do banco, chave JWT).
```

**Opção B – Variáveis no shell (desenvolvimento local)**

Para rodar a API localmente com MySQL em `localhost:3306`:

```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/project_management?createDatabaseIfNotExist=true&serverTimezone=UTC"
export SPRING_DATASOURCE_USERNAME=dev_user
export SPRING_DATASOURCE_PASSWORD=dev_pass
export SECURITY_JWT_SECRET_KEY="sua-chave-secreta-aqui"
export SECURITY_JWT_EXPIRATION=86400000
```

| Variável | Descrição | Exemplo |
|----------|-----------|--------|
| `SPRING_DATASOURCE_URL` | JDBC URL do MySQL | `jdbc:mysql://db:3306/project_management?createDatabaseIfNotExist=true&serverTimezone=UTC` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `dev_user` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `dev_pass` |
| `SECURITY_JWT_SECRET_KEY` | Chave para assinatura do JWT | String longa e aleatória |
| `SECURITY_JWT_EXPIRATION` | Expiração do token (ms) | `86400000` (24h) |

## Como rodar a aplicação

### Com Docker Compose (ambiente completo)

Sobe MySQL e a API em containers. Exige Docker e Docker Compose.

```bash
# Na raiz do projeto
docker compose up -d
```

- **API:** http://localhost:8080  
- **MySQL:** porta `3306` (host); usuário `dev_user`, senha `dev_pass`, banco `project_management`.

Parar os serviços:

```bash
docker compose down
```

Recriar a API após mudanças no código:

```bash
docker compose up -d --build api
```

### Local (API na máquina, banco no Docker)

Sobe só o MySQL no Docker e roda a API com Maven:

```bash
# Sobe apenas o banco
docker compose up -d db

# Define as variáveis para apontar para localhost (ou use .env com URL 127.0.0.1)
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/project_management?createDatabaseIfNotExist=true&serverTimezone=UTC"
export SPRING_DATASOURCE_USERNAME=dev_user
export SPRING_DATASOURCE_PASSWORD=dev_pass
export SECURITY_JWT_SECRET_KEY="3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b"
export SECURITY_JWT_EXPIRATION=86400000

# Roda a API
./mvnw spring-boot:run
```

A API ficará em http://localhost:8080.

### Local (MySQL e API na máquina)

1. Crie o banco e o usuário no MySQL (ou use um banco já existente):

```sql
CREATE DATABASE IF NOT EXISTS project_management;
CREATE USER IF NOT EXISTS 'dev_user'@'%' IDENTIFIED BY 'dev_pass';
GRANT ALL ON project_management.* TO 'dev_user'@'%';
FLUSH PRIVILEGES;
```

2. Exporte as variáveis de ambiente (como na opção B acima) com `SPRING_DATASOURCE_URL` apontando para `127.0.0.1:3306`.

3. Execute:

```bash
./mvnw spring-boot:run
```

## API

- **Base URL:** `http://localhost:8080`
- **Autenticação:** JWT no header `Authorization: Bearer <token>`.

### Endpoints principais

| Método | Path | Descrição |
|--------|------|-----------|
| `POST` | `/api/auth` | Login (email/senha) → retorna token |
| `GET`  | `/api/dashboard/summary` | Resumo (projetos, recursos, alocações) |
| `GET/POST` | `/api/projects` | Listar (com filtros/ordenação) e criar projeto |
| `GET/PUT/DELETE` | `/api/projects/{id}` | Buscar, atualizar e excluir projeto |
| `GET/POST` | `/api/resources` | Listar (com filtros/ordenação) e criar recurso |
| `GET/PUT/DELETE` | `/api/resources/{id}` | Buscar, atualizar e excluir recurso |
| `GET/POST` | `/api/allocations` | Listar (com filtros/ordenação) e criar alocação |
| `GET/PUT/DELETE` | `/api/allocations/{id}` | Buscar, atualizar e excluir alocação |
| `GET`  | `/api/users/me` | Usuário autenticado |
| `GET/POST` | `/api/users` | Listar e criar usuário (admin) |
| `GET/PUT/DELETE` | `/api/users/{id}` | Buscar, atualizar e excluir usuário |

Respostas em **snake_case**. Listagens aceitam `page`, `size`, `sort_by`, `sort_order` e filtros opcionais (consulte a documentação da API ou o código dos controllers).

## Estrutura do projeto

```
src/main/java/com/thiago/projectmanagement/
├── application/          # Use cases, DTOs
├── domain/               # Modelos, repositórios (interface), exceções
├── infrastructure/       # Persistência (JPA), segurança (JWT), config (CORS)
└── presentation/         # Controllers, DTOs de request, exception handler
```

## Licença

Projeto de uso interno/educacional.
