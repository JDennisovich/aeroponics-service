package Capstone.Aeroponics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.image.directory}")
    private String imageDirectory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Maps URLs like http://localhost:8080/resources/{filename}
        // to files in the configured image directory
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("file:" + imageDirectory);
    }
}
