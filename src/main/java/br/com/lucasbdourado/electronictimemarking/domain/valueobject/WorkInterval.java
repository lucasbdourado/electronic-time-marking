package br.com.lucasbdourado.electronictimemarking.domain.valueobject;

import java.time.Duration;
import java.time.LocalTime;

public record WorkInterval(LocalTime start, LocalTime end)
{
	public WorkInterval
	{
		if (start == null || end == null)
		{
			throw new IllegalArgumentException("Horários não podem ser nulos");
		}

		if (end.isBefore(start))
		{
			throw new IllegalArgumentException("Fim não pode ser antes do início");
		}

	}

	public Duration getDuration()
	{
		return Duration.between(start, end);
	}
}
