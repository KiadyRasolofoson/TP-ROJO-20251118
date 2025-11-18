package com.notes.apis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgress;

    @ManyToOne
    @JoinColumn(name = "id_option", nullable = false)
    private ProgramOption option;

    @ManyToOne
    @JoinColumn(name = "id_level")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "id_semester", nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "id_session", nullable = false)
    private SessionEntity session;

    @ManyToOne
    @JoinColumn(name = "id_student", nullable = false)
    private Student student;

    public Long getIdProgress() {
        return idProgress;
    }

    public void setIdProgress(Long idProgress) {
        this.idProgress = idProgress;
    }

    public ProgramOption getOption() {
        return option;
    }

    public void setOption(ProgramOption option) {
        this.option = option;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public SessionEntity getSession() {
        return session;
    }

    public void setSession(SessionEntity session) {
        this.session = session;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    // getters & setters
}
