package br.com.lucasbdourado.electronictimemarking.infrastructure.persistence;

import br.com.lucasbdourado.electronictimemarking.domain.entity.TimeMark;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeMarkJpaRepository extends JpaRepository<@NonNull TimeMark, @NonNull Long>
{
}
