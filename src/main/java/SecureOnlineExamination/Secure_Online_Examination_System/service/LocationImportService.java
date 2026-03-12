package SecureOnlineExamination.Secure_Online_Examination_System.service;

import SecureOnlineExamination.Secure_Online_Examination_System.dto.LocationRowDto;
import SecureOnlineExamination.Secure_Online_Examination_System.model.Location;
import SecureOnlineExamination.Secure_Online_Examination_System.model.LocationType;
import SecureOnlineExamination.Secure_Online_Examination_System.repository.LocationRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Imports only Kigali City province from locations.json.
 * Province has parent_id=null. Other provinces can be added via Postman.
 */
@Service
public class LocationImportService {

    private static final String KIGALI_PROVINCE_NAME = "KIGALI";
    private static final String KIGALI_PROVINCE_CODE = "P-1";

    private final ObjectMapper objectMapper;
    private final LocationRepository locationRepository;

    public LocationImportService(ObjectMapper objectMapper, LocationRepository locationRepository) {
        this.objectMapper = objectMapper;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public void importKigaliOnly() throws Exception {
        if (locationRepository.findByCode(KIGALI_PROVINCE_CODE).isPresent()) return;

        List<LocationRowDto> rows = readJson("locations.json")
                .stream()
                .filter(r -> KIGALI_PROVINCE_NAME.equalsIgnoreCase(clean(r.getProvinceName())))
                .toList();
        Map<String, Location> provinceCache = new HashMap<>();
        Map<String, Location> districtCache = new HashMap<>();
        Map<String, Location> sectorCache = new HashMap<>();
        Map<String, Location> cellCache = new HashMap<>();

        for (LocationRowDto r : rows) {
            String pc = clean(r.getProvinceCode());
            String pn = clean(r.getProvinceName());
            String dc = clean(r.getDistrictCode());
            String dn = clean(r.getDistrictName());
            String sc = clean(r.getSectorCode());
            String sn = clean(r.getSectorName());
            String cc = clean(r.getCellCode());
            String cn = clean(r.getCellName());
            String vc = clean(r.getVillageCode());
            String vn = clean(r.getVillageName());

            if (pn.isBlank() || dn.isBlank() || sn.isBlank() || cn.isBlank() || vn.isBlank()) continue;

            // Use prefixed codes to avoid collision (e.g. province 1 vs district 101)
            String provCode = "P-" + pc;
            String distCode = "D-" + dc;
            String sectCode = "S-" + sc;
            String cellCode = "C-" + cc;
            String villCode = "V-" + vc;

            Location province = provinceCache.computeIfAbsent(provCode, k ->
                    locationRepository.findByCode(provCode)
                            .orElseGet(() -> locationRepository.save(new Location(provCode, pn, LocationType.PROVINCE, null, null)))
            );

            String dKey = province.getId() + "|" + distCode;
            Location district = districtCache.computeIfAbsent(dKey, k ->
                    locationRepository.findByCode(distCode)
                            .orElseGet(() -> locationRepository.save(new Location(distCode, dn, LocationType.DISTRICT, null, province)))
            );

            String sKey = district.getId() + "|" + sectCode;
            Location sector = sectorCache.computeIfAbsent(sKey, k ->
                    locationRepository.findByCode(sectCode)
                            .orElseGet(() -> locationRepository.save(new Location(sectCode, sn, LocationType.SECTOR, null, district)))
            );

            String cKey = sector.getId() + "|" + cellCode;
            Location cell = cellCache.computeIfAbsent(cKey, k ->
                    locationRepository.findByCode(cellCode)
                            .orElseGet(() -> locationRepository.save(new Location(cellCode, cn, LocationType.CELL, null, sector)))
            );

            if (!locationRepository.existsByCode(villCode)) {
                locationRepository.save(new Location(villCode, vn, LocationType.VILLAGE, null, cell));
            }
        }
    }

    private List<LocationRowDto> readJson(String path) throws Exception {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(is, new TypeReference<>() {});
        }
    }

    private static String clean(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ");
    }
}
