package com.notes.apis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "semester")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSemester;

    @Column(nullable = false, unique = true)
    private String label;

    public Semester() {

    }

    public Semester(Long idSemester, String label) {
        this.idSemester = idSemester;
        this.label = label;
    }

    public Long getIdSemester() {
        return idSemester;
    }

    public void setIdSemester(Long idSemester) {
        this.idSemester = idSemester;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
