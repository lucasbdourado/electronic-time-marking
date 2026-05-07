package br.com.lucasbdourado.electronictimemarking.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.lucasbdourado.electronictimemarking.application.dto.WorkDayResponse;
import br.com.lucasbdourado.electronictimemarking.domain.service.WorkDayCalculator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class CalculateWorkDayUseCaseTest
{
	private static final LocalDate WORK_DAY = LocalDate.of(2026, 5, 6);

	private final CalculateWorkDayUseCase useCase = new CalculateWorkDayUseCase(new WorkDayCalculator());

	@Test
	void shouldCalculateOpenWorkDay()
	{
		WorkDayResponse response = useCase.process(WORK_DAY, List.of(
			LocalTime.of(8, 0),
			LocalTime.of(12, 0),
			LocalTime.of(13, 0)
		));

		assertEquals(List.of(LocalTime.of(8, 0), LocalTime.of(12, 0), LocalTime.of(13, 0)), response.getTimes());
		assertEquals(240, response.getWorkedMinutes());
		assertEquals(285, response.getRemainingMinutes());
		assertEquals(LocalTime.of(17, 45), response.getExitTime());
		assertEquals("OPEN", response.getStatus());
		assertFalse(response.isInvalid());
	}

	@Test
	void shouldCalculateOpenWorkDayFromStringTimes()
	{
		WorkDayResponse response = useCase.process(WORK_DAY, List.of("08:00", "12:00", "13:00"));

		assertEquals(List.of(LocalTime.of(8, 0), LocalTime.of(12, 0), LocalTime.of(13, 0)), response.getTimes());
		assertEquals(240, response.getWorkedMinutes());
		assertEquals(285, response.getRemainingMinutes());
		assertEquals(LocalTime.of(17, 45), response.getExitTime());
		assertEquals("OPEN", response.getStatus());
		assertFalse(response.isInvalid());
	}

	@Test
	void shouldCalculateCompletedClosedWorkDay()
	{
		WorkDayResponse response = useCase.process(WORK_DAY, List.of(
			LocalTime.of(8, 0),
			LocalTime.of(12, 0),
			LocalTime.of(13, 0),
			LocalTime.of(17, 45)
		));

		assertEquals(525, response.getWorkedMinutes());
		assertEquals(0, response.getRemainingMinutes());
		assertNull(response.getExitTime());
		assertEquals("COMPLETED", response.getStatus());
		assertFalse(response.isInvalid());
	}

	@Test
	void shouldFlagInvalidClosedDayWithoutLunchAnchor()
	{
		WorkDayResponse response = useCase.process(WORK_DAY, List.of(
			LocalTime.of(8, 0),
			LocalTime.of(10, 0),
			LocalTime.of(10, 30),
			LocalTime.of(17, 0)
		));

		assertTrue(response.isInvalid());
		assertEquals("INVALID", response.getStatus());
	}

	@Test
	void shouldRejectUnsupportedTimeType()
	{
		assertThrows(IllegalArgumentException.class, () -> useCase.process(WORK_DAY, List.of(8)));
	}
}
