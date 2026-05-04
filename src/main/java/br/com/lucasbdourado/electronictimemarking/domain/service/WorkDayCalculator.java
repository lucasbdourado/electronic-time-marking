package br.com.lucasbdourado.electronictimemarking.domain.service;

import br.com.lucasbdourado.electronictimemarking.domain.valueobject.WorkInterval;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WorkDayCalculator
{
	private static final Duration DAILY_GOAL = Duration.ofHours(8).plusMinutes(45);

	private List<LocalTime> parseTimeMarks(List<String> times)
	{
		return times.stream().map(LocalTime::parse).toList();
	}

	private List<LocalTime> sortTimeMarks(List<LocalTime> times)
	{
		return times.stream().sorted().toList();
	}

	private List<WorkInterval> buildIntervals(List<LocalTime> times)
	{
		List<WorkInterval> intervals = new ArrayList<>();

		for (int i = 0; i < times.size() - 1; i += 2)
		{
			intervals.add(new WorkInterval(times.get(i), times.get(i + 1)));
		}

		return intervals;
	}

	private Duration calculateWorkedTime(List<WorkInterval> intervals)
	{
		return intervals.stream().map(WorkInterval::getDuration).reduce(Duration.ZERO, Duration::plus);
	}

	private Duration calculateRemainingTime(Duration worked)
	{
		return DAILY_GOAL.minus(worked);
	}

	private LocalTime calculateExit(List<LocalTime> times, Duration remaining)
	{
		boolean isOpen = times.size() % 2 != 0;

		if (!isOpen || remaining.isNegative() || remaining.isZero())
		{
			return null;
		}

		LocalTime lastEntry = times.getLast();

		return lastEntry.plus(remaining);
	}

	private WorkStatus determineStatus(List<LocalTime> times, Duration worked, List<WorkInterval> intervals)
	{
		boolean isOpen = times.size() % 2 != 0;

		if (isOpen)
		{
			return WorkStatus.IN_PROGRESS;
		}

		if (worked.compareTo(DAILY_GOAL) >= 0)
		{
			return WorkStatus.COMPLETE;
		}

		return WorkStatus.INCOMPLETE;
	}

	private boolean isInvalidClosedDay(List<LocalTime> times, List<WorkInterval> intervals)
	{
		boolean isEven = times.size() % 2 == 0;
		boolean hasMinimum = times.size() >= 4;

		boolean hasAnchorInterval = hasAnchorInterval(intervals);

		return isEven && hasMinimum && !hasAnchorInterval;
	}

	private boolean hasAnchorInterval(List<WorkInterval> intervals)
	{
		for (int i = 0; i < intervals.size() - 1; i++)
		{

			WorkInterval current = intervals.get(i);
			WorkInterval next = intervals.get(i + 1);

			long minutes = Duration.between(current.end(), next.start()).toMinutes();

			if (minutes >= 60 && minutes <= 120)
			{
				return true;
			}
		}

		return false;
	}

	private enum WorkStatus
	{
		IN_PROGRESS, COMPLETE, INCOMPLETE
	}
}
