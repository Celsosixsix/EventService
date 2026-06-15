# Event Service

Serviço de gestão de eventos (shows, espetáculos etc.) do sistema de reservas. CRUD de eventos e ponto de partida da saga de estoque de assentos: ao criar um evento, publica `EventCreatedEvent` para o serviço de reserva de assentos inicializar o estoque.

## Stack

- Java 21 + Spring Boot
- Spring Security (JWT local em `dev`, headers de gateway em `prod`)
- MyBatis (acesso a dados)
- MySQL
- Spring Cloud AWS SQS (publicação de eventos, fila FIFO)
- Spring Boot Actuator + Micrometer (CloudWatch opcional + Prometheus)

## Responsabilidades

- CRUD de eventos (nome, data, capacidade, local, preço do ticket, limite de meia-entrada)
- Geração de ID via `timeBasedEpochGenerator` (UUID v7-like)
- Publicação de `EventCreatedEvent` na fila `fila-evento-cadastrado.fifo` após criação, consumido pelo ticket-reservation-service para registrar o estoque inicial

## Endpoints

Todos exigem papel `ADMIN`.

| Método | Rota | Descrição |
|---|---|---|
| POST | `/events` | Cria evento e dispara `EventCreatedEvent` (best-effort) |
| GET | `/events` | Lista todos os eventos |
| GET | `/events/{id}` | Busca evento por ID |
| PUT | `/events/{id}` | Atualiza evento |
| DELETE | `/events/{id}` | Remove evento |

### Validações do `EventDto`

- `name`: obrigatório
- `date`: obrigatória, deve ser futura
- `capacity`: obrigatória, mínimo 1
- `halfPriceLimit`: opcional, ≥ 0
- `location`: obrigatório
- `ticketPrice`: obrigatório, ≥ 0

## Integração via SQS

- **Publica** `EventCreatedEvent` (`eventId`, `capacity`, `ticketPrice`) na fila `fila-evento-cadastrado.fifo`, usando `eventId` como `MessageGroupId`/`MessageDeduplicationId`.
- A publicação é best-effort: falha ao publicar não impede a criação do evento (apenas loga warning).

## Configuração (variáveis de ambiente)

| Variável | Descrição |
|---|---|
| `PROFILE` | `dev` (valida JWT local) ou `prod` (confia em headers do API Gateway) |
| `DATABASE_URL` / `DATABASE_USERNAME` / `DATABASE_PASSWORD` | Conexão MySQL |
| `JWT_SECRET` | Segredo JWT compartilhado com identity-service |
| `AWS_REGION` | Região AWS (default `us-east-1`) |
| `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY` / `AWS_SESSION_TOKEN` | Credenciais AWS para SQS |
| `AWS_SQS_EVENT_QUEUE` | Nome da fila de publicação (default `fila-evento-cadastrado.fifo`) |
| `CLOUDWATCH_ENABLED` | Habilita export de métricas no CloudWatch (default `false`) |
| `CLOUDWATCH_NAMESPACE` | Namespace das métricas (default `EventService`) |

## Banco de dados

- Mappers MyBatis em `classpath:repository/**/*.xml`, com `map-underscore-to-camel-case=true`.
- `UuidBinaryTypeHandler` mapeia `UUID` ↔ `BINARY(16)`.

## Observabilidade

- Actuator: `health`, `info`, `metrics`, `prometheus`.
- Métricas customizadas: `events.requests.latency` (histograma de latência) e `events.requests.errors` (contador de erros).
- `@Timed` nos endpoints do controller para latência por operação.

## Execução local

```bash
docker build -t event-service .
docker run -p 8080:8080 \
  -e PROFILE=dev \
  -e DATABASE_URL=jdbc:mysql://localhost:3306/events \
  -e DATABASE_USERNAME=root \
  -e DATABASE_PASSWORD=secret \
  -e JWT_SECRET=<segredo-compartilhado> \
  -e AWS_ACCESS_KEY_ID=... \
  -e AWS_SECRET_ACCESS_KEY=... \
  event-service
```

## Papel na arquitetura

Ponto de entrada administrativo do catálogo de eventos. A criação de um evento dispara a inicialização do estoque de assentos no ticket-reservation-service via fila `fila-evento-cadastrado.fifo`.
