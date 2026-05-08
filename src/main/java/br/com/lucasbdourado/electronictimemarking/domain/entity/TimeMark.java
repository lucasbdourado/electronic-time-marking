package br.com.lucasbdourado.electronictimemarking.domain.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class TimeMark
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalTime markedAt;

	@ManyToOne(optional = false)
	private DailyRegister dailyRegister;

	@Enumerated(EnumType.STRING)
	private RegisterType type;

	protected TimeMark()
	{
	}

	public TimeMark(RegisterType type, LocalTime markedAt)
	{
		if (type == null)
		{
			throw new IllegalArgumentException("O tipo do registro nao pode ser nulo");
		}

		if (markedAt == null)
		{
			throw new IllegalArgumentException("O horario do registro nao pode ser nulo");
		}

		this.type = type;
		this.markedAt = markedAt;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return id;
	}

	public LocalTime getMarkedAt()
	{
		return markedAt;
	}

	public void setMarkedAt(LocalTime markedAt)
	{
		this.markedAt = markedAt;
	}

	public DailyRegister getDailyRegister()
	{
		return dailyRegister;
	}

	public void setDailyRegister(DailyRegister dailyRegister)
	{
		this.dailyRegister = dailyRegister;
	}

	public RegisterType getType()
	{
		return type;
	}

	public void setType(RegisterType type)
	{
		this.type = type;
	}
}
