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
## Como Executar e Testar a Aplicação
Pré-requisitos
- Docker e Docker Compose instalados.
- Ferramenta HTTP (cURL, Postman, Insomnia) ou Swagger UI.

### 1. Subindo o Ecossistema Completo

Na raiz do repositório:
```
docker compose up -d

# Para verificar os serviços em execução:
docker compose ps
```

Acompanhe os logs da aplicação:
```
docker compose logs -f notification-app
```

### 2. Painéis e Interfaces Disponíveis

- Swagger UI: http://localhost:8080/swagger-ui.html
- Painel do RabbitMQ: http://localhost:15672 (Login: guest / Senha: guest)
- Métricas Actuator / Prometheus: http://localhost:8080/actuator/prometheus

### 3. Cenários de Teste
Cenário A: Envio de Notificação com Sucesso (Status SENT)
```
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "usuario@empresa.com",
    "subject": "Boas-vindas!",
    "content": "Sua conta foi criada com sucesso.",
    "channel": "EMAIL"
  }'
```

Cenário B: Falha na Entrega e Tráfego para DLQ (Status FAILED)
```
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "fail@test.com",
    "subject": "Teste de Resiliência DLQ",
    "content": "Simulação de falha no envio.",
    "channel": "EMAIL"
  }'
```

Cenário C: Validação de Idempotência com Redis
```
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "idempotencia@test.com",
    "subject": "Aviso de Cobrança",
    "content": "Sua fatura está disponível.",
    "channel": "EMAIL"
  }'
```

4. Consultando o Banco de Dados (PostgreSQL)
```
docker exec -it notification-postgres psql -U postgres -d notification_db -c "SELECT id, recipient, status, updated_at FROM tb_notifications ORDER BY created_at DESC;"
```

5. Encerrando o Ambiente
```
docker compose down

# Para remover os volumes persistentes:
docker compose down -v
```
