package br.com.lucasbdourado.electronictimemarking.domain.valueobject;

import java.time.Duration;
import java.time.LocalTime;

public record WorkPeriod(LocalTime start, LocalTime end)
{
	public WorkPeriod
	{
		if (start == null || end == null)
		{
			throw new IllegalArgumentException("Horarios nao podem ser nulos");
		}

		if (end.isBefore(start))
		{
			throw new IllegalArgumentException("Fim nao pode ser antes do inicio");
		}
	}

	public Duration getDuration()
	{
		return Duration.between(start, end);
	}
}
