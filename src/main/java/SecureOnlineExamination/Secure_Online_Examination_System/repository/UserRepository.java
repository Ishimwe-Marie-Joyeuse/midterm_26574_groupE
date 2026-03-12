package SecureOnlineExamination.Secure_Online_Examination_System.repository;

import SecureOnlineExamination.Secure_Online_Examination_System.model.User;
import SecureOnlineExamination.Secure_Online_Examination_System.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Page<User> findByRole(UserRole role, Pageable pageable);

    /** Users by location (village) - for filtering by province/district/etc use service layer */
    Page<User> findByLocation_Id(UUID locationId, Pageable pageable);
    List<User> findByLocation_Id(UUID locationId);

    @Query("SELECT u FROM User u WHERE u.location.id IN :villageIds")
    Page<User> findByLocationIdIn(List<UUID> villageIds, Pageable pageable);
}
