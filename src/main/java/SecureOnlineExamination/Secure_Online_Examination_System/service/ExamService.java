package SecureOnlineExamination.Secure_Online_Examination_System.service;

import SecureOnlineExamination.Secure_Online_Examination_System.model.Exam;
import SecureOnlineExamination.Secure_Online_Examination_System.model.User;
import SecureOnlineExamination.Secure_Online_Examination_System.repository.ExamRepository;
import SecureOnlineExamination.Secure_Online_Examination_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ExamService {
    
    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Exam createExam(Exam exam) {
        if (examRepository.existsByTitle(exam.getTitle())) {
            throw new RuntimeException("Exam with title already exists: " + exam.getTitle());
        }
        return examRepository.save(exam);
    }
    
    /**
     * Enroll a student in an exam
     * Demonstrates Many-to-Many relationship handling
     */
    public Exam enrollStudentInExam(Long examId, Long studentId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));
        
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        exam.getEnrolledStudents().add(student);
        student.getEnrolledExams().add(exam);
        
        return examRepository.save(exam);
    }
    
    public Page<Exam> getAllExams(Pageable pageable) {
        return examRepository.findAll(pageable);
    }
    
    public Page<Exam> getActiveExams(Pageable pageable) {
        return examRepository.findByActiveTrue(pageable);
    }
    
    public Page<Exam> searchExamsByTitle(String title, Pageable pageable) {
        return examRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
    
    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }
    
    public Exam updateExam(Long id, Exam examDetails) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
        
        exam.setTitle(examDetails.getTitle());
        exam.setDescription(examDetails.getDescription());
        exam.setDurationMinutes(examDetails.getDurationMinutes());
        exam.setTotalMarks(examDetails.getTotalMarks());
        exam.setPassingMarks(examDetails.getPassingMarks());
        exam.setStartTime(examDetails.getStartTime());
        exam.setEndTime(examDetails.getEndTime());
        exam.setActive(examDetails.getActive());
        
        return examRepository.save(exam);
    }
    
    public void deleteExam(Long id) {
        if (!examRepository.existsById(id)) {
            throw new RuntimeException("Exam not found with id: " + id);
        }
        examRepository.deleteById(id);
    }
}
