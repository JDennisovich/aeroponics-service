package Capstone.Aeroponics.models.DTO.tower;

import Capstone.Aeroponics.models.DTO.plant.PlantDTO;
import Capstone.Aeroponics.models.DTO.user.UserDTO;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.enums.TowerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TowerDTO {

    private long id;
    private UserDTO user;
    private PlantDTO plant;
    private String name;
    private TowerStatus status;
    private Integer wateringDuration;
    private int intervals;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public TowerDTO(Tower tower) {
        this.id = tower.getId();
        this.user = new UserDTO(tower.getUser());
        this.plant = new PlantDTO(tower.getPlant());
        this.name = tower.getName();
        this.status = tower.getStatus();
        this.wateringDuration = tower.getWatering_duration();
        this.intervals = tower.getIntervals();
        this.startDate = tower.getStart_date();
        this.endDate = tower.getEnd_date();
        this.startTime = tower.getStart_time();
        this.endTime = tower.getEnd_time();
    }
}
