package br.com.lucasbdourado.electronictimemarking.infrastructure.persistence;

import br.com.lucasbdourado.electronictimemarking.domain.entity.WorkDayReminderPreference;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkDayReminderPreferenceJpaRepository extends JpaRepository<@NonNull WorkDayReminderPreference, @NonNull Long>
{
	Optional<WorkDayReminderPreference> findByDiscordUserId(String discordUserId);
}
