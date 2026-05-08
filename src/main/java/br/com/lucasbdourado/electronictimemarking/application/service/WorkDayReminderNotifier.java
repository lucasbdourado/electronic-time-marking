package br.com.lucasbdourado.electronictimemarking.application.service;

public interface WorkDayReminderNotifier
{
	void notify(String discordUserId, String message);
}
