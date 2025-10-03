package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.About;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AboutRO(
        Long id,
        @NotBlank(message = "Title is mandatory") String title,
        @NotBlank(message = "Content is mandatory") String content
) {
    public About toEntity(About about) {
        if (about == null) {
            about = new About();
        }
        about.setTitle(title);
        about.setContent(content);
        
        return about;
    }
}
