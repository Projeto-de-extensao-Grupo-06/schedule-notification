# schedule-notification

Microserviço responsável pelo agendamento e envio de notificações de visitas técnicas e de instalação para clientes.

---

## Visão geral

O serviço recebe dados de agendamentos, persiste notificações pendentes e as envia automaticamente no momento correto via email. Foi construído de forma independente, com banco de dados próprio e scheduler dedicado, sem depender de nenhum outro serviço para funcionar.

---

## Arquitetura

O projeto aplica os princípios da Clean Architecture de forma adaptativa, organizando o código em duas grandes camadas: `core` e `infrastructure`.

```
notificationService/
├── core/
│   ├── adapters/          # Interfaces que definem os contratos (ports)
│   ├── application/
│   │   ├── command/       # Objetos de entrada dos casos de uso
│   │   └── usecase/       # Casos de uso da aplicação
│   └── domain/
│       ├── model/         # Entidades e enums do domínio
│       └── shared/
│           ├── exception/ # Exceções de domínio
│           └── vo/        # Value Objects
└── infrastructure/
    ├── di/                # Configuração de injeção de dependência
    ├── email/             # Implementação de envio de email
    ├── persistence/jpa/   # Implementação de persistência com JPA
    ├── rabbit/            # (planejado) Integração com RabbitMQ
    ├── scheduler/         # Job de disparo periódico das notificações
    ├── web/rest/          # Controller REST e tratamento de erros
    └── webhook/           # (planejado) Integração via webhook
```

---

## Camada de domínio

Contém as regras de negócio puras, sem nenhuma dependência de framework.

**`ScheduleNotification`** é a entidade central. Ela encapsula o comportamento do sistema através de métodos como `markAsSent()` e `markAsFailed()`, garantindo que as transições de estado só aconteçam de forma válida.

**Value Objects** garantem que dados inválidos nunca cheguem ao domínio:
- `Email` — valida formato antes de aceitar o valor
- `Phone` — valida tamanho e formata apenas os dígitos
- `SendAt` — calcula a data de envio subtraindo os dias de antecedência da data de início
- `Contato` — agrupa email e telefone em um único conceito

**`NotificationStatus`** define os possíveis estados de uma notificação: `PENDING`, `SENT` e `FAILED`.

**`ScheduleType`** define os tipos de agendamento aceitos: `TECHNICAL_VISIT` e `INSTALL_VISIT`. Agendamentos do tipo `NOTE` são rejeitados pois não possuem cliente a notificar.

---

## Camada de aplicação

Os casos de uso expressam o comportamento do sistema em linguagem de negócio, sem saber nada sobre banco de dados, HTTP ou email.

- **`CreateNotificationUseCase`** — cria uma nova notificação a partir de um agendamento. Rejeita agendamentos do tipo `NOTE` com erro de validação
- **`UpdateNotificationUseCase`** — atualiza os dados de uma notificação existente mantendo seu `id` e `createdAt` originais
- **`CancelNotificationUseCase`** — remove a notificação do banco, permitindo que o mesmo `scheduleId` seja reutilizado futuramente
- **`SendPendingNotificationsUseCase`** — busca todas as notificações pendentes com envio vencido e tenta enviá-las, marcando cada uma como `SENT` ou `FAILED`

Todos os casos de uso dependem exclusivamente de interfaces (`NotificationMutation`, `NotificationQuery`, `NotificationSender`), nunca de implementações concretas.

---

## Camada de infraestrutura

Contém todas as implementações concretas, isoladas do núcleo da aplicação.

**Persistência** — `ScheduleNotificationJpaAdapter` implementa `NotificationMutation` e `NotificationQuery` usando Spring Data JPA. O campo `scheduleId` possui constraint de unicidade, garantindo que cada agendamento tenha no máximo uma notificação ativa. O `ScheduleNotificationMapper` faz a conversão entre a entidade JPA e o modelo de domínio, garantindo que o domínio nunca carregue anotações de banco de dados.

**Email** — `EmailNotificationSender` implementa `NotificationSender` usando JavaMailSender com um template HTML. Para trocar o canal de notificação basta criar uma nova implementação da interface.

**Scheduler** — `NotificationSchedulerJob` executa periodicamente o `SendPendingNotificationsUseCase`, verificando notificações pendentes a cada intervalo configurável.

**Tratamento de erros** — `GlobalExceptionHandler` intercepta as exceções do domínio e retorna respostas HTTP adequadas: `404` para notificações não encontradas, `400` para argumentos inválidos e `409` para conflitos de unicidade.

**Injeção de dependência** — `NotificationBeanConfig` instancia os casos de uso manualmente, injetando as implementações concretas. Isso mantém o núcleo completamente livre de anotações do Spring.

---

## Testes

Os testes unitários cobrem as três camadas do núcleo sem depender de nenhuma infraestrutura.

```
test/
├── domain/
│   ├── model/
│   │   ├── ScheduleNotificationBuilder.java
│   │   └── ScheduleNotificationTest.java
│   └── vo/
│       ├── EmailTest.java
│       ├── PhoneTest.java
│       └── SendAtTest.java
└── usecase/
    └── NotificationUseCaseTest.java
```

Para rodar:
```bash
mvnw.cmd test
```

---

## Como executar

**Pré-requisitos:** Docker e Docker Compose instalados.

Crie um arquivo `.env` na raiz do projeto:

```env
DB_USERNAME=root
DB_PASSWORD=root
EMAIL=seu_email@gmail.com
PASSWORD_EMAIL=sua_senha_de_app
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
```

Suba os serviços:

```bash
docker-compose up --build
```

A aplicação estará disponível em `http://localhost:8080`.

---

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/notifications` | Cria uma nova notificação |
| PUT | `/notifications/{scheduleId}` | Atualiza uma notificação existente |
| DELETE | `/notifications/{scheduleId}` | Remove uma notificação |

---

## Respostas de erro

| Status | Situação |
|---|---|
| 400 | Argumento inválido — email mal formatado, telefone inválido ou tipo `NOTE` |
| 404 | Notificação não encontrada para o `scheduleId` informado |
| 409 | Já existe uma notificação ativa para este agendamento |