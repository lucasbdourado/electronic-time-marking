package br.com.lucasbdourado.electronictimemarking.repository;

import br.com.lucasbdourado.electronictimemarking.domain.TimeMark;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeMarkRepository extends JpaRepository<@NonNull TimeMark,@NonNull Long> {
}
