package Capstone.Aeroponics.services;

import Capstone.Aeroponics.models.entities.About;
import Capstone.Aeroponics.repositories.AboutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AboutService {

    private final AboutRepository aboutRepository;

    // Get all about entries
    public List<About> getAllAbout() {
        return aboutRepository.findAll();
    }

    // Get about by ID
    public Optional<About> getAboutById(Long id) {
        return aboutRepository.findById(id);
    }

    // Create new about entry
    public About createAbout(About about) {
        log.info("Creating new about entry with title: {}", about.getTitle());
        return aboutRepository.save(about);
    }

    // Update about entry
    public About updateAbout(Long id, About aboutDetails) {
        About about = aboutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("About entry not found with id: " + id));
        
        about.setTitle(aboutDetails.getTitle());
        about.setContent(aboutDetails.getContent());
        
        log.info("Updating about entry with id: {}", id);
        return aboutRepository.save(about);
    }

    // Delete about entry
    public void deleteAbout(Long id) {
        About about = aboutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("About entry not found with id: " + id));
        
        log.info("Deleting about entry with id: {}", id);
        aboutRepository.delete(about);
    }
}
