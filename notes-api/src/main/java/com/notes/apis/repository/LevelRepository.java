package com.notes.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes.apis.model.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
}
