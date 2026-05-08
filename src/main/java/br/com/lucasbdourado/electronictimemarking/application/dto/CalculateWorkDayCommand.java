package br.com.lucasbdourado.electronictimemarking.application.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CalculateWorkDayCommand(LocalDate date, List<LocalTime> times)
{
	public CalculateWorkDayCommand
	{
		if (date == null)
		{
			throw new IllegalArgumentException("A data da jornada nao pode ser nula");
		}

		if (times == null)
		{
			throw new IllegalArgumentException("A lista de horarios nao pode ser nula");
		}
	}
}
