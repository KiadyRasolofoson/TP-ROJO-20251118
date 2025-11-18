package com.notes.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes.apis.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
