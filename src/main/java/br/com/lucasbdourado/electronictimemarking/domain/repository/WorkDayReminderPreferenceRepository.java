package br.com.lucasbdourado.electronictimemarking.domain.repository;

import br.com.lucasbdourado.electronictimemarking.domain.entity.WorkDayReminderPreference;
import java.util.Optional;

public interface WorkDayReminderPreferenceRepository
{
	Optional<WorkDayReminderPreference> findByDiscordUserId(String discordUserId);

	WorkDayReminderPreference save(WorkDayReminderPreference preference);
}
