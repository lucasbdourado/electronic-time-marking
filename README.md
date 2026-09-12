# Electronic Time Marking

A time tracking system built with **Java 21** and **Spring Boot**.

The application processes employee clock-in and clock-out events, calculates daily working hours, and manages workday reminders.

It works together with the [Discord Reminder Bot](https://github.com/lucasbdourado/discord-reminder-bot), using RabbitMQ for communication between the applications.

## Tech Stack

* Java 21
* Spring Boot
* RabbitMQ
* Quartz
* Spring Data JPA
* MySQL
* Docker Compose

## Architecture

```text
Discord
   ↓
Discord Reminder Bot
   ↓
RabbitMQ
   ↓
Electronic Time Marking
   ↓
MySQL
```

The Discord bot receives user commands and publishes time marking events through RabbitMQ. This service processes the events, calculates the workday and stores the records.

Quartz is used to schedule workday reminders, which are sent back to the Discord bot through RabbitMQ.

## How to Run

### 1. Start the infrastructure

```bash
docker compose up -d
```

This starts:

* MySQL
* RabbitMQ

### 2. Run Electronic Time Marking

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

### 3. Run the Discord Bot

Clone the associated project:

```bash
git clone https://github.com/lucasbdourado/discord-reminder-bot.git
cd discord-reminder-bot
```

Configure your Discord token:

```bash
DISCORD_TOKEN=your_discord_bot_token
```

Then start the application:

```bash
./mvnw spring-boot:run
```

### Discord Commands

```text
in
in h
in home
out
```

`in` registers the beginning of a work period.

`in home` registers the beginning of a remote work period.

`out` registers the end of a work period.

## Related Project

[Discord Reminder Bot](https://github.com/lucasbdourado/discord-reminder-bot) — Discord interface responsible for receiving time tracking commands and communicating with this service through RabbitMQ.
