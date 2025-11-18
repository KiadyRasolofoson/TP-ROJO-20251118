package com.notes.apis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrade;

    private Double grade;

    @ManyToOne
    @JoinColumn(name = "id_subject", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "id_progress", nullable = false)
    private Progress progress;

    public Long getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(Long idGrade) {
        this.idGrade = idGrade;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    // getters & setters
}
