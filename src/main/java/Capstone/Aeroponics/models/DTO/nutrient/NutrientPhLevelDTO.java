package Capstone.Aeroponics.models.DTO.nutrient;

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
public class NutrientPhLevelDTO {

    @Id
    private long id;
    private BigDecimal phLevel;
    private LocalTime time;

    public NutrientPhLevelDTO(Nutrient_log nutrientLog) {
        this.id = nutrientLog.getId();
        this.phLevel = nutrientLog.getPh_level();
        this.time = nutrientLog.getTime();
    }
}
