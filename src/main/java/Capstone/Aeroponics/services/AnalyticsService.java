package Capstone.Aeroponics.services;

import Capstone.Aeroponics.models.DTO.analytics.HarvestPredictionDTO;
import Capstone.Aeroponics.models.entities.Nutrient_log;
import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.repositories.Nutrient_logRepository;
import Capstone.Aeroponics.repositories.TowerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final TowerRepository towerRepository;
    private final Nutrient_logRepository nutrientLogRepository;

    /**
     * Get harvest predictions for all active towers of a user
     */
    public List<HarvestPredictionDTO> getHarvestPredictionsByUserId(Long userId) {
        List<Tower> activeTowers = towerRepository.findByUserId(userId).stream()
                .filter(Tower::getStatus)
                .collect(Collectors.toList());

        return activeTowers.stream()
                .map(this::calculateHarvestPrediction)
                .collect(Collectors.toList());
    }

    /**
     * Get harvest prediction for a specific tower
     */
    public HarvestPredictionDTO getHarvestPredictionByTowerId(Long towerId) {
        Tower tower = towerRepository.findById(towerId)
                .orElseThrow(() -> new RuntimeException("Tower not found"));
        
        return calculateHarvestPrediction(tower);
    }

    /**
     * Calculate harvest prediction for a tower
     */
    private HarvestPredictionDTO calculateHarvestPrediction(Tower tower) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = tower.getStart_date();
        LocalDate endDate = tower.getEnd_date();
        Plant plant = tower.getPlant();

        // Calculate growth progress
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        long daysPassed = ChronoUnit.DAYS.between(startDate, today);
        double growthProgress = Math.min(100.0, (daysPassed * 100.0) / totalDays);

        // Get recent nutrient data (last 7 days)
        List<Nutrient_log> recentLogs = nutrientLogRepository
                .findByTowerIdOrderByTimeDesc(tower.getId())
                .stream()
                .limit(20) // Get last 20 readings
                .collect(Collectors.toList());

        // Calculate average pH and PPM
        double avgPh = recentLogs.isEmpty() ? 0 : 
            recentLogs.stream()
                .mapToDouble(log -> log.getPh_level().doubleValue())
                .average()
                .orElse(0);

        double avgPpm = recentLogs.isEmpty() ? 0 :
            recentLogs.stream()
                .mapToDouble(log -> log.getPpm().doubleValue())
                .average()
                .orElse(0);

        // Check if conditions are optimal
        boolean isOptimalPh = avgPh >= plant.getMin_ph_level() && avgPh <= plant.getMax_ph_level();
        boolean isOptimalPpm = avgPpm >= plant.getMin_ppm() && avgPpm <= plant.getMax_ppm();
        boolean isOptimalConditions = isOptimalPh && isOptimalPpm && !recentLogs.isEmpty();

        // Determine health status
        String healthStatus = determineHealthStatus(isOptimalConditions, avgPh, avgPpm, plant);

        // Adjust predicted harvest date based on conditions
        LocalDate predictedHarvestDate = calculatePredictedHarvestDate(
            endDate, isOptimalConditions, growthProgress
        );

        long daysUntilHarvest = ChronoUnit.DAYS.between(today, predictedHarvestDate);

        // Generate recommendation
        String recommendation = generateRecommendation(
            isOptimalPh, isOptimalPpm, avgPh, avgPpm, plant, daysUntilHarvest
        );

        return HarvestPredictionDTO.builder()
                .towerId(tower.getId())
                .towerName(tower.getName())
                .plantName(plant.getName())
                .startDate(startDate)
                .predictedHarvestDate(predictedHarvestDate)
                .endDate(endDate)
                .daysUntilHarvest((int) daysUntilHarvest)
                .growthProgress(Math.round(growthProgress * 10.0) / 10.0)
                .healthStatus(healthStatus)
                .averagePh(Math.round(avgPh * 10.0) / 10.0)
                .averagePpm(Math.round(avgPpm * 10.0) / 10.0)
                .isOptimalConditions(isOptimalConditions)
                .recommendation(recommendation)
                .build();
    }

    /**
     * Determine health status based on conditions
     */
    private String determineHealthStatus(boolean isOptimal, double avgPh, double avgPpm, Plant plant) {
        if (avgPh == 0 || avgPpm == 0) {
            return "NO_DATA";
        }

        if (isOptimal) {
            return "EXCELLENT";
        }

        // Check how far off from optimal
        double phDeviation = Math.min(
            Math.abs(avgPh - plant.getMin_ph_level()),
            Math.abs(avgPh - plant.getMax_ph_level())
        );
        double ppmDeviation = Math.min(
            Math.abs(avgPpm - plant.getMin_ppm()),
            Math.abs(avgPpm - plant.getMax_ppm())
        );

        if (phDeviation <= 0.5 && ppmDeviation <= 100) {
            return "GOOD";
        } else if (phDeviation <= 1.0 && ppmDeviation <= 200) {
            return "FAIR";
        } else {
            return "POOR";
        }
    }

    /**
     * Calculate predicted harvest date with adjustments
     */
    private LocalDate calculatePredictedHarvestDate(
        LocalDate originalEndDate, 
        boolean isOptimal, 
        double growthProgress
    ) {
        if (isOptimal) {
            // Optimal conditions might speed up growth by 5%
            return originalEndDate.minusDays(2);
        } else if (growthProgress < 50) {
            // Poor conditions in early growth might delay harvest
            return originalEndDate.plusDays(3);
        } else {
            // Use original date
            return originalEndDate;
        }
    }

    /**
     * Generate recommendation based on conditions
     */
    private String generateRecommendation(
        boolean isOptimalPh,
        boolean isOptimalPpm,
        double avgPh,
        double avgPpm,
        Plant plant,
        long daysUntilHarvest
    ) {
        if (avgPh == 0 || avgPpm == 0) {
            return "No sensor data available. Please check your monitoring system.";
        }

        if (isOptimalPh && isOptimalPpm) {
            if (daysUntilHarvest <= 7) {
                return "Excellent conditions! Prepare for harvest within a week.";
            }
            return "Excellent conditions! Continue current nutrient schedule.";
        }

        StringBuilder recommendation = new StringBuilder();
        
        if (!isOptimalPh) {
            if (avgPh < plant.getMin_ph_level()) {
                recommendation.append("pH is too low. Add pH up solution. ");
            } else {
                recommendation.append("pH is too high. Add pH down solution. ");
            }
        }

        if (!isOptimalPpm) {
            if (avgPpm < plant.getMin_ppm()) {
                recommendation.append("PPM is too low. Increase nutrient concentration. ");
            } else {
                recommendation.append("PPM is too high. Dilute with water. ");
            }
        }

        if (recommendation.length() == 0) {
            recommendation.append("Monitor conditions closely.");
        }

        return recommendation.toString().trim();
    }
}
