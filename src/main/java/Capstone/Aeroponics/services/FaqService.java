package Capstone.Aeroponics.services;

import Capstone.Aeroponics.models.entities.Faq;
import Capstone.Aeroponics.repositories.FaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqService {

    private final FaqRepository faqRepository;

    // Get all FAQs
    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    // Get FAQ by ID
    public Optional<Faq> getFaqById(Long id) {
        return faqRepository.findById(id);
    }

    // Create new FAQ
    public Faq createFaq(Faq faq) {
        log.info("Creating new FAQ with question: {}", faq.getQuestion());
        return faqRepository.save(faq);
    }

    // Update FAQ
    public Faq updateFaq(Long id, Faq faqDetails) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        
        faq.setQuestion(faqDetails.getQuestion());
        faq.setAnswer(faqDetails.getAnswer());
        
        log.info("Updating FAQ with id: {}", id);
        return faqRepository.save(faq);
    }

    // Delete FAQ
    public void deleteFaq(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id: " + id));
        
        log.info("Deleting FAQ with id: {}", id);
        faqRepository.delete(faq);
    }
}
