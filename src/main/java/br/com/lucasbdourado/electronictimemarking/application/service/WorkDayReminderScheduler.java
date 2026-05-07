package br.com.lucasbdourado.electronictimemarking.application.service;

import java.time.LocalTime;

public interface WorkDayReminderScheduler
{
	void schedule(String discordUserId, LocalTime exitTime);

	void unschedule(String discordUserId);
}
