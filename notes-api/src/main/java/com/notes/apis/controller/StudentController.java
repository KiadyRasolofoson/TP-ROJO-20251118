package com.notes.apis.controller;

import com.notes.apis.model.Grade;
import com.notes.apis.model.Student;
import com.notes.apis.repository.GradeRepository;
import com.notes.apis.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    // ==============================
    // Notes d'un semestre pour un étudiant
    // ==============================
    @GetMapping("/{studentId}/grades/semester/{semesterId}")
    public ResponseEntity<Map<String, Object>> getGradesBySemester(
            @PathVariable Long studentId,
            @PathVariable Long semesterId) {

        Map<String, Object> response = new LinkedHashMap<>();

        try {
            var studentOpt = studentRepository.findById(studentId);
            if (studentOpt.isEmpty()) {
                response.put("success", false);
                response.put("data", null);
                response.put("error", "Étudiant non trouvé avec l'ID : " + studentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            List<Grade> grades = gradeRepository.findGradesByStudentAndSemesterId(studentId, semesterId);

            // Création d'une liste simplifiée pour le JSON
            List<Map<String, Object>> simplifiedGrades = grades.stream().map(g -> {
                Map<String, Object> gradeMap = new HashMap<>();
                gradeMap.put("idGrade", g.getIdGrade());
                gradeMap.put("grade", g.getGrade());

                Map<String, Object> subjectMap = new HashMap<>();
                subjectMap.put("idSubject", g.getSubject().getIdSubject());
                subjectMap.put("title", g.getSubject().getTitle());
                subjectMap.put("ue", g.getSubject().getUe());
                subjectMap.put("credit", g.getSubject().getCredit());

                gradeMap.put("subject", subjectMap);
                return gradeMap;
            }).toList();

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("student", Map.of(
                    "id", studentOpt.get().getIdStudent(),
                    "name", studentOpt.get().getName(),
                    "firstname", studentOpt.get().getFirstname()));
            data.put("semester", Map.of(
                    "id", semesterId,
                    "label", grades.isEmpty() ? "Unknown" : grades.get(0).getProgress().getSemester().getLabel()));
            data.put("grades", simplifiedGrades);

            response.put("success", true);
            response.put("data", data);
            response.put("error", null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("data", null);
            response.put("error", "Erreur imprévue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==============================
    // Notes pour un niveau/année pour un étudiant
    // ==============================
    @GetMapping("/{studentId}/grades/level/{levelId}")
    public ResponseEntity<Map<String, Object>> getGradesByLevel(
            @PathVariable Long studentId,
            @PathVariable Long levelId) {

        Map<String, Object> response = new LinkedHashMap<>();

        try {
            var studentOpt = studentRepository.findById(studentId);
            if (studentOpt.isEmpty()) {
                response.put("success", false);
                response.put("data", null);
                response.put("error", "Étudiant non trouvé avec l'ID : " + studentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            List<Grade> grades = gradeRepository.findGradesByStudentAndLevelId(studentId, levelId);

            // Regrouper par semestre
            Map<String, List<Map<String, Object>>> gradesBySemester = grades.stream()
                    .collect(Collectors.groupingBy(
                            g -> g.getProgress().getSemester().getLabel(),
                            LinkedHashMap::new, // garder l'ordre
                            Collectors.mapping(g -> {
                                Map<String, Object> gradeMap = new HashMap<>();
                                gradeMap.put("idGrade", g.getIdGrade());
                                gradeMap.put("grade", g.getGrade());

                                Map<String, Object> subjectMap = new HashMap<>();
                                subjectMap.put("idSubject", g.getSubject().getIdSubject());
                                subjectMap.put("title", g.getSubject().getTitle());
                                subjectMap.put("ue", g.getSubject().getUe());
                                subjectMap.put("credit", g.getSubject().getCredit());

                                gradeMap.put("subject", subjectMap);
                                return gradeMap;
                            }, Collectors.toList())));

            // Construire la structure finale
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("student", Map.of(
                    "id", studentOpt.get().getIdStudent(),
                    "name", studentOpt.get().getName(),
                    "firstname", studentOpt.get().getFirstname()));

            List<Map<String, Object>> semesters = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : gradesBySemester.entrySet()) {
                Map<String, Object> semesterMap = new LinkedHashMap<>();
                semesterMap.put("semester", Map.of("label", entry.getKey()));
                semesterMap.put("grades", entry.getValue());
                semesters.add(semesterMap);
            }

            data.put("semesters", semesters);

            response.put("success", true);
            response.put("data", data);
            response.put("error", null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("data", null);
            response.put("error", "Erreur imprévue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==============================
    // Moyennes d'un étudiant pour tous les semestres (S1 à S4)
    // ==============================
    @GetMapping("/{studentId}/averages")
    public ResponseEntity<Map<String, Object>> getStudentAverages(@PathVariable Long studentId) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            var studentOpt = studentRepository.findById(studentId);
            if (studentOpt.isEmpty()) {
                response.put("success", false);
                response.put("data", null);
                response.put("error", "Étudiant non trouvé avec l'ID : " + studentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Student student = studentOpt.get();

            // Récupérer les moyennes pour chaque semestre (S1 à S4)
            Map<String, Object> averagesData = new LinkedHashMap<>();
            Map<String, Double> semesterAverages = new LinkedHashMap<>();
            double totalSum = 0;
            int semesterCount = 0;

            for (long semesterId = 1; semesterId <= 4; semesterId++) {
                Double average = gradeRepository.findAverageGradeByStudentAndSemester(studentId, semesterId);
                if (average != null) {
                    double roundedAverage = Math.round(average * 100.0) / 100.0;
                    semesterAverages.put("S" + semesterId, roundedAverage);
                    totalSum += roundedAverage;
                    semesterCount++;
                } else {
                    semesterAverages.put("S" + semesterId, null);
                }
            }

            // Calculer la moyenne générale
            Double generalAverage = semesterCount > 0 ? Math.round((totalSum / semesterCount) * 100.0) / 100.0 : null;

            // Construire la réponse
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("student", Map.of(
                    "id", student.getIdStudent(),
                    "name", student.getName(),
                    "firstname", student.getFirstname(),
                    "birth", student.getBirth()));
            data.put("semesterAverages", semesterAverages);
            data.put("generalAverage", generalAverage);
            data.put("semestersWithGrades", semesterCount);

            response.put("success", true);
            response.put("data", data);
            response.put("error", null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("data", null);
            response.put("error", "Erreur imprévue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
