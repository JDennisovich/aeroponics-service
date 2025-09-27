package Capstone.Aeroponics.models.DTO.nutrient;

import Capstone.Aeroponics.models.DTO.tower.TowerDTO;
import Capstone.Aeroponics.models.entities.Nutrient_log;
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
    private TowerDTO tower;
    private LocalTime time;
    private BigDecimal ph_level;
    private BigDecimal ppm;

    public NutrientDTO(Nutrient_log nutrientLog) {
        this.id = nutrientLog.getId();
        this.tower = new TowerDTO(nutrientLog.getTower());
        this.time = nutrientLog.getTime();
        this.ph_level = nutrientLog.getPh_level();
        this.ppm = nutrientLog.getPpm();
    }

    //TODO: ADD CONFIRM PASSWORD TO USER REGISTER
}