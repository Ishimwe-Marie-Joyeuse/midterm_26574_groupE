package SecureOnlineExamination.Secure_Online_Examination_System.controller;

import SecureOnlineExamination.Secure_Online_Examination_System.model.Exam;
import SecureOnlineExamination.Secure_Online_Examination_System.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    
    @Autowired
    private ExamService examService;
    
    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        Exam savedExam = examService.createExam(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
    }
    
    /**
     * POST /api/exams/{examId}/enroll/{studentId} - Enroll student in exam
     * Demonstrates Many-to-Many relationship
     */
    @PostMapping("/{examId}/enroll/{studentId}")
    public ResponseEntity<Exam> enrollStudent(
            @PathVariable Long examId,
            @PathVariable Long studentId) {
        Exam exam = examService.enrollStudentInExam(examId, studentId);
        return ResponseEntity.ok(exam);
    }
    
    @GetMapping
    public ResponseEntity<Page<Exam>> getAllExams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Exam> exams = examService.getAllExams(pageable);
        
        return ResponseEntity.ok(exams);
    }
    
    @GetMapping("/active")
    public ResponseEntity<Page<Exam>> getActiveExams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Exam> exams = examService.getActiveExams(pageable);
        return ResponseEntity.ok(exams);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Exam>> searchExams(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Exam> exams = examService.searchExamsByTitle(title, pageable);
        return ResponseEntity.ok(exams);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        return examService.getExamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(
            @PathVariable Long id,
            @RequestBody Exam examDetails) {
        Exam updatedExam = examService.updateExam(id, examDetails);
        return ResponseEntity.ok(updatedExam);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}