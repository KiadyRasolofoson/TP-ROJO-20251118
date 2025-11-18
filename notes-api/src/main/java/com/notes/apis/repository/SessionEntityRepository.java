package com.notes.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes.apis.model.SessionEntity;

@Repository
public interface SessionEntityRepository extends JpaRepository<SessionEntity, Long> {

}
