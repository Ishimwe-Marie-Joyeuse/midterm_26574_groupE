package SecureOnlineExamination.Secure_Online_Examination_System.service;

import SecureOnlineExamination.Secure_Online_Examination_System.dto.CreateUserRequest;
import SecureOnlineExamination.Secure_Online_Examination_System.model.Location;
import SecureOnlineExamination.Secure_Online_Examination_System.model.LocationType;
import SecureOnlineExamination.Secure_Online_Examination_System.model.User;
import SecureOnlineExamination.Secure_Online_Examination_System.model.UserRole;
import SecureOnlineExamination.Secure_Online_Examination_System.repository.LocationRepository;
import SecureOnlineExamination.Secure_Online_Examination_System.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;

    public UserService(UserRepository userRepository, LocationRepository locationRepository, LocationService locationService) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }

    public User registerUser(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already exists: " + req.getUsername());
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists: " + req.getEmail());
        }
        Location village = resolveVillage(req.getVillageCode(), req.getVillageName());
        User user = new User(
                req.getUsername(), req.getEmail(), req.getPassword(), req.getFullName(),
                UserRole.valueOf(req.getRole() != null ? req.getRole() : "STUDENT"),
                village
        );
        if (req.getActive() != null) user.setActive(req.getActive());
        return userRepository.save(user);
    }

    private Location resolveVillage(String villageCode, String villageName) {
        if (villageCode != null && !villageCode.isBlank()) {
            String code = villageCode.trim();
            return locationRepository.findByCodeAndLocationType("V-" + code, LocationType.VILLAGE)
                    .or(() -> locationRepository.findByCodeAndLocationType(code, LocationType.VILLAGE))
                    .orElseThrow(() -> new RuntimeException("Village not found for code: " + villageCode));
        }
        if (villageName != null && !villageName.isBlank()) {
            List<Location> list = locationService.findVillagesByName(villageName.trim());
            if (list.isEmpty()) throw new RuntimeException("Village not found for name: " + villageName);
            if (list.size() > 1) throw new RuntimeException("Multiple villages match. Use villageCode instead.");
            return list.get(0);
        }
        throw new RuntimeException("Provide villageCode or villageName");
    }

    public List<User> getAllUsersSorted(String... sortFields) {
        return userRepository.findAll(Sort.by(sortFields));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getUsersByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    public List<User> getUsersByProvinceCodeOrName(String identifier) {
        List<UUID> villageIds = getVillageIdsUnderProvince(identifier);
        if (villageIds.isEmpty()) return List.of();
        return villageIds.stream()
                .flatMap(id -> userRepository.findByLocation_Id(id).stream())
                .distinct()
                .toList();
    }

    public List<User> getUsersByProvinceCode(String code) {
        List<User> byPrefixed = getUsersByProvinceCodeOrName("P-" + code);
        return byPrefixed.isEmpty() ? getUsersByProvinceCodeOrName(code) : byPrefixed;
    }

    public List<User> getUsersByProvinceName(String name) {
        return getUsersByProvinceCodeOrName(name);
    }

    public Page<User> getUsersByProvinceCodePaginated(String code, Pageable pageable) {
        List<UUID> villageIds = getVillageIdsUnderProvince("P-" + code);
        if (villageIds.isEmpty()) villageIds = getVillageIdsUnderProvince(code);
        if (villageIds.isEmpty()) return Page.empty(pageable);
        return userRepository.findByLocationIdIn(villageIds, pageable);
    }

    public Page<User> getUsersByProvinceId(UUID provinceId, Pageable pageable) {
        List<UUID> villageIds = locationService.getAllVillagesUnderLocation(provinceId).stream()
                .map(Location::getId).toList();
        if (villageIds.isEmpty()) return Page.empty(pageable);
        return userRepository.findByLocationIdIn(villageIds, pageable);
    }

    public Page<User> getUsersByDistrictId(UUID districtId, Pageable pageable) {
        List<UUID> villageIds = locationService.getAllVillagesUnderLocation(districtId).stream()
                .map(Location::getId).toList();
        if (villageIds.isEmpty()) return Page.empty(pageable);
        return userRepository.findByLocationIdIn(villageIds, pageable);
    }

    public Page<User> getUsersBySectorId(UUID sectorId, Pageable pageable) {
        List<UUID> villageIds = locationService.getAllVillagesUnderLocation(sectorId).stream()
                .map(Location::getId).toList();
        if (villageIds.isEmpty()) return Page.empty(pageable);
        return userRepository.findByLocationIdIn(villageIds, pageable);
    }

    public Page<User> getUsersByCellId(UUID cellId, Pageable pageable) {
        List<UUID> villageIds = locationService.getAllVillagesUnderLocation(cellId).stream()
                .map(Location::getId).toList();
        if (villageIds.isEmpty()) return Page.empty(pageable);
        return userRepository.findByLocationIdIn(villageIds, pageable);
    }

    public Page<User> getUsersByVillageId(UUID villageId, Pageable pageable) {
        return userRepository.findByLocation_Id(villageId, pageable);
    }

    private List<UUID> getVillageIdsUnderProvince(String provinceCodeOrName) {
        Optional<Location> province = locationRepository.findByCode(provinceCodeOrName)
                .filter(l -> l.getLocationType() == LocationType.PROVINCE)
                .or(() -> locationRepository.findFirstByLocationTypeAndNameIgnoreCase(LocationType.PROVINCE, provinceCodeOrName));
        if (province.isEmpty()) return List.of();
        return locationService.getAllSubLocations(province.get().getId())
                .map(list -> list.stream()
                        .filter(l -> l.getLocationType() == LocationType.VILLAGE)
                        .map(Location::getId)
                        .toList())
                .orElse(List.of());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setRole(userDetails.getRole());
        user.setActive(userDetails.getActive());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) throw new RuntimeException("User not found: " + id);
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}