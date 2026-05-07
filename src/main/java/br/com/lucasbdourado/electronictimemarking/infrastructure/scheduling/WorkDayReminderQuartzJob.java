package br.com.lucasbdourado.electronictimemarking.infrastructure.scheduling;

import br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.producer.WorkDayReminderNotificationPublisher;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record WorkDayReminderQuartzJob(WorkDayReminderNotificationPublisher notificationPublisher) implements Job
{
	public static final String DISCORD_USER_ID = "discordUserId";

	public static final String MINUTES_BEFORE_EXIT = "minutesBeforeExit";

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkDayReminderQuartzJob.class);

	@Override
	public void execute(JobExecutionContext context)
	{
		String discordUserId = context.getMergedJobDataMap().getString(DISCORD_USER_ID);

		int minutesBeforeExit = context.getMergedJobDataMap().getInt(MINUTES_BEFORE_EXIT);

		LOGGER.info("Work day reminder job fired. discordUserId={}, minutesBeforeExit={}",
			maskDiscordUserId(discordUserId), minutesBeforeExit);

		notificationPublisher.publish(discordUserId, reminderMessage(minutesBeforeExit));
	}

	private String reminderMessage(int minutesBeforeExit)
	{
		if (minutesBeforeExit <= 0)
		{
			return "**Lembrete:** seu horario de saida chegou.";
		}
		if (minutesBeforeExit == 1)
		{
			return "**Lembrete:** falta 1 minuto para o seu horario de saida.";
		}

		return "**Lembrete:** faltam " + minutesBeforeExit + " minutos para o seu horario de saida.";
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
