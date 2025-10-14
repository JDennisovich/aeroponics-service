package Capstone.Aeroponics.models.DTO.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for water depletion analytics
 * Tracks water level changes and predicts when water will reach critical levels
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterDepletionDTO {
    
    // Tower information
    private Long towerId;
    private String towerName;
    private String plantName;
    
    // Current water status
    private String currentWaterLevel;  // HIGH, MEDIUM, or LOW
    private double currentWaterPercentage;  // Estimated percentage
    
    // Water depletion metrics
    private double waterDepletionRate;  // Change in percentage per day
    private int daysUntilCritical;  // Days until water reaches LOW level
    
    // Status indicators
    private String waterStatus;  // OPTIMAL, WARNING, CRITICAL
    private String overallStatus;  // EXCELLENT, GOOD, ATTENTION_NEEDED, CRITICAL
    
    // Additional metrics
    private double avgWaterChange;  // Average change per reading
    private int totalReadings;  // Number of readings analyzed
    
    // Recommendations
    private String recommendation;
    private boolean needsImmediateAction;
}
