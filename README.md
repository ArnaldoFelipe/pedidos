# 📦 Microsserviço de Pedidos

## 💼 Order Service — Saga Pattern & Event-Driven Architecture

Microsserviço responsável pelo **ciclo de vida dos pedidos** e pelo ponto de entrada da orquestração distribuída baseada no padrão **Saga (Coreografia)**.

A aplicação recebe as intenções de compra, realiza a persistência com integridade relacional e coordena a comunicação assíncrona com o ecossistema de microsserviços utilizando **RabbitMQ**.

O projeto segue princípios de **Clean Code**, arquitetura em camadas e desacoplamento de domínio, permitindo que o serviço opere de forma independente e resiliente.

---

## 👨‍💻 Sobre o Projeto

O microsserviço é responsável por:

- 📥 Receber requisições de criação de pedidos via API REST
- 💾 Persistir pedidos utilizando **Spring Data JPA** e **PostgreSQL**
- 🔄 Controlar o ciclo de vida do pedido através de enum de status
- 📤 Publicar eventos para reserva/baixa de estoque
- 📥 Consumir eventos de confirmação de estoque de forma assíncrona
- 📨 Utilizar **RabbitMQ** como mecanismo de comunicação entre os microsserviços
- 🛡️ Realizar tratamento de exceções e tolerância a falhas na camada de mensageria

---

# 🏗️ Arquitetura

O serviço faz parte de uma arquitetura distribuída baseada em **Event-Driven Architecture**, utilizando o padrão **Saga por Coreografia**.

O fluxo principal da transação ocorre da seguinte forma:

```text
┌──────────────┐
│    Cliente   │
└──────┬───────┘
       │
       │ POST /pedidos
       ▼
┌──────────────────┐
│  Order Service   │
└────────┬─────────┘
         │
         │ Persiste pedido
         ▼
┌──────────────────┐
│    PostgreSQL    │
└──────────────────┘
         │
         │ Publica BaixaEstoqueEvent
         ▼
┌──────────────────┐
│     RabbitMQ     │
└────────┬─────────┘
         │
         │ estoque.baixar.queue
         ▼
┌──────────────────┐
│  Estoque Service │
└────────┬─────────┘
         │
         │ PedidoAprovadoEvent
         ▼
┌──────────────────┐
│     RabbitMQ     │
└────────┬─────────┘
         │
         │ pedido.aprovar.queue
         ▼
┌──────────────────┐
│  Order Service   │
└────────┬─────────┘
         │
         │ Atualiza status
         ▼
    🟢 APROVADO
```

---

# 🔄 Saga Pattern

O microsserviço participa da Saga através de eventos assíncronos.

### 1. Criação do pedido

O cliente envia uma requisição para:

```http
POST /pedidos
```

O pedido é persistido no banco local com o status:

```text
PENDENTE
```

### 2. Disparo do evento

Após a criação do pedido, o serviço publica um evento:

```text
BaixaEstoqueEvent
```

Esse evento contém o identificador do pedido e os itens solicitados.

O evento é enviado ao **RabbitMQ**, onde será processado pelo microsserviço responsável pelo estoque.

### 3. Processamento do estoque

O **Estoque Service** recebe o evento e realiza a operação de baixa/reserva dos produtos.

### 4. Confirmação

Após o processamento, o serviço de estoque publica:

```text
PedidoAprovadoEvent
```

### 5. Atualização do pedido

O **Order Service** possui um listener responsável por consumir o evento de aprovação.

Ao receber a confirmação, o status do pedido é atualizado para:

```text
APROVADO
```

---

# 📡 Mensageria

| Componente | Nome | Tipo / Detalhe |
|---|---|---|
| Exchange | `pedido.exchange` | Direct Exchange |
| Fila publicada | `estoque.baixar.queue` | Fila consumida pelo serviço de estoque |
| Fila escutada | `pedido.aprovar.queue` | Fila de confirmação de estoque |
| Routing Key | `pedido.aprovar.rk` | Chave de ligação para aprovação |

### Fluxo de eventos

```text
Order Service
     │
     │ BaixaEstoqueEvent
     ▼
RabbitMQ
     │
     │ estoque.baixar.queue
     ▼
Estoque Service
     │
     │ PedidoAprovadoEvent
     ▼
RabbitMQ
     │
     │ pedido.aprovar.queue
     ▼
Order Service
```

---

# 🗄️ Modelo de Dados

O banco de dados local mantém apenas as informações pertencentes ao domínio de pedidos.

### PostgreSQL

```sql
CREATE TABLE pedido (
    pedido_id UUID PRIMARY KEY,
    data_pedido TIMESTAMP NOT NULL,
    status_pedido INTEGER NOT NULL,
    valor_total NUMERIC(12, 2) NOT NULL
);
```

### Status do Pedido

| Código | Status | Descrição |
|---:|---|---|
| `0` | `PENDENTE` | Pedido criado aguardando processamento |
| `1` | `CANCELADO` | Pedido cancelado |
| `2` | `APROVADO` | Pedido aprovado após confirmação do estoque |

---

# 🚀 Tecnologias

### Backend

- ☕ Java 17+
- 🍃 Spring Boot
- 🌱 Spring Data JPA
- 📦 Maven
- 🗃️ Hibernate

### Banco de Dados

- 🐘 PostgreSQL

### Mensageria

- 🐇 RabbitMQ
- `@RabbitListener`
- Event-Driven Architecture
- Saga Pattern

### Práticas e Arquitetura

- Clean Code
- Arquitetura em camadas
- Separação de responsabilidades
- Desacoplamento de domínio
- Comunicação assíncrona
- Transações distribuídas
- Tratamento de exceções

---

# 🔌 Contratos de Comunicação

## 1. Criar Pedido

### Endpoint

```http
POST /pedidos
Content-Type: application/json
```

### Payload

```json
{
  "pedidoId": "55555555-5555-5555-5555-555555555555",
  "valorTotal": 1500.00,
  "itens": [
    {
      "produtoId": "22222222-2222-2222-2222-222222222222",
      "quantidade": 2
    }
  ]
}
```

---

## 2. Evento de Aprovação

O serviço consome o evento `PedidoAprovadoEvent` através do RabbitMQ.

```json
{
  "pedidoId": "55555555-5555-5555-5555-555555555555"
}
```

---

# 🛠️ Como Executar

## Pré-requisitos

Antes de executar o projeto, certifique-se de possuir:

- Java 17+ ou Java 21+
- Maven
- PostgreSQL
- RabbitMQ

O PostgreSQL e o RabbitMQ devem estar disponíveis na rede local ou através de containers.

---

## 📥 Clone o Repositório

```bash
git clone https://github.com/seu-usuario/ms-pedidos.git

cd ms-pedidos
```

---

## ▶️ Executando a Aplicação

Utilizando o Maven Wrapper:

### Linux / macOS

```bash
./mvnw clean spring-boot:run
```

### Windows

```bash
mvnw.cmd clean spring-boot:run
```

---

# 📌 Responsabilidades do Serviço

O **Order Service** é responsável por:

```text
                    ORDER SERVICE
                         │
          ┌──────────────┼──────────────┐
          │              │              │
          ▼              ▼              ▼
       REST API      PostgreSQL      RabbitMQ
          │              │              │
          │              │              │
       Criação       Persistência    Eventos
       de pedidos       local        assíncronos
          │                             │
          └──────────────┬──────────────┘
                         ▼
                   Saga Pattern
```

O serviço **não depende diretamente da implementação interna dos demais microsserviços**. A comunicação entre os componentes ocorre através de eventos, reduzindo o acoplamento e permitindo maior independência entre os serviços.

---

# 🎯 Objetivo do Projeto

Este projeto tem como objetivo demonstrar, na prática, a construção de um microsserviço utilizando conceitos presentes em arquiteturas distribuídas modernas, como:

- Microsserviços
- Event-Driven Architecture
- Saga Pattern
- Comunicação assíncrona
- RabbitMQ
- Spring Boot
- Persistência relacional
- Separação de responsabilidades
- Resiliência em sistemas distribuídos

---

## 📚 Conceitos Demonstrados

```text
Spring Boot
     │
     ├── REST API
     │
     ├── Spring Data JPA
     │
     ├── PostgreSQL
     │
     └── RabbitMQ
            │
            ├── Events
            ├── Queues
            ├── Exchanges
            └── Routing Keys
                    │
                    ▼
              Saga Pattern
                    │
                    ▼
          Event-Driven Architecture
```

---

# 👨‍💻 Autor

Desenvolvido por **Arnaldo Felipe Da Silva**.

> Projeto desenvolvido com foco em aprendizado e aplicação prática de arquitetura de microsserviços, mensageria e sistemas distribuídos.
