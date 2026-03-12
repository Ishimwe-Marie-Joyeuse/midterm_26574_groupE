package SecureOnlineExamination.Secure_Online_Examination_System.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for parsing locations.json. JSON uses province_code, district_code as numbers;
 * we convert to String for storage.
 */
public class LocationRowDto {

    @JsonProperty("province_code")
    private Object provinceCode;

    @JsonProperty("province_name")
    private String provinceName;

    @JsonProperty("district_code")
    private Object districtCode;

    @JsonProperty("district_name")
    private String districtName;

    @JsonProperty("sector_code")
    private Object sectorCode;

    @JsonProperty("sector_name")
    private String sectorName;

    @JsonProperty("cell_code")
    private Object cellCode;

    @JsonProperty("cell_name")
    private String cellName;

    @JsonProperty("village_code")
    private Object villageCode;

    @JsonProperty("village_name")
    private String villageName;

    public String getProvinceCode() { return toStr(provinceCode); }
    public String getProvinceName() { return provinceName; }
    public String getDistrictCode() { return toStr(districtCode); }
    public String getDistrictName() { return districtName; }
    public String getSectorCode() { return toStr(sectorCode); }
    public String getSectorName() { return sectorName; }
    public String getCellCode() { return toStr(cellCode); }
    public String getCellName() { return cellName; }
    public String getVillageCode() { return toStr(villageCode); }
    public String getVillageName() { return villageName; }

    private static String toStr(Object o) {
        return o == null ? "" : String.valueOf(o);
    }
}
