package SecureOnlineExamination.Secure_Online_Examination_System.repository;

import SecureOnlineExamination.Secure_Online_Examination_System.model.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    List<Exam> findByActiveTrue();
    Page<Exam> findByActiveTrue(Pageable pageable);
    Page<Exam> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    @Query("SELECT e FROM Exam e WHERE e.startTime >= :startTime AND e.endTime <= :endTime")
    List<Exam> findExamsInTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    boolean existsByTitle(String title);
}
