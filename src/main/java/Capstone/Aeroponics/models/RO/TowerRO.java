package Capstone.Aeroponics.models.RO;

import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.Tower;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TowerRO(
    int id,
    @NotNull(message = "User ID is mandatory") long user_id,
    @NotNull(message = "Plant is mandatory") Plant plant,
    @NotNull(message = "Time is mandatory") LocalTime time,
    @NotNull(message = "Frequency is mandatory") int frequency
) {
    public Tower toEntity(Tower tower) {
        if (tower == null) {
            tower = new Tower();
        }
        tower.setUser_id(user_id);
        tower.setPlant(plant);
        tower.setTime(time);
        tower.setFrequency(frequency);

        return tower;
    }
}