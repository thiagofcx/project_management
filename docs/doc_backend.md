# Documentação Técnica – Regras de Negócio (Backend)

## 1. Visão geral

O backend segue uma arquitetura em camadas (apresentação → aplicação/use cases → domínio → infraestrutura). As regras de negócio estão concentradas na camada de **aplicação** (use cases em `application.usecase.impl`) e na **segurança** (`SecurityConfig`). O domínio define os modelos e enums (Role, ProjectStatus).

A aplicação foi estruturada como **monolito modular**: um único deploy e um único processo, com domínios bem delimitados (usuários, projetos, recursos, alocações) e dependências controladas entre módulos. Essa escolha reduz a complexidade operacional e de rede em relação a microsserviços, facilita evoluir e testar por contexto de negócio e, no futuro, permite extrair um módulo para serviço próprio se for necessário escalar ou isolar.

---

## 2. Autenticação e autorização

### 2.1 Autenticação (login)

- **Endpoint:** `POST /api/auth/`** (público).
- **Regras:**
  - Usuário deve existir por e-mail.
  - Senha informada deve coincidir com o hash armazenado (BCrypt).
  - Em caso de e-mail inexistente ou senha incorreta, retorna erro genérico de credenciais inválidas (não revela se o e-mail existe).
- **Saída:** token JWT + dados do usuário (id, email, role).

Referência: `AuthenticateUserUseCaseImpl`.

### 2.2 Papéis (Role)


| Papel   | Valor   | Uso                                                                                                               |
| ------- | ------- | ----------------------------------------------------------------------------------------------------------------- |
| ADMIN   | ADMIN   | CRUD de usuários; acesso a projetos, recursos e alocações.                                                        |
| MANAGER | MANAGER | Acesso a projetos, recursos, alocações e listagem/consulta de usuários (não pode criar/alterar/remover usuários). |
| USER    | USER    | Apenas rotas que caem em `anyRequest().authenticated()` (ex.: dashboard).                                         |


Definição: `domain.model.Role`.

### 2.3 Regras de acesso por recurso


| Recurso                                   | Método   | Quem pode                    |
| ----------------------------------------- | -------- | ---------------------------- |
| `/api/auth/`**                            | Qualquer | Público                      |
| `POST /api/users`                         | POST     | ADMIN                        |
| `GET /api/users/`**                       | GET      | ADMIN, MANAGER               |
| `PUT /api/users/**`                       | PUT      | ADMIN                        |
| `DELETE /api/users/**`                    | DELETE   | ADMIN                        |
| `/api/projects`, `/api/projects/**`       | Todos    | ADMIN, MANAGER               |
| `/api/resources`, `/api/resources/**`     | Todos    | ADMIN, MANAGER               |
| `/api/allocations`, `/api/allocations/**` | Todos    | ADMIN, MANAGER               |
| Demais rotas (ex.: `/api/dashboard`)      | -        | Qualquer usuário autenticado |


Referência: `SecurityConfig.securityFilterChain`.

---

## 3. Usuários (Users)

### 3.1 Criar usuário

- **Quem:** apenas ADMIN (camada de aplicação e `@PreAuthorize`).
- **Regras:**
  - E-mail deve ser único no sistema; se já existir, lança `UserAlreadyExistsException`.
  - Papel (role) é opcional; padrão é `USER`.
  - Senha é armazenada com hash (BCrypt), nunca em texto puro.
- **Validação de entrada (request):** nome, e-mail e senha obrigatórios; e-mail no formato válido.

Referência: `CreateUserUseCaseImpl`.

### 3.2 Atualizar usuário

- **Quem:** apenas ADMIN.
- **Regras:**
  - Usuário alvo deve existir; caso contrário, `UserNotFoundException`.
  - E-mail deve ser único **excluindo o próprio usuário**; se outro usuário já usa o e-mail, `UserAlreadyExistsException`.
  - Senha **não** é alterada no update (campo de senha não é atualizado no domínio).
  - Role opcional; se não informado, mantém o role atual.

Referência: `UpdateUserUseCaseImpl`.

### 3.3 Excluir usuário

- **Quem:** apenas ADMIN.
- **Regras:**
  - Usuário alvo deve existir; caso contrário, `UserNotFoundException`.
  - Exclusão é por ID (não há regra explícita de “não pode excluir a si mesmo” no use case; a segurança é por role).

Referência: `DeleteUserUseCaseImpl`.

### 3.4 Perfil do usuário logado

- **Endpoint:** `GET /api/users/me`.
- **Regras:** retorna o usuário cujo ID está no token JWT. Acesso HTTP está sujeito a `GET /api/users/`**, ou seja, apenas ADMIN e MANAGER (qualquer usuário autenticado não tem acesso a `/me` pela configuração atual).

Referência: `GetCurrentUserUseCaseImpl`, `UserController`, `SecurityConfig`.

---

## 4. Projetos (Projects)

### 4.1 Status de projeto (ProjectStatus)

Valores: `PLANNING`, `IN_PROGRESS`, `ON_HOLD`, `COMPLETED`.  
Definição: `domain.model.ProjectStatus`.

### 4.2 Criar projeto

- **Quem:** ADMIN ou MANAGER.
- **Regras:**
  - Status opcional; padrão é `PLANNING`.
  - Não há validação de datas (startDate/endDate) nem de unicidade de nome no use case.
- **Validação de entrada:** nome obrigatório (1–255 caracteres), descrição até 2000, startDate e endDate obrigatórios.

Referência: `CreateProjectUseCaseImpl`, DTOs de request.

### 4.3 Atualizar projeto

- **Regras:**
  - Projeto deve existir; caso contrário, `ProjectNotFoundException`.
  - Campos opcionais: se não enviados, mantêm os valores atuais (nome, descrição, datas, status).
- **Validação de entrada:** mesmo esquema do create (nome, descrição, datas obrigatórios quando enviados).

Referência: `UpdateProjectUseCaseImpl`.

### 4.4 Excluir projeto

- **Regras:** projeto deve existir; caso contrário, `ProjectNotFoundException`. Não há regra explícita que impeça exclusão quando existem alocações (o comportamento depende de cascade/constraints no banco).

Referência: `DeleteProjectUseCaseImpl`.

---

## 5. Recursos (Resources)

“Recursos” são entidades de catálogo (ex.: pessoas/equipe) alocáveis a projetos, identificados por nome, e-mail e skills.

### 5.1 Criar recurso

- **Quem:** ADMIN ou MANAGER.
- **Regras:**
  - E-mail deve ser único; se já existir, `ResourceAlreadyExistsException`.
- **Validação de entrada:** nome e e-mail obrigatórios, e-mail válido, tamanhos máximos (ex.: nome 255, e-mail 255, skills 100).

Referência: `CreateResourceUseCaseImpl`, DTOs de request.

### 5.2 Atualizar recurso

- **Regras:**
  - Recurso deve existir; caso contrário, `ResourceNotFoundException`.
  - E-mail deve ser único excluindo o próprio recurso; se outro recurso já usa o e-mail, `ResourceAlreadyExistsException`.
  - Campos opcionais: se não enviados, mantêm os valores atuais.

Referência: `UpdateResourceUseCaseImpl`.

### 5.3 Excluir recurso

- **Regras:** recurso deve existir; caso contrário, `ResourceNotFoundException`. Não há regra explícita que impeça exclusão com alocações ativas (depende do banco).

Referência: `DeleteResourceUseCaseImpl`.

---

## 6. Alocações (Allocations)

Alocação associa um **recurso** a um **projeto**. A regra central é: um recurso não pode estar alocado a mais de um projeto “ativo” ao mesmo tempo.

### 6.1 Projeto “ativo” (não concluído)

Considera-se projeto **não concluído** quando o status é diferente de `COMPLETED` (ou seja, `PLANNING`, `IN_PROGRESS` ou `ON_HOLD`).  
Isso é usado para:

- Impedir nova alocação se o recurso já está em algum projeto não concluído.
- Na atualização de alocação, impedir que o recurso fique em dois projetos não concluídos (excluindo a própria alocação em edição).
- Contar “recursos alocados” no dashboard (recursos com pelo menos uma alocação em projeto não concluído).

Referência: `AllocationRepository.existsByResourceIdWithProjectStatusNotCompleted`, `existsByResourceIdWithProjectStatusNotCompletedExceptId`, `countDistinctResourceIdsWithProjectStatusNotCompleted`.

### 6.2 Criar alocação

- **Quem:** ADMIN ou MANAGER.
- **Regras:**
  - Projeto deve existir; caso contrário, `ProjectNotFoundException`.
  - Recurso deve existir; caso contrário, `ResourceNotFoundException`.
  - O recurso **não** pode possuir outra alocação em projeto com status diferente de `COMPLETED`; caso possua, `ResourceAlreadyAllocatedException`.
- **Validação de entrada:** projectId e resourceId obrigatórios.

Referência: `CreateAllocationUseCaseImpl`.

### 6.3 Atualizar alocação

- **Regras:**
  - Alocação deve existir; caso contrário, `AllocationNotFoundException`.
  - Se projectId ou resourceId forem enviados, as entidades correspondentes devem existir.
  - Após aplicar os novos projectId/resourceId (ou manter os atuais se não enviados), o recurso não pode ter **outra** alocação em projeto não concluído, **excluindo esta alocação**; caso contrário, `ResourceAlreadyAllocatedException`.
- **Validação de entrada:** projectId e resourceId opcionais (para update parcial).

Referência: `UpdateAllocationUseCaseImpl`.

### 6.4 Excluir alocação

- **Regras:** alocação deve existir; caso contrário, `AllocationNotFoundException`. Não há outra regra de negócio no use case.

Referência: `DeleteAllocationUseCaseImpl`.

---

## 7. Dashboard

- **Quem:** qualquer usuário autenticado (rota não listada em `SecurityConfig`; cai em `anyRequest().authenticated()`).
- **Regras (agregações):**
  - **Projetos:** total e contagem por status (PLANNING, IN_PROGRESS, ON_HOLD, COMPLETED).
  - **Recursos:** total; “alocados” = quantidade de recursos distintos com pelo menos uma alocação em projeto **não concluído**; “não alocados” = total − alocados (mínimo 0).
  - **Alocações:** total de registros de alocação.

Referência: `GetDashboardSummaryUseCaseImpl`.

---

## 8. Validações de entrada (API)

Resumo das validações nos DTOs de entrada (Bean Validation):


| Contexto                | Campos / Regras                                                                                 |
| ----------------------- | ----------------------------------------------------------------------------------------------- |
| Auth                    | E-mail e senha obrigatórios; e-mail válido.                                                     |
| Criar/atualizar usuário | Nome, e-mail e (no create) senha obrigatórios; e-mail válido.                                   |
| Projeto                 | Nome 1–255, descrição até 2000, startDate e endDate obrigatórios.                               |
| Recurso                 | Nome e e-mail obrigatórios; e-mail válido; tamanhos máximos (nome 255, e-mail 255, skills 100). |
| Alocação                | projectId e resourceId obrigatórios no create.                                                  |
| Paginação               | page ≥ 0, size entre 1 e 100 (onde há listagem paginada).                                       |


Referência: classes em `presentation.dto` e anotações `@Valid` / `@Validated` nos controllers.

---

## 9. Exceções de domínio


| Exceção                             | Quando                                                                            |
| ----------------------------------- | --------------------------------------------------------------------------------- |
| `InvalidCredentialsException`       | Login com e-mail inexistente ou senha incorreta.                                  |
| `UnauthorizedException`             | Ação restrita a ADMIN (ex.: criar/atualizar/excluir usuário) feita por não-admin. |
| `UserNotFoundException`             | Usuário não encontrado por ID ou no login.                                        |
| `UserAlreadyExistsException`        | E-mail já cadastrado (create) ou em uso por outro usuário (update).               |
| `ProjectNotFoundException`          | Projeto não encontrado (ex.: em alocação ou update).                              |
| `ResourceNotFoundException`         | Recurso não encontrado.                                                           |
| `ResourceAlreadyExistsException`    | E-mail de recurso já cadastrado ou em uso por outro recurso.                      |
| `ResourceAlreadyAllocatedException` | Recurso já alocado a um projeto não concluído (create/update de alocação).        |
| `AllocationNotFoundException`       | Alocação não encontrada.                                                          |


---

## 10. Observações técnicas

- **Senha:** apenas no create de usuário; nunca retornada nos DTOs e não atualizada no update.
- **Datas de projeto:** não há regra no use case que exija `startDate ≤ endDate` ou que impeça projetos encerrados.
- **Exclusão em cascata:** projetos, recursos e alocações podem ter impacto em integridade referencial conforme o mapeamento JPA e o banco (não documentado aqui).
- **Idempotência:** create de usuário/recurso com mesmo e-mail falha; create de alocação para o mesmo par (projeto, recurso) não está explicitamente tratado como duplicata no use case (pode depender de constraint única no repositório/banco).

