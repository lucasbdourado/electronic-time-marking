package br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.producer;

public record WorkDayReminderNotificationEvent(String discordUserId, String message)
{
}
