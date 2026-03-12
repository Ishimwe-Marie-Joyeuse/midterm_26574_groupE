package SecureOnlineExamination.Secure_Online_Examination_System.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentExam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Many-to-One: Many StudentExams belong to One Student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private User student;
    
    // Many-to-One: Many StudentExams belong to One Exam
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonIgnore
    private Exam exam;
    
    private LocalDateTime startedAt;
    
    private LocalDateTime submittedAt;
    
    private Integer scoreObtained;
    
    private Boolean passed;
    
    @Enumerated(EnumType.STRING)
    private ExamStatus status;
    
    @Column(length = 2000)
    private String feedback;
    
    public StudentExam(User student, Exam exam) {
        this.student = student;
        this.exam = exam;
        this.startedAt = LocalDateTime.now();
        this.status = ExamStatus.IN_PROGRESS;
    }
}
