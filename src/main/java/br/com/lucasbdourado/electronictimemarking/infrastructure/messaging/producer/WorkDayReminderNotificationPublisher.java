package br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.producer;

import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.REMINDER_EVENTS_EXCHANGE;
import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.WORKDAY_REMINDER_NOTIFICATION_ROUTING_KEY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkDayReminderNotificationPublisher
{
	private static final Logger LOGGER =
		LoggerFactory.getLogger(WorkDayReminderNotificationPublisher.class);

	private final RabbitTemplate rabbitTemplate;

	public WorkDayReminderNotificationPublisher(RabbitTemplate rabbitTemplate)
	{
		this.rabbitTemplate = rabbitTemplate;
	}

	public void publish(String discordUserId, String message)
	{
		WorkDayReminderNotificationEvent event =
			new WorkDayReminderNotificationEvent(discordUserId, message);

		rabbitTemplate.convertAndSend(REMINDER_EVENTS_EXCHANGE,
			WORKDAY_REMINDER_NOTIFICATION_ROUTING_KEY, event);
		LOGGER.info("Published work day reminder notification. discordUserId={}",
			maskDiscordUserId(discordUserId));
	}

	private String maskDiscordUserId(String discordUserId)
	{
		if (discordUserId == null)
		{
			return "****";
		}
		String trimmed = discordUserId.trim();
		if (trimmed.length() <= 4)
		{
			return "****";
		}
		return "****" + trimmed.substring(trimmed.length() - 4);
	}
}
