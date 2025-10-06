package Capstone.Aeroponics.models.DTO.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutrientDepletionDTO {
    
    private Long towerId;
    private String towerName;
    private String plantName;
    
    // Current nutrient levels
    private double currentPh;
    private double currentPpm;
    
    // Optimal ranges
    private double optimalPhMin;
    private double optimalPhMax;
    private double optimalPpmMin;
    private double optimalPpmMax;
    
    // Depletion analysis
    private double phDepletionRate; // pH change per day
    private double ppmDepletionRate; // PPM change per day
    private int daysUntilPhCritical; // Days until pH goes out of range
    private int daysUntilPpmCritical; // Days until PPM goes out of range
    
    // Status
    private String phStatus; // OPTIMAL, WARNING, CRITICAL
    private String ppmStatus; // OPTIMAL, WARNING, CRITICAL
    private String overallStatus; // EXCELLENT, GOOD, ATTENTION_NEEDED, CRITICAL
    
    // Recommendations
    private String recommendation;
    private boolean needsImmediateAction;
    
    // Trend data (last 7 days)
    private double avgPhChange;
    private double avgPpmChange;
}
