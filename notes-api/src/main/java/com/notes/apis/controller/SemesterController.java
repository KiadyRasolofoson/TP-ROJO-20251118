package com.notes.apis.controller;

import com.notes.apis.model.Semester;
import com.notes.apis.model.Student;
import com.notes.apis.repository.ProgressRepository;
import com.notes.apis.repository.SemesterRepository;
import com.notes.apis.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @GetMapping
    public ResponseEntity<List<Semester>> getAllSemesters() {
        try {
            List<Semester> semesters = semesterRepository.findAll();
            return new ResponseEntity<>(semesters, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{semesterId}/students")
    public ResponseEntity<Map<String, Object>> getStudentsBySemester(@PathVariable Long semesterId) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            List<Student> students = progressRepository.findStudentsBySemesterId(semesterId);

            if (students.isEmpty()) {
                response.put("success", true);
                response.put("data", List.of());
                response.put("message", "Aucun étudiant trouvé pour ce semestre");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            // Calculer la moyenne pour chaque étudiant pour TOUS les semestres (S1 à S4)
            List<Map<String, Object>> studentsWithAllAverages = students.stream().map(student -> {
                Map<String, Object> studentData = new HashMap<>();
                studentData.put("idStudent", student.getIdStudent());
                studentData.put("name", student.getName());
                studentData.put("firstname", student.getFirstname());
                studentData.put("birth", student.getBirth());

                // Calculer les moyennes pour chaque semestre (S1 à S4)
                Map<String, Object> averages = new HashMap<>();
                for (long semId = 1; semId <= 4; semId++) {
                    Double average = gradeRepository.findAverageGradeByStudentAndSemester(student.getIdStudent(),
                            semId);
                    averages.put("S" + semId, average != null ? Math.round(average * 100.0) / 100.0 : null);
                }

                studentData.put("averages", averages);

                // Moyenne générale (moyenne de toutes les moyennes de semestres)
                double generalAverage = averages.values().stream()
                        .filter(avg -> avg != null)
                        .mapToDouble(avg -> (Double) avg)
                        .average()
                        .orElse(0.0);
                studentData.put("generalAverage", Math.round(generalAverage * 100.0) / 100.0);

                return studentData;
            }).toList();

            response.put("success", true);
            response.put("data", studentsWithAllAverages);
            response.put("count", studentsWithAllAverages.size());
            response.put("semesterId", semesterId);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Erreur lors de la récupération des étudiants: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}