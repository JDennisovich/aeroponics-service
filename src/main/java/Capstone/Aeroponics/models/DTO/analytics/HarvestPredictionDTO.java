package Capstone.Aeroponics.models.DTO.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HarvestPredictionDTO {
    
    private Long towerId;
    private String towerName;
    private String plantName;
    private LocalDate startDate;
    private LocalDate predictedHarvestDate;
    private LocalDate endDate;
    private int daysUntilHarvest;
    private double growthProgress; // Percentage (0-100)
    private String healthStatus; // EXCELLENT, GOOD, FAIR, POOR
    private double averagePh;
    private double averagePpm;
    private boolean isOptimalConditions;
    private String recommendation;
}
