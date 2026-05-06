package br.com.lucasbdourado.electronictimemarking.application.usecase;

import br.com.lucasbdourado.electronictimemarking.application.dto.WorkDayResponse;
import br.com.lucasbdourado.electronictimemarking.domain.service.WorkDayCalculator;
import br.com.lucasbdourado.electronictimemarking.domain.service.WorkStatus;
import br.com.lucasbdourado.electronictimemarking.domain.valueobject.WorkPeriod;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculateWorkDayUseCase
{
	private final WorkDayCalculator calculator;

	public CalculateWorkDayUseCase(WorkDayCalculator calculator)
	{
		this.calculator = calculator;
	}

	public WorkDayResponse process(List<?> timeList)
	{
		List<LocalTime> times = parseTimes(timeList);

		List<LocalTime> normalized = calculator.normalize(times);

		List<WorkPeriod> periods = calculator.buildPeriods(normalized);

		Duration worked = calculator.calculateWorkedTime(periods);

		Duration remaining = calculator.calculateRemainingTime(worked);

		LocalTime exit = calculator.calculateExit(normalized, remaining);

		WorkStatus status = calculator.determineStatus(normalized, worked, periods);

		boolean invalid = calculator.isInvalidClosedDay(normalized, periods);

		WorkDayResponse response = new WorkDayResponse();

		response.setTimes(normalized);
		response.setWorkedMinutes(worked.toMinutes());
		response.setRemainingMinutes(remaining.toMinutes());
		response.setExitTime(exit);
		response.setStatus(status.name());
		response.setInvalid(invalid);

		return response;
	}

	private List<LocalTime> parseTimes(List<?> timeList)
	{
		return timeList.stream().map(this::parseTime).toList();
	}

	private LocalTime parseTime(Object time)
	{
		if (time instanceof LocalTime localTime)
		{
			return localTime;
		}

		if (time instanceof String stringTime)
		{
			return LocalTime.parse(stringTime);
		}

		throw new IllegalArgumentException("Time must be a LocalTime or String");
	}
}
