package com.notes.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes.apis.model.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
}
