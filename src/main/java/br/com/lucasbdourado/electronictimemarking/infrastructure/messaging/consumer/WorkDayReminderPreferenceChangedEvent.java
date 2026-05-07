package br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.consumer;

import java.time.LocalTime;

public record WorkDayReminderPreferenceChangedEvent(String discordUserId, boolean enabled,
	LocalTime exitTime)
{
}
