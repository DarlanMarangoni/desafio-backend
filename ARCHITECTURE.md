### Arquitetura do Sistema

Este diagrama representa o fluxo de dados e a integração entre os componentes do sistema. Você pode copiar o código abaixo e colar no [draw.io](https://app.diagrams.net/) (Vá em `+` -> `Advanced` -> `Mermaid`) ou visualizar diretamente em visualizadores compatíveis com Mermaid.

```mermaid
graph TD
    subgraph "External/Infrastructure"
        SQS[AWS SQS / LocalStack]
        MG[Message Generator]
    end

    subgraph "Microservices"
        TC[Transaction Consumer]
        LB[Nginx Load Balancer]
        BA1[Balance API - Instance 1]
        BA2[Balance API - Instance 2]
    end

    subgraph "Data Storage"
        Mongo[(MongoDB)]
    end

    subgraph "Observability"
        Prom[Prometheus]
        Jaeger[Jaeger]
    end

    %% Fluxo de Transação
    MG -->|Envia Transação| SQS
    SQS -->|Consome Evento| TC
    TC -->|Salva Transação| Mongo
    TC -->|Atualiza Saldo| Mongo

    %% Fluxo de Consulta
    User((Usuário / Cliente)) -->|Consulta Saldo| LB
    LB -->|Round Robin| BA1
    LB -->|Round Robin| BA2
    BA1 -->|Busca Saldo| Mongo
    BA2 -->|Busca Saldo| Mongo

    %% Observabilidade
    TC -.->|Métricas / Prometheus| Prom
    TC -.->|Traces / OTLP| Jaeger
    BA1 -.->|Métricas / Prometheus| Prom
    BA1 -.->|Traces / OTLP| Jaeger
    BA2 -.->|Métricas / Prometheus| Prom
    BA2 -.->|Traces / OTLP| Jaeger
    LB -.->|Status| Prom
```

### Componentes:
- **Message Generator**: Simula a entrada de transações.
- **SQS**: Fila de mensagens para desacoplamento.
- **Transaction Consumer**: Processador assíncrono e reativo de transações.
- **Balance API**: API REST reativa para consulta de saldos.
- **MongoDB**: Banco de dados NoSQL para persistência.
- **Prometheus**: Coleta de métricas (Actuator).
- **Jaeger**: Rastreamento distribuído (OpenTelemetry).
