package br.com.lucasbdourado.electronictimemarking.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DailyRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate registerDate;

    @ManyToOne(optional = false)
    private Author author;

    @OneToMany(mappedBy = "dailyRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeMark> marks = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<TimeMark> getMarks() {
        return marks;
    }

    public void setMarks(List<TimeMark> marks) {
        this.marks = marks;
    }
}
