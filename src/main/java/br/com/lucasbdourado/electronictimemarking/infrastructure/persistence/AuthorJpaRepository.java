package br.com.lucasbdourado.electronictimemarking.infrastructure.persistence;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<@NonNull Author, @NonNull Long>
{
	Optional<Author> findByCode(String code);

	Optional<Author> findByDiscordId(Long discordId);
}
