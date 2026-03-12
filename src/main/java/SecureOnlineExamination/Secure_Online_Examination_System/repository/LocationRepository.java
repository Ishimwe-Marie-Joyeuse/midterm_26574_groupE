package SecureOnlineExamination.Secure_Online_Examination_System.repository;

import SecureOnlineExamination.Secure_Online_Examination_System.model.Location;
import SecureOnlineExamination.Secure_Online_Examination_System.model.LocationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    boolean existsByCode(String code);
    boolean existsByNameAndLocationType(String name, LocationType type);

    Optional<Location> findByCode(String code);
    Optional<Location> findByNameAndLocationType(String name, LocationType type);

    List<Location> findByLocationType(LocationType type);
    Page<Location> findByLocationType(LocationType type, Pageable pageable);

    List<Location> findByParent(Location parent);
    Page<Location> findByParent(Location parent, Pageable pageable);

    List<Location> findByParentId(UUID parentId);
    List<Location> findByLocationTypeAndParent(LocationType type, Location parent);

    List<Location> findByParentIsNull();

    long countByLocationType(LocationType type);
    long countByParent(Location parent);

    /** Find village by code (unique across all locations) */
    Optional<Location> findByCodeAndLocationType(String code, LocationType type);

    List<Location> findByLocationTypeAndNameIgnoreCase(LocationType type, String name);
    Optional<Location> findFirstByLocationTypeAndNameIgnoreCase(LocationType type, String name);
}
