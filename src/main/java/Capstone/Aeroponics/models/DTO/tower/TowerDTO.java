package Capstone.Aeroponics.models.DTO.tower;

import Capstone.Aeroponics.models.DTO.plant.PlantDTO;
import Capstone.Aeroponics.models.DTO.user.UserDTO;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.enums.WaterLevel;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TowerDTO {

    @Id
    private long id;
    private UserDTO user;
    private PlantDTO plant;
    private LocalTime time;
    private WaterLevel waterLevel;
    private int frequency;
    private LocalDate startDate;
    private LocalDate endDate;

    public TowerDTO(Tower tower) {
        this.id = tower.getId();
        this.user = new UserDTO(tower.getUser());
        this.plant = new PlantDTO(tower.getPlant());
        this.time = tower.getTime();
        this.waterLevel = tower.getWaterLevel();
        this.frequency = tower.getFrequency();
        this.startDate = tower.getStart_date();
        this.endDate = tower.getEnd_date();
    }
}
