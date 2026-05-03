package br.com.lucasbdourado.electronictimemarking.repository;

import br.com.lucasbdourado.electronictimemarking.domain.Author;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<@NonNull Author, @NonNull Long> {
    Optional<Author> findByCode(String code);

    Optional<Author> findByDiscordId(Long discordId);
}
