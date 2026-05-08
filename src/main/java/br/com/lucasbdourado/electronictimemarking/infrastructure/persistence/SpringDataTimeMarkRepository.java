package br.com.lucasbdourado.electronictimemarking.infrastructure.persistence;

import br.com.lucasbdourado.electronictimemarking.domain.entity.TimeMark;
import br.com.lucasbdourado.electronictimemarking.domain.repository.TimeMarkRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SpringDataTimeMarkRepository implements TimeMarkRepository
{
	private final TimeMarkJpaRepository repository;

	public SpringDataTimeMarkRepository(TimeMarkJpaRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public TimeMark save(TimeMark timeMark)
	{
		return repository.save(timeMark);
	}
}
