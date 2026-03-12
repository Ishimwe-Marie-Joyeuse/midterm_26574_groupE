package SecureOnlineExamination.Secure_Online_Examination_System.controller;

import SecureOnlineExamination.Secure_Online_Examination_System.model.Location;
import SecureOnlineExamination.Secure_Online_Examination_System.model.LocationType;
import SecureOnlineExamination.Secure_Online_Examination_System.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Location hierarchy: Province → District → Sector → Cell → Village (single table, LocationType enum).
 * User saves location ONLY via Location of type VILLAGE.
 */
@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/parent")
    public ResponseEntity<?> createParentLocation(@RequestBody Location location) {
        try {
            Location saved = locationService.saveParentLocation(location);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/child")
    public ResponseEntity<?> createChildLocation(@RequestParam UUID parentId, @RequestBody Location location) {
        try {
            Location saved = locationService.saveChildLocation(location, parentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<Location>> getAllLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(locationService.getAllLocations(page, size, sortBy, direction));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Location>> getAllLocationsNoPagination() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable UUID id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getLocationByCode(@PathVariable String code) {
        return locationService.getLocationByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable UUID id, @RequestBody Location location) {
        try {
            return ResponseEntity.ok(locationService.updateLocation(id, location));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable UUID id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.ok(Map.of("message", "Location and sub-locations deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getLocationsByType(@PathVariable String type) {
        try {
            LocationType lt = LocationType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(locationService.getLocationsByType(lt));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Valid types: PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE"));
        }
    }

    @GetMapping("/type/{type}/paged")
    public ResponseEntity<?> getLocationsByTypePaged(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            LocationType lt = LocationType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(locationService.getLocationsByType(lt, page, size));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Valid types: PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE"));
        }
    }

    @GetMapping("/provinces")
    public ResponseEntity<List<Location>> getAllProvinces() {
        return ResponseEntity.ok(locationService.getAllProvinces());
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<?> getChildLocations(@PathVariable UUID parentId) {
        return locationService.getChildLocations(parentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{parentId}/children/paged")
    public ResponseEntity<?> getChildLocationsPaged(
            @PathVariable UUID parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return locationService.getChildLocations(parentId, page, size)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/hierarchy-path")
    public ResponseEntity<?> getHierarchyPath(@PathVariable UUID id) {
        return locationService.getLocationHierarchyPath(id)
                .map(path -> ResponseEntity.ok(Map.of("hierarchyPath", path)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/villages")
    public ResponseEntity<List<Location>> getAllVillagesUnderLocation(@PathVariable UUID id) {
        return ResponseEntity.ok(locationService.getAllVillagesUnderLocation(id));
    }

    @GetMapping("/villages/lookup")
    public ResponseEntity<List<Location>> lookupVillagesByName(@RequestParam String name) {
        return ResponseEntity.ok(locationService.findVillagesByName(name));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Location>> searchLocations(@RequestParam String keyword) {
        return ResponseEntity.ok(locationService.searchLocations(keyword));
    }

    @GetMapping("/search-by-type")
    public ResponseEntity<?> searchLocationsByType(@RequestParam String type, @RequestParam String keyword) {
        try {
            LocationType lt = LocationType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(locationService.searchLocationsByType(lt, keyword));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Valid types: PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE"));
        }
    }

    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Map<String, Boolean>> existsByCode(@PathVariable String code) {
        return ResponseEntity.ok(Map.of("exists", locationService.existsByCode(code)));
    }

    @GetMapping("/statistics")
    public ResponseEntity<LocationService.LocationStatistics> getStatistics() {
        return ResponseEntity.ok(locationService.getStatistics());
    }
}
