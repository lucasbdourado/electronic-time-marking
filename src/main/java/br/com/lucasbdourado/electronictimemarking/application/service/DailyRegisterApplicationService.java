package br.com.lucasbdourado.electronictimemarking.application.service;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import br.com.lucasbdourado.electronictimemarking.domain.entity.DailyRegister;
import br.com.lucasbdourado.electronictimemarking.domain.entity.TimeMark;
import br.com.lucasbdourado.electronictimemarking.domain.repository.AuthorRepository;
import br.com.lucasbdourado.electronictimemarking.domain.repository.DailyRegisterRepository;
import br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.consumer.TimeMarkRegisteredEvent;
import jakarta.transaction.Transactional;
import java.sql.Time;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyRegisterApplicationService
{

	private final AuthorRepository authorRepository;

	private final DailyRegisterRepository repository;

	public DailyRegisterApplicationService(AuthorRepository authorRepository, DailyRegisterRepository repository)
	{
		this.authorRepository = authorRepository;
		this.repository = repository;
	}

	@Transactional
	public void create(TimeMarkRegisteredEvent event)
	{
		if (event.author() == null)
		{
			throw new RuntimeException("O author da mensagem não pode ser nulo");
		}

		if (event.type() == null)
		{
			throw new RuntimeException("O tipo do registro não pode ser nulo");
		}

		if (event.registeredAt() == null)
		{
			throw new RuntimeException("O horário do registro não pode ser nulo");
		}

		Author author = findOrCreateAuthor(event.author());

		DailyRegister dailyRegister = findOrCreateDailyRegister(author,
			event.registeredAt().toLocalDate());

		TimeMark timeMark = new TimeMark();
		timeMark.setType(event.type());
		timeMark.setMarkedAt(event.registeredAt().toLocalTime());

		dailyRegister.addMark(timeMark);

		repository.save(dailyRegister);
	}

	private Author findOrCreateAuthor(Author eventAuthor)
	{
		return authorRepository.findByCode(eventAuthor.getCode()).map(author -> {
			author.setDiscordId(eventAuthor.getDiscordId());
			author.setCode(eventAuthor.getCode());
			author.setName(eventAuthor.getName());
			return author;
		}).orElseGet(() -> authorRepository.save(eventAuthor));
	}

	private DailyRegister findOrCreateDailyRegister(Author author, LocalDate registerDate)
	{
		return repository.findByAuthorAndRegisterDate(author, registerDate).orElseGet(() -> {
			DailyRegister dailyRegister = new DailyRegister();
			dailyRegister.setAuthor(author);
			dailyRegister.setRegisterDate(registerDate);
			return dailyRegister;
		});
	}
}
