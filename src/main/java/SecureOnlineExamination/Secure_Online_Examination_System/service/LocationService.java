package SecureOnlineExamination.Secure_Online_Examination_System.service;

import SecureOnlineExamination.Secure_Online_Examination_System.model.Location;
import SecureOnlineExamination.Secure_Online_Examination_System.model.LocationType;
import SecureOnlineExamination.Secure_Online_Examination_System.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location saveParentLocation(Location location) {
        if (locationRepository.existsByCode(location.getCode())) {
            throw new RuntimeException("Location with code '" + location.getCode() + "' already exists");
        }
        location.setParent(null);
        return locationRepository.save(location);
    }

    public Location saveChildLocation(Location location, UUID parentId) {
        if (locationRepository.existsByCode(location.getCode())) {
            throw new RuntimeException("Location with code '" + location.getCode() + "' already exists");
        }
        Location parent = locationRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent location not found: " + parentId));
        location.setParent(parent);
        return locationRepository.save(location);
    }

    public Location updateLocation(UUID id, Location details) {
        Location loc = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found: " + id));
        if (!loc.getCode().equals(details.getCode()) && locationRepository.existsByCode(details.getCode())) {
            throw new RuntimeException("Location code already exists");
        }
        loc.setCode(details.getCode());
        loc.setName(details.getName());
        loc.setDescription(details.getDescription());
        if (details.getParent() != null) loc.setParent(details.getParent());
        return locationRepository.save(loc);
    }

    public Optional<Location> getLocationById(UUID id) {
        return locationRepository.findById(id);
    }

    public Optional<Location> getLocationByCode(String code) {
        return locationRepository.findByCode(code);
    }

    public Optional<Location> getVillageByCode(String code) {
        return locationRepository.findByCodeAndLocationType(code, LocationType.VILLAGE);
    }

    public Optional<Location> getLocationByNameAndType(String name, LocationType type) {
        return locationRepository.findByNameAndLocationType(name, type);
    }

    public Page<Location> getAllLocations(int page, int size, String sortBy, String direction) {
        Sort sort = "desc".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return locationRepository.findAll(PageRequest.of(page, size, sort));
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> getLocationsByType(LocationType type) {
        return locationRepository.findByLocationType(type);
    }

    public Page<Location> getLocationsByType(LocationType type, int page, int size) {
        return locationRepository.findByLocationType(type, PageRequest.of(page, size));
    }

    public Optional<List<Location>> getChildLocations(UUID parentId) {
        if (!locationRepository.existsById(parentId)) return Optional.empty();
        return Optional.of(locationRepository.findByParentId(parentId));
    }

    public Optional<Page<Location>> getChildLocations(UUID parentId, int page, int size) {
        return locationRepository.findById(parentId)
                .map(parent -> locationRepository.findByParent(parent, PageRequest.of(page, size)));
    }

    public List<Location> getAllProvinces() {
        return locationRepository.findByParentIsNull();
    }

    public Optional<String> getLocationHierarchyPath(UUID locationId) {
        return locationRepository.findById(locationId).map(loc -> {
            StringBuilder path = new StringBuilder(loc.getName());
            Location current = loc.getParent();
            while (current != null) {
                path.insert(0, current.getName() + " > ");
                current = current.getParent();
            }
            return path.toString();
        });
    }

    public Optional<List<Location>> getAllSubLocations(UUID locationId) {
        return locationRepository.findById(locationId).map(loc -> {
            List<Location> result = new ArrayList<>();
            collectSubLocations(loc, result);
            return result;
        });
    }

    private void collectSubLocations(Location loc, List<Location> result) {
        for (Location sub : loc.getSubLocations()) {
            result.add(sub);
            collectSubLocations(sub, result);
        }
    }

    /** Get all villages under a location (province, district, sector, or cell) */
    public List<Location> getAllVillagesUnderLocation(UUID locationId) {
        return getAllSubLocations(locationId)
                .map(list -> list.stream().filter(l -> l.getLocationType() == LocationType.VILLAGE).toList())
                .orElse(List.of());
    }

    public boolean existsByCode(String code) {
        return locationRepository.existsByCode(code);
    }

    public void deleteLocation(UUID id) {
        Location loc = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found: " + id));
        locationRepository.delete(loc);
    }

    public List<Location> searchLocations(String keyword) {
        return locationRepository.findAll().stream()
                .filter(l -> l.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public List<Location> searchLocationsByType(LocationType type, String keyword) {
        return locationRepository.findByLocationType(type).stream()
                .filter(l -> l.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public List<Location> findVillagesByName(String name) {
        return locationRepository.findByLocationTypeAndNameIgnoreCase(LocationType.VILLAGE, name);
    }

    public LocationStatistics getStatistics() {
        return new LocationStatistics(
                locationRepository.countByLocationType(LocationType.PROVINCE),
                locationRepository.countByLocationType(LocationType.DISTRICT),
                locationRepository.countByLocationType(LocationType.SECTOR),
                locationRepository.countByLocationType(LocationType.CELL),
                locationRepository.countByLocationType(LocationType.VILLAGE)
        );
    }

    public record LocationStatistics(long provinceCount, long districtCount, long sectorCount, long cellCount, long villageCount) {}
}
