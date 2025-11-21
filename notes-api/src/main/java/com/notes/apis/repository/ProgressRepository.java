package com.notes.apis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.notes.apis.model.Progress;
import com.notes.apis.model.Student;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    @Query("SELECT DISTINCT p.student FROM Progress p WHERE p.semester.idSemester = :semesterId")
    List<Student> findStudentsBySemester(@Param("semesterId") Long semesterId);
    
    @Query("SELECT p.student FROM Progress p WHERE p.semester.idSemester = :semesterId")
    List<Student> findStudentsBySemesterId(@Param("semesterId") Long semesterId);
}
