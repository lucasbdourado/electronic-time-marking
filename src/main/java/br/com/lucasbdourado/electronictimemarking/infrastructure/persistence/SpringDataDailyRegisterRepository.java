package br.com.lucasbdourado.electronictimemarking.infrastructure.persistence;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import br.com.lucasbdourado.electronictimemarking.domain.entity.DailyRegister;
import br.com.lucasbdourado.electronictimemarking.domain.repository.DailyRegisterRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringDataDailyRegisterRepository implements DailyRegisterRepository
{
	private final DailyRegisterJpaRepository repository;

	public SpringDataDailyRegisterRepository(DailyRegisterJpaRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Optional<DailyRegister> findByAuthorAndRegisterDate(Author author, LocalDate registerDate)
	{
		return repository.findByAuthorAndRegisterDate(author, registerDate);
	}

	@Override
	public DailyRegister save(DailyRegister dailyRegister)
	{
		return repository.save(dailyRegister);
	}
}
