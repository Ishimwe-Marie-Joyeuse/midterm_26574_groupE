package SecureOnlineExamination.Secure_Online_Examination_System.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Location import disabled. Add locations via Postman (POST /api/locations/parent, POST /api/locations/child).
 */
@Component
public class LocationImportRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        // No auto-import; add locations manually via API
    }
}
