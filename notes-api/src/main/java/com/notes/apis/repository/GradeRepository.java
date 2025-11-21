package com.notes.apis.repository;

import com.notes.apis.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

        // Notes par étudiant et semestre
        @Query("SELECT g FROM Grade g " +
                        "JOIN g.progress p " +
                        "WHERE p.student.idStudent = :studentId " +
                        "AND p.semester.idSemester = :semesterId " +
                        "ORDER BY g.subject.title")
        List<Grade> findGradesByStudentAndSemesterId(
                        @Param("studentId") Long studentId,
                        @Param("semesterId") Long semesterId);

        // Notes par étudiant et niveau
        @Query("SELECT g FROM Grade g " +
                        "JOIN g.progress p " +
                        "WHERE p.student.idStudent = :studentId " +
                        "AND p.level.idLevel = :levelId " +
                        "ORDER BY g.subject.title")
        List<Grade> findGradesByStudentAndLevelId(
                        @Param("studentId") Long studentId,
                        @Param("levelId") Long levelId);

        @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.progress.student.idStudent = :studentId AND g.progress.semester.idSemester = :semesterId")
        Double findAverageGradeByStudentAndSemester(@Param("studentId") Long studentId,
                        @Param("semesterId") Long semesterId);
}
