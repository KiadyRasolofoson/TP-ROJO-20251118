package com.notes.apis.controller;

import com.notes.apis.model.Semester;
import com.notes.apis.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {
    
    @Autowired
    private SemesterRepository semesterRepository;
    
    @GetMapping
    public ResponseEntity<List<Semester>> getAllSemesters() {
        try {
            List<Semester> semesters = semesterRepository.findAll();
            return new ResponseEntity<>(semesters, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}