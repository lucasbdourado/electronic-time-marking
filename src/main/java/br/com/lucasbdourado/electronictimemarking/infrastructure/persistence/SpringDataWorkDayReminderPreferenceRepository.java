package br.com.lucasbdourado.electronictimemarking.infrastructure.persistence;

import br.com.lucasbdourado.electronictimemarking.domain.entity.WorkDayReminderPreference;
import br.com.lucasbdourado.electronictimemarking.domain.repository.WorkDayReminderPreferenceRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringDataWorkDayReminderPreferenceRepository implements WorkDayReminderPreferenceRepository
{
	private final WorkDayReminderPreferenceJpaRepository repository;

	public SpringDataWorkDayReminderPreferenceRepository(
		WorkDayReminderPreferenceJpaRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Optional<WorkDayReminderPreference> findByDiscordUserId(String discordUserId)
	{
		return repository.findByDiscordUserId(discordUserId);
	}

	@Override
	public WorkDayReminderPreference save(WorkDayReminderPreference preference)
	{
		return repository.save(preference);
	}
}
