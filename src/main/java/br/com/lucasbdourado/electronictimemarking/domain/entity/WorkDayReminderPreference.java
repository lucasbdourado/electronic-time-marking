package br.com.lucasbdourado.electronictimemarking.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;

@Entity
public class WorkDayReminderPreference
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String discordUserId;

	@Column(nullable = false)
	private boolean enabled;

	@Column(nullable = false)
	private LocalTime exitTime;

	protected WorkDayReminderPreference()
	{
	}

	public WorkDayReminderPreference(String discordUserId, boolean enabled, LocalTime exitTime)
	{
		this.discordUserId = discordUserId;
		this.enabled = enabled;
		this.exitTime = exitTime;
	}

	public Long getId()
	{
		return id;
	}

	public String getDiscordUserId()
	{
		return discordUserId;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public LocalTime getExitTime()
	{
		return exitTime;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public void setExitTime(LocalTime exitTime)
	{
		this.exitTime = exitTime;
	}
}
