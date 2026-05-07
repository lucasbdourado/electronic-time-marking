package br.com.lucasbdourado.electronictimemarking.domain.repository;

import br.com.lucasbdourado.electronictimemarking.domain.entity.WorkDayReminderPreference;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDayReminderPreferenceRepository
	extends JpaRepository<@NonNull WorkDayReminderPreference, @NonNull Long>
{
	Optional<WorkDayReminderPreference> findByDiscordUserId(String discordUserId);
}
