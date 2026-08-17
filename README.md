# Dealership Management (DM) - Sistema de Gestão de Veículos e Concessionárias

Este repositório contém a solução completa para o **Desafio Técnico de Desenvolvedor Fullstack**, desenvolvida para centralizar e gerenciar o inventário de veículos e o ecossistema de concessionárias parceiras de uma montadora.

---

## 1. Visão Geral do Projeto

O **Dealership Management** foi arquitetado e desenvolvido seguindo padrões rigorosos de mercado (Clean Code, princípios SOLID, Arquitetura Hexagonal no backend e Feature-Sliced Design no frontend), superando os requisitos básicos ao implementar 100% de cobertura de testes de linha no backend, containerização completa via Docker Compose e integrações robustas com serviços externos.

### O Desafio Técnico Atendido

* **Gestão de Concessionárias:** Cadastro, consulta, alteração, exclusão (com validação de dependências) e enriquecimento automático de endereços via **ViaCEP**.


* **Gestão de Veículos:** CRUD completo de automóveis com tipagem estrita de combustíveis, controle de inventário e upload de imagens via **MinIO (Compatível com S3)**.


* **Associação e Listagem:** Relacionamento direto entre veículos e concessionárias com paginação e filtros avançados.



---

## 2. Arquitetura da Solução

O sistema adota uma arquitetura desacoplada de microsserviços lógicos dividida em duas camadas independentes que se comunicam via API REST:

```text
[ Frontend (Next.js / TypeScript) ] --(REST / JSON)---> [ Backend (Java 21 / Spring Boot) ]
                                                                     |
                                                       +-------------+-------------+
                                                       |                           |
                                           [ PostgreSQL Database ]        [ MinIO Object Storage ]

```

* **Backend:** Desenvolvido em Java 21 com Spring Boot, estruturado sob os conceitos de Domain-Driven Design (DDD) e Arquitetura Hexagonal. Para aprofundar-se nos detalhes de camadas, portas, adaptadores e contratos, consulte a [Documentação do Backend na Wiki do Projeto](https://www.google.com/search?q=./backend/README.md).
* **Frontend:** Desenvolvido em Next.js (App Router) com TypeScript, Tailwind CSS e Shadcn/UI, consumindo dados assíncronos de forma otimizada com o TanStack Query. Para detalhes de componentização e padrões visuais, consulte a [Documentação do Frontend na Wiki do Projeto](https://www.google.com/search?q=./frontend/README.md).

---

## 3. Stack Tecnológica

### Backend

* **Linguagem & Framework:** Java 21, Spring Boot.


* **Arquitetura & Padrões:** Arquitetura Hexagonal, DDD, DTOs, Spring Data JPA / Hibernate.


* **Integrações Externas:** Spring Cloud OpenFeign (ViaCEP e OpenCNPJ), AWS SDK v2 (MinIO / S3).
* **Testes & Qualidade:** JUnit 5, Mockito, Spring MockMvc, JaCoCo (100% de cobertura de linhas).

### Frontend

* **Framework:** Next.js (App Router) / React.


* **Gerenciamento de Estado & Cache:** TanStack Query (React Query).


* **Formulários & Validação:** React Hook Form e Zod.


* **Estilização:** Tailwind CSS e componentes Shadcn/UI.



### Infraestrutura

* **Containerização:** Docker e Docker Compose.


* **Banco de Dados:** PostgreSQL (Produção) / H2 Database (Ambiente de Testes).


* **Armazenamento de Mídia:** MinIO (Object Storage S3-compatible).

---

## 4. Guia de Instalação e Execução (Runbook com Docker Compose)

Para rodar a aplicação completa de forma rápida e isolada em sua máquina, utilize o Docker Compose integrado com o gerenciamento de variáveis de ambiente.

### Passo a Passo:

1. **Configurar as variáveis de ambiente:**
Na raiz do repositório, utilize o arquivo `.env.example` como base para criar o seu arquivo `.env` local:
```bash
cp .env.example .env

```


*(Abra o arquivo `.env` gerado e preencha as credenciais locais desejadas para o banco de dados e o storage)*.
2. **Subir a infraestrutura completa (PostgreSQL, MinIO e Backend):**
```bash
docker compose --profile full up -d

```


3. **Executar a suíte de testes do Backend (Opcional):**
Caso queira validar a integridade e os relatórios de cobertura do JaCoCo:
```bash
cd backend/dealership-management
./mvnw clean test

```


4. **Executar o Frontend localmente:**
```bash
cd frontend/dealership-ui
npm install
npm run dev

```



---

## 5. Documentação Adicional (Wiki)

Para uma imersão técnica detalhada em cada uma das partes do sistema, acesse os guias dedicados:

* 📄 **[Documentação Completa do Backend](https://www.google.com/search?q=./backend/README.md)** (Arquitetura Hexagonal, Endpoints REST, Regras de Negócio e Testes).
* 📄 **[Documentação Completa do Frontend](https://www.google.com/search?q=./frontend/README.md)** (Feature-Sliced Design, Hooks do TanStack Query, Schemas Zod e Testes E2E com Playwright).
