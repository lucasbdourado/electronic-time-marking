package br.com.lucasbdourado.electronictimemarking.domain.repository;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import br.com.lucasbdourado.electronictimemarking.domain.entity.DailyRegister;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyRegisterRepository extends JpaRepository<@NonNull DailyRegister, @NonNull Long>
{
	Optional<DailyRegister> findByAuthorAndRegisterDate(Author author, LocalDate registerDate);
}
