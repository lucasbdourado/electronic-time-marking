package br.com.lucasbdourado.electronictimemarking.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class DailyRegister
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate registerDate;

	@ManyToOne(optional = false)
	private Author author;

	@OneToMany(mappedBy = "dailyRegister", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TimeMark> marks = new ArrayList<>();

	protected DailyRegister()
	{
	}

	public DailyRegister(Author author, LocalDate registerDate)
	{
		if (author == null)
		{
			throw new IllegalArgumentException("O author do registro diario nao pode ser nulo");
		}
		if (registerDate == null)
		{
			throw new IllegalArgumentException("A data do registro diario nao pode ser nula");
		}
		this.author = author;
		this.registerDate = registerDate;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public LocalDate getRegisterDate()
	{
		return registerDate;
	}

	public void setRegisterDate(LocalDate registerDate)
	{
		this.registerDate = registerDate;
	}

	public Author getAuthor()
	{
		return author;
	}

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public List<TimeMark> getMarks()
	{
		return Collections.unmodifiableList(marks);
	}

	public void setMarks(List<TimeMark> marks)
	{
		this.marks = marks;
	}

	public void addMark(TimeMark mark)
	{
		if (mark == null)
		{
			throw new IllegalArgumentException("A marcacao nao pode ser nula");
		}
		mark.setDailyRegister(this);
		this.marks.add(mark);
	}

	public boolean hasEnoughMarksToCalculate()
	{
		return marks.size() >= 3;
	}
}
