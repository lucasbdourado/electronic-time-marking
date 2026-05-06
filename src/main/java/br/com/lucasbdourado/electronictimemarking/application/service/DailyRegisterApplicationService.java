package br.com.lucasbdourado.electronictimemarking.application.service;

import br.com.lucasbdourado.electronictimemarking.application.dto.RegisterTimeMarkCommand;
import br.com.lucasbdourado.electronictimemarking.application.dto.WorkDayResponse;
import br.com.lucasbdourado.electronictimemarking.application.usecase.CalculateWorkDayUseCase;
import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import br.com.lucasbdourado.electronictimemarking.domain.entity.DailyRegister;
import br.com.lucasbdourado.electronictimemarking.domain.entity.TimeMark;
import br.com.lucasbdourado.electronictimemarking.domain.repository.AuthorRepository;
import br.com.lucasbdourado.electronictimemarking.domain.repository.DailyRegisterRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DailyRegisterApplicationService
{
	private final CalculateWorkDayUseCase calculateWorkDayUseCase;

	private final AuthorRepository authorRepository;

	private final DailyRegisterRepository repository;

	public DailyRegisterApplicationService(AuthorRepository authorRepository,
		DailyRegisterRepository repository, CalculateWorkDayUseCase calculateWorkDayUseCase)
	{
		this.authorRepository = authorRepository;
		this.repository = repository;
		this.calculateWorkDayUseCase = calculateWorkDayUseCase;
	}

	@Transactional
	public WorkDayResponse create(RegisterTimeMarkCommand command)
	{
		if (command.author() == null)
		{
			throw new RuntimeException("O author da mensagem nao pode ser nulo");
		}

		if (command.type() == null)
		{
			throw new RuntimeException("O tipo do registro nao pode ser nulo");
		}

		if (command.registeredAt() == null)
		{
			throw new RuntimeException("O horario do registro nao pode ser nulo");
		}

		Author author = findOrCreateAuthor(command.author());

		DailyRegister dailyRegister = findOrCreateDailyRegister(author,
			command.registeredAt().toLocalDate());

		TimeMark timeMark = new TimeMark();
		timeMark.setType(command.type());
		timeMark.setMarkedAt(command.registeredAt().toLocalTime());

		dailyRegister.addMark(timeMark);

		repository.save(dailyRegister);

		List<LocalTime> markListTime = dailyRegister.getMarks().stream().map(TimeMark::getMarkedAt)
			.toList();

		return calculateWorkDayUseCase.process(markListTime);
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
