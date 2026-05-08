package br.com.lucasbdourado.electronictimemarking.domain.entity;

import jakarta.persistence.*;

@Entity
public class Author
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private Long discordId;

	@Column(unique = true)
	private String code;

	private String name;

	protected Author()
	{
	}

	public Author(Long discordId, String code, String name)
	{
		if (discordId == null)
		{
			throw new IllegalArgumentException("O discordId do author nao pode ser nulo");
		}

		this.discordId = discordId;
		this.code = code;
		this.name = name;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getDiscordId()
	{
		return discordId;
	}

	public void setDiscordId(Long discordId)
	{
		this.discordId = discordId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void updateProfile(String code, String name)
	{
		this.code = code;
		this.name = name;
	}
}
