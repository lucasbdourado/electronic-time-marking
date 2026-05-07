package br.com.lucasbdourado.electronictimemarking.infrastructure.scheduling;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import br.com.lucasbdourado.electronictimemarking.application.service.WorkDayReminderScheduler;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.HexFormat;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QuartzWorkDayReminderScheduler implements WorkDayReminderScheduler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzWorkDayReminderScheduler.class);

	private static final String GROUP = "work-day-reminder";

	private static final int[] MINUTES_BEFORE_EXIT = {10, 5, 0};

	private final Scheduler scheduler;

	public QuartzWorkDayReminderScheduler(Scheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	@Override
	public void schedule(String discordUserId, LocalTime exitTime)
	{
		try
		{
			String id = stableId(discordUserId);
			JobKey jobKey = jobKey(id);
			scheduler.deleteJob(jobKey);

			JobDetail jobDetail = newJob(WorkDayReminderQuartzJob.class)
				.withIdentity(jobKey)
				.usingJobData(WorkDayReminderQuartzJob.DISCORD_USER_ID, discordUserId)
				.storeDurably()
				.build();
			scheduler.addJob(jobDetail, true);

			for (int minutesBeforeExit : MINUTES_BEFORE_EXIT)
			{
				LocalTime fireTime = exitTime.minusMinutes(minutesBeforeExit);
				Trigger trigger = newTrigger()
					.withIdentity(triggerKey(id, minutesBeforeExit))
					.forJob(jobDetail)
					.usingJobData(new JobDataMap(
						java.util.Map.of(WorkDayReminderQuartzJob.MINUTES_BEFORE_EXIT,
							minutesBeforeExit)))
					.withSchedule(cronSchedule(cronExpressionFor(fireTime)))
					.build();
				scheduler.scheduleJob(trigger);
			}

			LOGGER.info("Scheduled work day reminder jobs. discordUserId={}, exitTime={}",
				maskDiscordUserId(discordUserId), exitTime);
		}
		catch (SchedulerException exception)
		{
			throw new IllegalStateException("Failed to schedule work day reminder jobs", exception);
		}
	}

	@Override
	public void unschedule(String discordUserId)
	{
		try
		{
			boolean deleted = scheduler.deleteJob(jobKey(stableId(discordUserId)));
			LOGGER.info("Unscheduled work day reminder jobs. discordUserId={}, deleted={}",
				maskDiscordUserId(discordUserId), deleted);
		}
		catch (SchedulerException exception)
		{
			throw new IllegalStateException("Failed to unschedule work day reminder jobs", exception);
		}
	}

	private JobKey jobKey(String id)
	{
		return JobKey.jobKey("preference-" + id, GROUP);
	}

	private TriggerKey triggerKey(String id, int minutesBeforeExit)
	{
		return TriggerKey.triggerKey("preference-" + id + "-" + minutesBeforeExit + "m", GROUP);
	}

	private String cronExpressionFor(LocalTime time)
	{
		return "%d %d %d * * ?".formatted(time.getSecond(), time.getMinute(), time.getHour());
	}

	private String stableId(String discordUserId)
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(discordUserId.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash, 0, 12);
		}
		catch (NoSuchAlgorithmException exception)
		{
			throw new IllegalStateException("SHA-256 algorithm is not available", exception);
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
