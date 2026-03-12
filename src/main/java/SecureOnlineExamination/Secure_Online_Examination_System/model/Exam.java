package SecureOnlineExamination.Secure_Online_Examination_System.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    private Integer durationMinutes;
    
    private Integer totalMarks;
    
    private Integer passingMarks;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    // Many-to-Many: Many Exams can have Many Users enrolled
    @ManyToMany(mappedBy = "enrolledExams")
    @JsonIgnore
    private List<User> enrolledStudents = new ArrayList<>();
    
    // One-to-Many: One Exam has Many Questions
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();
    
    // One-to-Many: One Exam has Many StudentExam records
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StudentExam> studentExams = new ArrayList<>();
    
    public Exam(String title, String description, Integer durationMinutes, 
                Integer totalMarks, Integer passingMarks) {
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.passingMarks = passingMarks;
    }
}