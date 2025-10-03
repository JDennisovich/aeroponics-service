package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Faq;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FaqRO(
        Long id,
        @NotBlank(message = "Question is mandatory") String question,
        @NotBlank(message = "Answer is mandatory") String answer
) {
    public Faq toEntity(Faq faq) {
        if (faq == null) {
            faq = new Faq();
        }
        faq.setQuestion(question);
        faq.setAnswer(answer);
        
        return faq;
    }
}
