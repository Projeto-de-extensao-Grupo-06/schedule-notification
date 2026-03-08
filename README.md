Aqui está:

---

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
    ├── web/rest/          # Controller REST
    └── webhook/           # (planejado) Integração via webhook
```

---

## Camada de domínio

Contém as regras de negócio puras, sem nenhuma dependência de framework.

**`ScheduleNotification`** é a entidade central. Ela encapsula o comportamento do sistema através de métodos como `cancel()`, `markAsSent()` e `markAsFailed()`, garantindo que as transições de estado só aconteçam de forma válida.

**Value Objects** garantem que dados inválidos nunca cheguem ao domínio:
- `Email` — valida formato antes de aceitar o valor
- `Phone` — valida tamanho e formata apenas os dígitos
- `SendAt` — calcula a data de envio subtraindo os dias de antecedência da data de início
- `Contato` — agrupa email e telefone em um único conceito

**`NotificationStatus`** define os possíveis estados de uma notificação: `PENDING`, `SENT`, `FAILED` e `CANCELLED`.

---

## Camada de aplicação

Os casos de uso expressam o comportamento do sistema em linguagem de negócio, sem saber nada sobre banco de dados, HTTP ou email.

- **`CreateNotificationUseCase`** — cria uma nova notificação a partir de um agendamento
- **`UpdateNotificationUseCase`** — atualiza os dados de uma notificação existente
- **`CancelNotificationUseCase`** — cancela uma notificação pendente
- **`SendPendingNotificationsUseCase`** — busca todas as notificações pendentes com envio vencido e tenta enviá-las, marcando cada uma como `SENT` ou `FAILED`

Todos os casos de uso dependem exclusivamente de interfaces (`NotificationMutation`, `NotificationQuery`, `NotificationSender`), nunca de implementações concretas.

---

## Camada de infraestrutura

Contém todas as implementações concretas, isoladas do núcleo da aplicação.

**Persistência** — `ScheduleNotificationJpaAdapter` implementa `NotificationMutation` e `NotificationQuery` usando Spring Data JPA. O `ScheduleNotificationMapper` faz a conversão entre a entidade JPA e o modelo de domínio, garantindo que o domínio nunca carregue anotações de banco de dados.

**Email** — `EmailNotificationSender` implementa `NotificationSender` usando JavaMailSender com um template HTML. Para trocar o canal de notificação basta criar uma nova implementação da interface.

**Scheduler** — `NotificationSchedulerJob` executa periodicamente o `SendPendingNotificationsUseCase`, verificando notificações pendentes a cada intervalo configurável.

**Injeção de dependência** — `NotificationBeanConfig` instancia os casos de uso manualmente, injetando as implementações concretas. Isso mantém o núcleo completamente livre de anotações do Spring.

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
| DELETE | `/notifications/{scheduleId}` | Cancela uma notificação |