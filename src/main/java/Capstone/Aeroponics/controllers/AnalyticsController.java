package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.services.AnalyticsService;
import Capstone.Aeroponics.services.WaterDepletionService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final WaterDepletionService waterDepletionService;

    /**
     * Get nutrient depletion analysis for a specific tower
     */
    @GetMapping("/nutrient-depletion/tower/{towerId}")
    public ResponseEntity<?> getNutrientDepletionByTower(@PathVariable Long towerId) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage("Nutrient Depletion Analysis"),
                analyticsService.getNutrientDepletionByTowerId(towerId)
            )
        );
    }

    /**
     * Get water depletion analysis for a specific tower
     */
    @GetMapping("/water-depletion/tower/{towerId}")
    public ResponseEntity<?> getWaterDepletionByTower(@PathVariable Long towerId) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage("Water Depletion Analysis"),
                waterDepletionService.getWaterDepletionByTowerId(towerId)
            )
        );
    }
}
