package SecureOnlineExamination.Secure_Online_Examination_System.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudentExam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
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
