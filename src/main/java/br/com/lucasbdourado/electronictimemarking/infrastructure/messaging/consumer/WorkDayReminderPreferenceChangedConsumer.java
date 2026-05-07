package br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.consumer;

import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.WORKDAY_REMINDER_PREFERENCE_CHANGED_QUEUE;

import br.com.lucasbdourado.electronictimemarking.application.service.WorkDayReminderPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkDayReminderPreferenceChangedConsumer
{
	private static final Logger LOGGER =
		LoggerFactory.getLogger(WorkDayReminderPreferenceChangedConsumer.class);

	private final WorkDayReminderPreferenceService service;

	public WorkDayReminderPreferenceChangedConsumer(WorkDayReminderPreferenceService service)
	{
		this.service = service;
	}

	@RabbitListener(queues = WORKDAY_REMINDER_PREFERENCE_CHANGED_QUEUE)
	public void consume(WorkDayReminderPreferenceChangedEvent event)
	{
		if (event == null || event.discordUserId() == null || event.discordUserId().isBlank())
		{
			LOGGER.warn("Ignoring work day reminder preference event with missing discord user id");
			return;
		}
		if (event.exitTime() == null)
		{
			LOGGER.warn("Ignoring work day reminder preference event with missing exit time. discordUserId={}",
				maskDiscordUserId(event.discordUserId()));
			return;
		}

		try
		{
			service.updatePreference(event.discordUserId(), event.enabled(), event.exitTime());
			LOGGER.info("Work day reminder preference updated. discordUserId={}, enabled={}, exitTime={}",
				maskDiscordUserId(event.discordUserId()), event.enabled(), event.exitTime());
		}
		catch (RuntimeException exception)
		{
			LOGGER.error("Failed to update work day reminder preference. discordUserId={}",
				maskDiscordUserId(event.discordUserId()), exception);
			throw exception;
		}
	}

	private String maskDiscordUserId(String discordUserId)
	{
		String trimmed = discordUserId.trim();
		if (trimmed.length() <= 4)
		{
			return "****";
		}
		return "****" + trimmed.substring(trimmed.length() - 4);
	}
}
