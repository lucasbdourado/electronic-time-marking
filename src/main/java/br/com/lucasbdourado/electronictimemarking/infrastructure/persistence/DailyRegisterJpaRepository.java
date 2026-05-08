package br.com.lucasbdourado.electronictimemarking.infrastructure.persistence;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import br.com.lucasbdourado.electronictimemarking.domain.entity.DailyRegister;
import java.time.LocalDate;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRegisterJpaRepository extends JpaRepository<@NonNull DailyRegister, @NonNull Long>
{
	Optional<DailyRegister> findByAuthorAndRegisterDate(Author author, LocalDate registerDate);
}
