package br.com.lucasbdourado.electronictimemarking.infrastructure.persistence;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import br.com.lucasbdourado.electronictimemarking.domain.repository.AuthorRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringDataAuthorRepository implements AuthorRepository
{
	private final AuthorJpaRepository repository;

	public SpringDataAuthorRepository(AuthorJpaRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Optional<Author> findByCode(String code)
	{
		return repository.findByCode(code);
	}

	@Override
	public Optional<Author> findByDiscordId(Long discordId)
	{
		return repository.findByDiscordId(discordId);
	}

	@Override
	public Author save(Author author)
	{
		return repository.save(author);
	}
}
