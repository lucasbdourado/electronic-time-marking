package br.com.lucasbdourado.electronictimemarking.service;

import br.com.lucasbdourado.electronictimemarking.consumer.TimeMarkRegisteredEvent;
import br.com.lucasbdourado.electronictimemarking.domain.Author;
import br.com.lucasbdourado.electronictimemarking.domain.DailyRegister;
import br.com.lucasbdourado.electronictimemarking.domain.TimeMark;
import br.com.lucasbdourado.electronictimemarking.repository.AuthorRepository;
import br.com.lucasbdourado.electronictimemarking.repository.DailyRegisterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyRegisterService {

    private final AuthorRepository authorRepository;

    private final DailyRegisterRepository repository;

    public DailyRegisterService(AuthorRepository authorRepository, DailyRegisterRepository repository) {
        this.authorRepository = authorRepository;
        this.repository = repository;
    }

    @Transactional
    public void create(TimeMarkRegisteredEvent timeMarkRegisteredEvent)
    {
        if(timeMarkRegisteredEvent.author() == null)
        {
            throw new RuntimeException("O author da mensagem não pode ser nulo");
        }

        if(timeMarkRegisteredEvent.type() == null)
        {
            throw new RuntimeException("O tipo do registro não pode ser nulo");
        }

        if(timeMarkRegisteredEvent.registeredAt() == null)
        {
            throw new RuntimeException("O horário do registro não pode ser nulo");
        }

        Author author = findOrCreateAuthor(timeMarkRegisteredEvent.author());

        DailyRegister dailyRegister = findOrCreateDailyRegister(
                author,
                timeMarkRegisteredEvent.registeredAt().toLocalDate()
        );

        TimeMark timeMark = new TimeMark();
        timeMark.setType(timeMarkRegisteredEvent.type());
        timeMark.setMarkedAt(timeMarkRegisteredEvent.registeredAt().toLocalTime());

        dailyRegister.addMark(timeMark);

        repository.save(dailyRegister);
    }

    private Author findOrCreateAuthor(Author eventAuthor)
    {
        return authorRepository.findByCode(eventAuthor.getCode())
                .map(author -> {
                    author.setDiscordId(eventAuthor.getDiscordId());
                    author.setCode(eventAuthor.getCode());
                    author.setName(eventAuthor.getName());
                    return author;
                })
                .orElseGet(() -> authorRepository.save(eventAuthor));
    }

    private DailyRegister findOrCreateDailyRegister(Author author, LocalDate registerDate)
    {
        return repository.findByAuthorAndRegisterDate(author, registerDate)
                .orElseGet(() -> {
                    DailyRegister dailyRegister = new DailyRegister();
                    dailyRegister.setAuthor(author);
                    dailyRegister.setRegisterDate(registerDate);
                    return dailyRegister;
                });
    }
}
