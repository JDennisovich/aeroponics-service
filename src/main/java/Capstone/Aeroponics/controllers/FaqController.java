package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.models.entities.Faq;
import Capstone.Aeroponics.models.request.FaqRO;
import Capstone.Aeroponics.services.FaqService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    // Get all FAQs
    @GetMapping
    public ResponseEntity<List<Faq>> getAllFaqs() {
        List<Faq> faqList = faqService.getAllFaqs();
        return ResponseEntity.ok(faqList);
    }

    // Get FAQ by ID
    @GetMapping("/{id}")
    public ResponseEntity<Faq> getFaqById(@PathVariable Long id) {
        return faqService.getFaqById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new FAQ
    @PostMapping
    public ResponseEntity<Map<String, Object>> createFaq(@Valid @RequestBody FaqRO faqRO) {
        try {
            Faq faq = faqRO.toEntity(null);
            Faq createdFaq = faqService.createFaq(faq);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "FAQ created successfully");
            response.put("data", createdFaq);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create FAQ: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Update FAQ
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateFaq(@PathVariable Long id, @Valid @RequestBody FaqRO faqRO) {
        try {
            Faq existingFaq = faqService.getFaqById(id)
                    .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
            
            Faq faq = faqRO.toEntity(existingFaq);
            Faq updatedFaq = faqService.updateFaq(id, faq);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "FAQ updated successfully");
            response.put("data", updatedFaq);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update FAQ: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Delete FAQ
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFaq(@PathVariable Long id) {
        try {
            faqService.deleteFaq(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "FAQ deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete FAQ: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
