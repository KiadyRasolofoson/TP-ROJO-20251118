package com.notes.apis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSubject;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String ue;

    @Column(nullable = false)
    private int credit;

    @Column(nullable = false)
    private int code;

    @ManyToOne
    @JoinColumn(name = "id_semester", nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "id_program_option", nullable = false)
    private ProgramOption programOption;

    public Long getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(Long idSubject) {
        this.idSubject = idSubject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUe() {
        return ue;
    }

    public void setUe(String ue) {
        this.ue = ue;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public ProgramOption getProgramOption() {
        return programOption;
    }

    public void setProgramOption(ProgramOption programOption) {
        this.programOption = programOption;
    }

    // Getters & setters
}
