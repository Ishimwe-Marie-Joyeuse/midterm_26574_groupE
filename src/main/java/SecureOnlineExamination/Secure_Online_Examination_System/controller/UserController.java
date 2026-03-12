package SecureOnlineExamination.Secure_Online_Examination_System.controller;

import SecureOnlineExamination.Secure_Online_Examination_System.dto.CreateUserRequest;
import SecureOnlineExamination.Secure_Online_Examination_System.model.User;
import SecureOnlineExamination.Secure_Online_Examination_System.model.UserRole;
import SecureOnlineExamination.Secure_Online_Examination_System.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users - Register user with villageCode OR villageName only.
     * Cell, Sector, District, Province are resolved automatically via Village.
     */
    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody CreateUserRequest request) {
        User savedUser = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(defaultValue = "id") String... sortBy) {
        List<User> users = userService.getAllUsersSorted(sortBy);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<Page<User>> getUsersByRole(
            @PathVariable UserRole role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<User> users = userService.getUsersByRole(role, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/province/{identifier}")
    public ResponseEntity<List<User>> getUsersByProvince(@PathVariable String identifier) {
        List<User> users = userService.getUsersByProvinceCodeOrName(identifier);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/province/code/{code}")
    public ResponseEntity<List<User>> getUsersByProvinceCode(@PathVariable String code) {
        List<User> users = userService.getUsersByProvinceCode(code);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/province/name/{name}")
    public ResponseEntity<List<User>> getUsersByProvinceName(@PathVariable String name) {
        List<User> users = userService.getUsersByProvinceName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/province/paginated/{code}")
    public ResponseEntity<Page<User>> getUsersByProvinceCodePaginated(
            @PathVariable String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        Page<User> users = userService.getUsersByProvinceCodePaginated(code, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-province/{provinceId}")
    public ResponseEntity<Page<User>> getUsersByProvinceId(
            @PathVariable UUID provinceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return ResponseEntity.ok(userService.getUsersByProvinceId(provinceId, pageable));
    }

    @GetMapping("/by-district/{districtId}")
    public ResponseEntity<Page<User>> getUsersByDistrictId(
            @PathVariable UUID districtId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return ResponseEntity.ok(userService.getUsersByDistrictId(districtId, pageable));
    }

    @GetMapping("/by-sector/{sectorId}")
    public ResponseEntity<Page<User>> getUsersBySectorId(
            @PathVariable UUID sectorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return ResponseEntity.ok(userService.getUsersBySectorId(sectorId, pageable));
    }

    @GetMapping("/by-cell/{cellId}")
    public ResponseEntity<Page<User>> getUsersByCellId(
            @PathVariable UUID cellId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return ResponseEntity.ok(userService.getUsersByCellId(cellId, pageable));
    }

    @GetMapping("/by-village/{villageId}")
    public ResponseEntity<Page<User>> getUsersByVillageId(
            @PathVariable UUID villageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
        return ResponseEntity.ok(userService.getUsersByVillageId(villageId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
}
