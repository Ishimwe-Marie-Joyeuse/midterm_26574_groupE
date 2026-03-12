package SecureOnlineExamination.Secure_Online_Examination_System.repository;

import SecureOnlineExamination.Secure_Online_Examination_System.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    Optional<UserProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
