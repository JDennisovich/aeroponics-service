package Capstone.Aeroponics.models.DTO.nutrient;

import Capstone.Aeroponics.models.entities.Nutrient_log;
import Capstone.Aeroponics.models.enums.WaterLevel;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutrientDTO {

    @Id
    private long id;
    private TowerInfo tower;
    private LocalTime time;
    private BigDecimal ph_level;
    private BigDecimal ppm;
    private WaterLevel water_level;

    public NutrientDTO(Nutrient_log nutrientLog) {
        this.id = nutrientLog.getId();
        this.tower = new TowerInfo(nutrientLog.getTower());
        this.time = nutrientLog.getTime();
        this.ph_level = nutrientLog.getPh_level();
        this.ppm = nutrientLog.getPpm();
        this.water_level = nutrientLog.getWater_level();
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TowerInfo {
        private long id;
        private String name;
        private PlantInfo plant;

        public TowerInfo(Capstone.Aeroponics.models.entities.Tower tower) {
            this.id = tower.getId();
            this.name = tower.getName();
            this.plant = new PlantInfo(tower.getPlant());
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlantInfo {
        private long id;
        private String name;

        public PlantInfo(Capstone.Aeroponics.models.entities.Plant plant) {
            this.id = plant.getId();
            this.name = plant.getName();
        }
    }
}