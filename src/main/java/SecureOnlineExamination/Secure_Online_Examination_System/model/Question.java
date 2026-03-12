package SecureOnlineExamination.Secure_Online_Examination_System.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String questionText;
    
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    
    @Column(nullable = false)
    private String correctAnswer;
    
    private Integer marks;
    
    @Enumerated(EnumType.STRING)
    private QuestionDifficulty difficulty;
    
    // Many-to-One: Many Questions belong to One Exam
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonIgnore
    private Exam exam;
    
    public Question(String questionText, String optionA, String optionB, 
                   String optionC, String optionD, String correctAnswer, 
                   Integer marks, Exam exam) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.marks = marks;
        this.exam = exam;
    }
}
