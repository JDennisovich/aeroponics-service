package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.models.entities.About;
import Capstone.Aeroponics.models.request.AboutRO;
import Capstone.Aeroponics.services.AboutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/about")
@RequiredArgsConstructor
public class AboutController {

    private final AboutService aboutService;

    // Get all about entries
    @GetMapping
    public ResponseEntity<List<About>> getAllAbout() {
        List<About> aboutList = aboutService.getAllAbout();
        return ResponseEntity.ok(aboutList);
    }

    // Get about by ID
    @GetMapping("/{id}")
    public ResponseEntity<About> getAboutById(@PathVariable Long id) {
        return aboutService.getAboutById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new about entry
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAbout(@Valid @RequestBody AboutRO aboutRO) {
        try {
            About about = aboutRO.toEntity(null);
            About createdAbout = aboutService.createAbout(about);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "About entry created successfully");
            response.put("data", createdAbout);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create about entry: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update about entry
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAbout(@PathVariable Long id, @Valid @RequestBody AboutRO aboutRO) {
        try {
            About existingAbout = aboutService.getAboutById(id)
                    .orElseThrow(() -> new RuntimeException("About entry not found with id: " + id));
            
            About about = aboutRO.toEntity(existingAbout);
            About updatedAbout = aboutService.updateAbout(id, about);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "About entry updated successfully");
            response.put("data", updatedAbout);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update about entry: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete about entry
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAbout(@PathVariable Long id) {
        try {
            aboutService.deleteAbout(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "About entry deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete about entry: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
