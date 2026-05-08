package br.com.lucasbdourado.electronictimemarking.application.service;

import br.com.lucasbdourado.electronictimemarking.application.dto.CalculateWorkDayCommand;
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
import java.time.LocalDateTime;
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
		if (command.authorDiscordId() == null)
		{
			throw new IllegalArgumentException("O discordId do author da mensagem nao pode ser nulo");
		}

		if (command.type() == null)
		{
			throw new IllegalArgumentException("O tipo do registro nao pode ser nulo");
		}

		if (command.registeredAt() == null)
		{
			throw new IllegalArgumentException("O horario do registro nao pode ser nulo");
		}

		Author author = findOrCreateAuthor(command);

		DailyRegister dailyRegister = findOrCreateDailyRegister(author,
			command.registeredAt().toLocalDate());

		LocalDateTime localDateTime = command.registeredAt();
		TimeMark timeMark = new TimeMark(command.type(), localDateTime.toLocalTime());

		dailyRegister.addMark(timeMark);

		repository.save(dailyRegister);

		if (!dailyRegister.hasEnoughMarksToCalculate())
		{
			return new WorkDayResponse();
		}

		List<LocalTime> markListTime = dailyRegister.getMarks().stream().map(TimeMark::getMarkedAt)
			.toList();

		return calculateWorkDayUseCase.process(
			new CalculateWorkDayCommand(dailyRegister.getRegisterDate(), markListTime));
	}

	private Author findOrCreateAuthor(RegisterTimeMarkCommand command)
	{
		return authorRepository.findByDiscordId(command.authorDiscordId()).map(author -> {
			author.updateProfile(command.authorCode(), command.authorName());
			return author;
		}).orElseGet(() -> authorRepository.save(
			new Author(command.authorDiscordId(), command.authorCode(), command.authorName())));
	}

	private DailyRegister findOrCreateDailyRegister(Author author, LocalDate registerDate)
	{
		return repository.findByAuthorAndRegisterDate(author, registerDate)
			.orElseGet(() -> new DailyRegister(author, registerDate));
	}
}
