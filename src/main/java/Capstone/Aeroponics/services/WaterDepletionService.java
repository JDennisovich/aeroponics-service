package Capstone.Aeroponics.services;

import Capstone.Aeroponics.models.DTO.analytics.WaterDepletionDTO;
import Capstone.Aeroponics.models.entities.Nutrient_log;
import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.repositories.Nutrient_logRepository;
import Capstone.Aeroponics.repositories.TowerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Water Depletion Analytics Service
 * 
 * This service analyzes water level changes from nutrient logs to predict water depletion patterns.
 * Water level is stored as integer (1-10) where:
 * - 1-3 = High (80-100%)
 * - 4-7 = Medium (40-70%)
 * - 8-10 = Low (10-30%)
 * 
 * It calculates:
 * - Current water level from the most recent nutrient log entry
 * - Water depletion rates based on historical water level trends
 * - Days until water reaches critical level
 * - Status indicators and actionable recommendations
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WaterDepletionService {

    private final TowerRepository towerRepository;
    private final Nutrient_logRepository nutrientLogRepository;

    // Critical threshold for water level (20% = value 8-9)
    private static final double CRITICAL_WATER_PERCENTAGE = 20.0;
    
    /**
     * Convert raw water level (1-10) to percentage
     * Formula: (11 - value) * 10
     * 1 = 100%, 2 = 90%, 3 = 80%, ..., 9 = 20%, 10 = 10%
     */
    private double convertWaterLevelToPercentage(int waterLevel) {
        if (waterLevel < 1 || waterLevel > 10) {
            log.warn("Water level out of range: {}. Using default 50%", waterLevel);
            return 50.0; // Default to medium
        }
        return (11 - waterLevel) * 10.0;
    }
    
    /**
     * Get water level category text for display
     */
    private String getWaterLevelText(int waterLevel) {
        if (waterLevel >= 1 && waterLevel <= 3) return "High";
        if (waterLevel >= 4 && waterLevel <= 7) return "Medium";
        if (waterLevel >= 8 && waterLevel <= 10) return "Low";
        return "Unknown";
    }

    /**
     * Get water depletion analysis for a specific tower
     */
    public WaterDepletionDTO getWaterDepletionByTowerId(Long towerId) {
        Tower tower = towerRepository.findById(towerId)
                .orElseThrow(() -> new RuntimeException("Tower not found"));
        
        return calculateWaterDepletion(tower);
    }

    /**
     * Calculate water depletion analysis for a tower
     */
    private WaterDepletionDTO calculateWaterDepletion(Tower tower) {
        Plant plant = tower.getPlant();
        
        // Get all nutrient logs for this tower, ordered by time (most recent first)
        List<Nutrient_log> allLogs = nutrientLogRepository.findByTowerIdOrderByTimeDesc(tower.getId());

        if (allLogs.isEmpty()) {
            return buildNoDataResponse(tower, plant);
        }

        // Use large dataset for analysis (up to 10,000 readings for maximum accuracy)
        // This provides several months of historical data for better trend prediction
        List<Nutrient_log> recentLogs = allLogs.stream()
                .limit(10000)
                .collect(Collectors.toList());

        // Get current water level from the most recent log entry
        Nutrient_log latestLog = recentLogs.get(0);
        int currentWaterLevelRaw = latestLog.getWater_level();
        double currentWaterPercentage = convertWaterLevelToPercentage(currentWaterLevelRaw);
        String currentWaterLevelText = getWaterLevelText(currentWaterLevelRaw);
        
        log.info("Analyzing water depletion for tower {} - Current Water Level: {} [Raw: {}] ({}%), Total logs: {}", 
                 tower.getId(), currentWaterLevelText, currentWaterLevelRaw, currentWaterPercentage, recentLogs.size());

        // Calculate water depletion rate
        double waterDepletionRate = calculateWaterDepletionRate(recentLogs);
        
        // Calculate days until critical (LOW level)
        int daysUntilCritical = calculateDaysUntilCritical(currentWaterPercentage, waterDepletionRate);

        // Determine status
        String waterStatus = determineWaterStatus(currentWaterPercentage, daysUntilCritical);
        String overallStatus = determineOverallStatus(waterStatus);

        // Calculate average change
        double avgWaterChange = calculateAverageWaterChange(recentLogs);

        // Generate recommendation
        String recommendation = generateRecommendation(
            currentWaterPercentage, currentWaterLevelText, waterDepletionRate, daysUntilCritical
        );

        boolean needsImmediateAction = waterStatus.equals("CRITICAL");

        return WaterDepletionDTO.builder()
                .towerId(tower.getId())
                .towerName(tower.getName())
                .plantName(plant.getName())
                .currentWaterLevel(String.valueOf(currentWaterLevelRaw))
                .currentWaterPercentage(Math.round(currentWaterPercentage * 10.0) / 10.0)
                .waterDepletionRate(Math.round(waterDepletionRate * 100.0) / 100.0)
                .daysUntilCritical(daysUntilCritical)
                .waterStatus(waterStatus)
                .overallStatus(overallStatus)
                .avgWaterChange(Math.round(avgWaterChange * 100.0) / 100.0)
                .totalReadings(recentLogs.size())
                .recommendation(recommendation)
                .needsImmediateAction(needsImmediateAction)
                .build();
    }

    /**
     * Build response when no data is available
     */
    private WaterDepletionDTO buildNoDataResponse(Tower tower, Plant plant) {
        return WaterDepletionDTO.builder()
                .towerId(tower.getId())
                .towerName(tower.getName())
                .plantName(plant.getName())
                .currentWaterLevel("UNKNOWN")
                .currentWaterPercentage(0)
                .waterDepletionRate(0)
                .daysUntilCritical(999)
                .waterStatus("NO_DATA")
                .overallStatus("NO_DATA")
                .avgWaterChange(0)
                .totalReadings(0)
                .recommendation("No sensor data available. Please check your monitoring system.")
                .needsImmediateAction(false)
                .build();
    }

    /**
     * Calculate water depletion rate (percentage change per day)
     * Uses weighted moving average with outlier filtering for maximum accuracy
     * Trained on up to 10,000 data points for robust predictions
     */
    private double calculateWaterDepletionRate(List<Nutrient_log> logs) {
        if (logs.size() < 2) return 0;

        // Convert water levels (1-10) to percentages
        List<Double> waterPercentages = logs.stream()
            .map(log -> convertWaterLevelToPercentage(log.getWater_level()))
            .collect(Collectors.toList());

        // Filter outliers for better accuracy
        List<Double> filteredPercentages = filterOutliers(waterPercentages);
        
        if (filteredPercentages.size() < 2) {
            filteredPercentages = waterPercentages;
        }

        // Use weighted moving average for recent trends (last 20% of data gets more weight)
        int recentDataSize = Math.max(10, filteredPercentages.size() / 5);
        double recentAvg = filteredPercentages.stream()
            .limit(recentDataSize)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);
        
        int oldDataSize = Math.max(10, filteredPercentages.size() / 5);
        double oldAvg = filteredPercentages.stream()
            .skip(Math.max(0, filteredPercentages.size() - oldDataSize))
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);

        // Calculate total change
        double totalChange = recentAvg - oldAvg;
        
        // Estimate time span based on number of log entries
        // Assuming logs are recorded periodically (e.g., every 2-4 hours)
        // With 10,000 logs, this represents several months of data
        double estimatedDays = Math.max(1, logs.size() / 8.0); // ~8 readings per day
        
        // Calculate rate of change per day
        double ratePerDay = totalChange / estimatedDays;
        
        // Apply exponential smoothing for stability
        double smoothedRate = applyExponentialSmoothing(logs, ratePerDay);
        
        log.debug("Water depletion rate - Recent avg: {}%, Old avg: {}%, Change: {}%, Days: {}, Raw rate: {}%, Smoothed rate: {}%", 
                  recentAvg, oldAvg, totalChange, estimatedDays, ratePerDay, smoothedRate);
        
        return smoothedRate;
    }

    /**
     * Filter outliers using Interquartile Range (IQR) method
     * Only applied when we have sufficient data (>100 points)
     */
    private List<Double> filterOutliers(List<Double> values) {
        if (values.size() < 100) return values;

        List<Double> sorted = values.stream().sorted().collect(Collectors.toList());
        int q1Index = sorted.size() / 4;
        int q3Index = (sorted.size() * 3) / 4;
        
        double q1 = sorted.get(q1Index);
        double q3 = sorted.get(q3Index);
        double iqr = q3 - q1;
        
        double lowerBound = q1 - (1.5 * iqr);
        double upperBound = q3 + (1.5 * iqr);
        
        return values.stream()
            .filter(v -> v >= lowerBound && v <= upperBound)
            .collect(Collectors.toList());
    }

    /**
     * Apply exponential smoothing to reduce noise in water level predictions
     * Alpha = 0.3 provides good balance between responsiveness and stability
     */
    private double applyExponentialSmoothing(List<Nutrient_log> logs, double currentRate) {
        if (logs.size() < 10) return currentRate;

        // Calculate rates for recent segments
        double sum = 0;
        int segments = Math.min(5, logs.size() / 20);
        
        for (int i = 0; i < segments; i++) {
            int start = i * (logs.size() / segments);
            int end = Math.min((i + 1) * (logs.size() / segments), logs.size());
            
            if (end - start < 2) continue;
            
            double segmentStart = convertWaterLevelToPercentage(logs.get(end - 1).getWater_level());
            double segmentEnd = convertWaterLevelToPercentage(logs.get(start).getWater_level());
            
            double segmentChange = segmentEnd - segmentStart;
            double segmentDays = (end - start) / 8.0;
            double segmentRate = segmentChange / Math.max(1, segmentDays);
            
            sum += segmentRate;
        }
        
        double avgHistoricalRate = segments > 0 ? sum / segments : currentRate;
        
        // Exponential smoothing: 70% current rate, 30% historical average
        return (0.7 * currentRate) + (0.3 * avgHistoricalRate);
    }

    /**
     * Calculate average water level change between consecutive readings
     */
    private double calculateAverageWaterChange(List<Nutrient_log> logs) {
        if (logs.size() < 2) return 0;

        double sum = 0;
        int count = 0;

        // Calculate change between each consecutive pair
        for (int i = 0; i < logs.size() - 1; i++) {
            double currentPercentage = convertWaterLevelToPercentage(logs.get(i).getWater_level());
            double previousPercentage = convertWaterLevelToPercentage(logs.get(i + 1).getWater_level());
            
            sum += (currentPercentage - previousPercentage);
            count++;
        }

        double avgChange = count > 0 ? sum / count : 0;
        
        log.debug("Average water change per reading: {}%", avgChange);
        
        return avgChange;
    }

    /**
     * Calculate days until water reaches critical level (< 20%)
     */
    private int calculateDaysUntilCxritical(double currentPercentage, double depletionRate) {
        // If already at critical level
        if (currentPercentage <= CRITICAL_WATER_PERCENTAGE) {
            return 0;
        }

        // If water is not depleting (rate is 0 or positive)
        if (depletionRate >= 0) {
            return 999; // Water is stable or increasing
        }

        // Calculate days until reaching critical level (20%)
        double criticalThreshold = CRITICAL_WATER_PERCENTAGE;
        double difference = currentPercentage - criticalThreshold;
        int daysUntilCritical = (int) Math.ceil(difference / Math.abs(depletionRate));
        
        log.debug("Days until critical - Current: {}%, Rate: {}%/day, Critical threshold: {}%, Result: {} days", 
                  currentPercentage, depletionRate, criticalThreshold, daysUntilCritical);

        return Math.max(0, daysUntilCritical);
    }

    /**
     * Determine water status based on current percentage and days until critical
     */
    private String determineWaterStatus(double currentPercentage, int daysUntilCritical) {
        // Check if at critical level (< 20% = values 9-10)
        if (currentPercentage <= CRITICAL_WATER_PERCENTAGE) {
            return "CRITICAL";
        }

        // Check if approaching critical soon or at medium-low level (20-40% = values 7-8)
        if (currentPercentage <= 40.0 || daysUntilCritical <= 3) {
            return "WARNING";
        }

        // High level (> 40% = values 1-6) and not approaching critical soon
        return "OPTIMAL";
    }

    /**
     * Determine overall status
     */
    private String determineOverallStatus(String waterStatus) {
        if (waterStatus.equals("CRITICAL")) {
            return "CRITICAL";
        }
        if (waterStatus.equals("WARNING")) {
            return "ATTENTION_NEEDED";
        }
        if (waterStatus.equals("OPTIMAL")) {
            return "EXCELLENT";
        }
        return "GOOD";
    }

    /**
     * Generate recommendation based on water level analysis
     */
    private String generateRecommendation(double currentPercentage, String levelText, double depletionRate, int daysUntilCritical) {
        StringBuilder rec = new StringBuilder();
        
        log.info("Generating recommendation - Water Level: {} ({}%), Depletion rate: {}%/day, Days until critical: {}", 
                 levelText, currentPercentage, depletionRate, daysUntilCritical);

        // Check current water level
        if (currentPercentage <= CRITICAL_WATER_PERCENTAGE) { // < 20%
            rec.append("⚠️ Water level is LOW. Refill the reservoir immediately to prevent system damage. ");
        } else if (currentPercentage <= 40.0) { // 20-40%
            if (depletionRate < -5) {
                rec.append("⚠️ Water level is MEDIUM and depleting rapidly. Plan to refill soon. ");
            } else {
                rec.append("📊 Water level is MEDIUM. Monitor and prepare to refill. ");
            }
        } else { // > 40%
            if (daysUntilCritical <= 3 && daysUntilCritical > 0) {
                rec.append("⚠️ Water is depleting rapidly. Prepare to refill within the next few days. ");
            } else if (depletionRate < -3) {
                rec.append("📉 Water level is gradually decreasing. Monitor consumption patterns. ");
            } else {
                rec.append("✅ Water level is optimal. Continue current maintenance schedule. ");
            }
        }

        // Additional recommendations based on depletion rate
        if (depletionRate < -10) {
            rec.append("🔍 High water consumption detected. Check for leaks or adjust watering schedule. ");
        } else if (depletionRate > 5) {
            rec.append("📈 Water level increasing. Check for proper drainage or sensor calibration. ");
        }

        // If everything is good and no specific recommendation was added
        if (rec.length() == 0) {
            rec.append("✅ Water level is stable and optimal. Continue monitoring.");
        }

        return rec.toString().trim();
    }
}
