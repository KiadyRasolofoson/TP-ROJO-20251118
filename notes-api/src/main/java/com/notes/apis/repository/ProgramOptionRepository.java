package com.notes.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes.apis.model.ProgramOption;

@Repository
public interface ProgramOptionRepository extends JpaRepository<ProgramOption, Long> {
}
