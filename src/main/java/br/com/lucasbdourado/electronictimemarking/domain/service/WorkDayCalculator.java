package br.com.lucasbdourado.electronictimemarking.domain.service;

import br.com.lucasbdourado.electronictimemarking.domain.valueobject.WorkPeriod;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WorkDayCalculator
{
	private static final Duration DAILY_GOAL = Duration.ofHours(8).plusMinutes(45);

	public List<LocalTime> normalize(List<LocalTime> times)
	{
		return times.stream().sorted().toList();
	}

	public List<WorkPeriod> buildPeriods(List<LocalTime> times)
	{
		List<WorkPeriod> periods = new ArrayList<>();

		for (int i = 0; i < times.size() - 1; i += 2)
		{
			periods.add(new WorkPeriod(times.get(i), times.get(i + 1)));
		}

		return periods;
	}

	public Duration calculateWorkedTime(List<WorkPeriod> periods)
	{
		return periods.stream().map(WorkPeriod::getDuration).reduce(Duration.ZERO, Duration::plus);
	}

	public Duration calculateRemainingTime(Duration worked)
	{
		return DAILY_GOAL.minus(worked);
	}

	public LocalTime calculateExit(List<LocalTime> times, Duration remaining)
	{
		boolean isOpen = times.size() % 2 != 0;

		if (!isOpen || remaining.isNegative() || remaining.isZero())
		{
			return null;
		}

		LocalTime lastEntry = times.getLast();

		return lastEntry.plus(remaining);
	}

	public WorkStatus determineStatus(List<LocalTime> times, Duration worked, List<WorkPeriod> periods)
	{
		if (times.isEmpty())
		{
			return WorkStatus.EMPTY;
		}

		if (isInvalidClosedDay(times, periods))
		{
			return WorkStatus.INVALID;
		}

		if (times.size() % 2 != 0)
		{
			return WorkStatus.OPEN;
		}

		if (!calculateRemainingTime(worked).isPositive())
		{
			return WorkStatus.COMPLETED;
		}

		return WorkStatus.INCOMPLETE;
	}

	public boolean isInvalidClosedDay(List<LocalTime> times, List<WorkPeriod> periods)
	{
		boolean isEven = times.size() % 2 == 0;
		boolean hasMinimum = times.size() >= 4;

		boolean hasAnchorInterval = hasAnchorInterval(periods);

		return isEven && hasMinimum && !hasAnchorInterval;
	}

	private boolean hasAnchorInterval(List<WorkPeriod> periods)
	{
		for (int i = 0; i < periods.size() - 1; i++)
		{

			WorkPeriod current = periods.get(i);
			WorkPeriod next = periods.get(i + 1);

			long minutes = Duration.between(current.end(), next.start()).toMinutes();

			if (minutes >= 60 && minutes <= 120)
			{
				return true;
			}
		}

		return false;
	}
}
