package br.com.lucasbdourado.electronictimemarking.infrastructure.scheduling;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkDayReminderQuartzJob implements Job
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
