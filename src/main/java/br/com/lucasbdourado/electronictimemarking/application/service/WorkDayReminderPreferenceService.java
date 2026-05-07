package br.com.lucasbdourado.electronictimemarking.application.service;

import br.com.lucasbdourado.electronictimemarking.domain.entity.WorkDayReminderPreference;
import br.com.lucasbdourado.electronictimemarking.domain.repository.WorkDayReminderPreferenceRepository;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import org.springframework.stereotype.Service;

@Service
public class WorkDayReminderPreferenceService
{
	private final WorkDayReminderPreferenceRepository repository;

	private final WorkDayReminderScheduler scheduler;

	public WorkDayReminderPreferenceService(WorkDayReminderPreferenceRepository repository,
		WorkDayReminderScheduler scheduler)
	{
		this.repository = repository;
		this.scheduler = scheduler;
	}

	@Transactional
	public void updatePreference(String discordUserId, boolean enabled, LocalTime exitTime)
	{
		if (discordUserId == null || discordUserId.isBlank())
		{
			throw new IllegalArgumentException("discordUserId must not be null or blank");
		}
		if (exitTime == null)
		{
			throw new IllegalArgumentException("exitTime must not be null");
		}

		String normalizedDiscordUserId = discordUserId.trim();

		WorkDayReminderPreference preference = repository.findByDiscordUserId(normalizedDiscordUserId)
			.orElseGet(() -> new WorkDayReminderPreference(normalizedDiscordUserId, enabled, exitTime));

		preference.setEnabled(enabled);
		preference.setExitTime(exitTime);
		repository.save(preference);

		if (enabled)
		{
			scheduler.schedule(normalizedDiscordUserId, exitTime);
		}
		else
		{
			scheduler.unschedule(normalizedDiscordUserId);
		}
	}
}
