package SecureOnlineExamination.Secure_Online_Examination_System.dto;

import lombok.Data;

/**
 * Request for creating a user. Location is specified by villageCode OR villageName only.
 */
@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String role;
    private Boolean active;
    private String villageCode;
    private String villageName;
}
