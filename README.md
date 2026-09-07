# Notification Service

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-AMQP-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-Lock-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Prometheus](https://img.shields.io/badge/Prometheus-Metrics-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)](https://prometheus.io/)

Pequeno projeto de estudo para a construção de um microsserviço assíncrono e resiliente para processamento e envio de notificações, desenvolvido com **Spring Boot 4.1.1**, **RabbitMQ**, **PostgreSQL** e **Redis**.

---

## Arquitetura da Solução

O serviço adota o padrão **Event-Driven Architecture (EDA)** combinando o padrão **Pass-by-Reference** com trava distribuída de idempotência para garantir resiliência e consistência de dados.

### Principais Decisões Arquiteturais

- **Pass-by-Reference Pattern:** O broker de mensageria trafega apenas o `UUID` da notificação, reduzindo a sobrecarga na fila e evitando inconsistências de estado.
- **Idempotência com Redis:** Trava distribuída criada via `setIfAbsent` (`SETNX`) com TTL configurável, impedindo o reprocessamento de mensagens duplicadas.
- **Dead Letter Queue (DLQ):** Redirecionamento automático e atualização para `FAILED` em caso de falhas irrecuperáveis de envio.
- **Observabilidade com Micrometer:** Contadores customizados de métricas expostos nativamente para o **Prometheus**.

---

## Variáveis de Ambiente

| Variável | Descrição | Valor Padrão |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | URL de conexão do PostgreSQL | `jdbc:postgresql://localhost:5432/notification_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do PostgreSQL | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do PostgreSQL | `postgres` |
| `SPRING_RABBITMQ_HOST` | Host do RabbitMQ | `localhost` |
| `SPRING_RABBITMQ_PORT` | Porta AMQP do RabbitMQ | `5672` |
| `SPRING_DATA_REDIS_HOST` | Host do Redis | `localhost` |
| `SPRING_DATA_REDIS_PORT` | Porta do Redis | `6379` |

---

## Execução com Docker

Um arquivo `docker-compose.yml` está disponível para subir toda a infraestrutura necessária (PostgreSQL, RabbitMQ e Redis) com um único comando.

### Subindo a Infraestrutura

```bash
docker compose up -d

# Para verificar os serviços em execução:
docker compose ps
