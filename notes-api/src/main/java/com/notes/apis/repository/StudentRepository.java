package com.notes.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes.apis.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
