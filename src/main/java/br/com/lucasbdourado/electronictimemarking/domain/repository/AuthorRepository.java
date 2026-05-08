package br.com.lucasbdourado.electronictimemarking.domain.repository;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import java.util.Optional;

public interface AuthorRepository
{
	Optional<Author> findByCode(String code);

	Optional<Author> findByDiscordId(Long discordId);

	Author save(Author author);
}
