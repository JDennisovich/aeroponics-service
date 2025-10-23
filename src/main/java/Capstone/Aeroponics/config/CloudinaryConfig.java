package Capstone.Aeroponics.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    // These values can come from application.properties or environment variables.
    @Value("${cloudinary.cloud_name:${CLOUDINARY_CLOUD_NAME:}}")
    private String cloudName;

    @Value("${cloudinary.api_key:${CLOUDINARY_API_KEY:}}")
    private String apiKey;

    @Value("${cloudinary.api_secret:${CLOUDINARY_API_SECRET:}}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        // IMPORTANT: Ensure the environment variables or properties are set at runtime.
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
}

