# Desafio Backend Itaú - Consulta de Saldo

Este projeto implementa uma solução de alta performance para processamento de transações financeiras e consulta de saldos, atendendo aos requisitos de consistência eventual, observabilidade e escalabilidade.

## Arquitetura

O diagrama de arquitetura detalhado pode ser visualizado em [ARCHITECTURE.md](ARCHITECTURE.md).

A solução é composta por dois microserviços desenvolvidos em **Kotlin** com **Spring Boot 3.4.3** e **Project Reactor**:

1.  **transaction-consumer**: Consome eventos de transação de uma fila SQS, persiste os registros no MongoDB e atualiza o saldo da conta.
2.  **balance-api**: Fornece um endpoint reativo para consulta de saldo em tempo real.

### Principais Características

- **Reatividade**: Fluxo end-to-end não bloqueante para suportar alta carga.
- **Idempotência**: Processamento de transações baseado em IDs únicos vindos dos eventos.
- **Consistência**: Atualização de saldo usando `findAndModify` com controle de versão por timestamp (`updatedAt`).
- **Observabilidade**: 
    - Métricas expostas via **Prometheus** (`/actuator/prometheus`).
    - Distributed Tracing via **OpenTelemetry (OTLP)** com exportação para **Jaeger**.

## Como Executar

### Pré-requisitos
- Docker e Docker Compose
- Maven 3.9+
- JDK 24

### Passo a Passo

1. **Subir toda a aplicação e infraestrutura**:
   ```bash
   docker-compose up -d --build
   ```
   Isso iniciará todos os componentes necessários: 
   - **LocalStack (SQS)** e **Message Generator** (gera 300k transações automaticamente).
   - **MongoDB** (Persistência).
   - **Prometheus** & **Grafana** (Métricas).
   - **Jaeger** (Distributed Tracing).
   - **Nginx (Load Balancer)** na porta 8080.
   - **Transaction Consumer** (Microserviço de consumo).
   - **Balance API** (2 instâncias do microserviço de consulta).

## Monitoramento e Acesso

- **Nginx Load Balancer**: [http://localhost:8080/balance/{accountId}](http://localhost:8080/balance/{accountId})
- **Prometheus**: [http://localhost:9090](http://localhost:9090)
- **Jaeger UI**: [http://localhost:16686](http://localhost:16686)
- **Instância 1 (Direto)**: `GET http://localhost:9001/balance/{accountId}`
- **Instância 2 (Direto)**: `GET http://localhost:9002/balance/{accountId}`

## Testes

Para validar o recebimento de mensagens no SQS via CLI:
```bash
export AWS_DEFAULT_REGION=sa-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/transacoes-financeiras-processadas
```