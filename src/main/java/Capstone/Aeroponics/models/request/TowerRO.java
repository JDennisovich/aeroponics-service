package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.models.enums.TowerStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public record TowerRO(
        Long id,
        @NotNull(message = "User is mandatory") User user,
        @NotNull(message = "Plant is mandatory") Plant plant,
        @NotNull(message = "Name is mandatory") String name,
        @NotNull(message = "Intervals is mandatory") int intervals,
        @NotNull(message = "Start date is mandatory") LocalDate start_date,
        @NotNull(message = "End date is mandatory") LocalDate end_date,
        @NotNull(message = "Start time is mandatory") LocalTime start_time,
        @NotNull(message = "End time is mandatory") LocalTime end_time,
        Integer watering_duration, // Optional, defaults to 15
        TowerStatus status // Optional status field
) {
    public Tower toEntity(Tower tower) {
        if (tower == null) {
            tower = new Tower();
        }

        if (id != null) {
            tower.setId(id);
        }

        tower.setUser(user);
        tower.setPlant(plant);
        tower.setName(name);
        tower.setStatus(Objects.nonNull(status) ? status : TowerStatus.ACTIVE);
        tower.setIntervals(intervals);
        tower.setStart_date(start_date);
        tower.setEnd_date(end_date);
        tower.setStart_time(start_time);
        tower.setEnd_time(end_time);
        tower.setWatering_duration(Objects.nonNull(watering_duration) ? watering_duration : 15);

        return tower;
    }
}
