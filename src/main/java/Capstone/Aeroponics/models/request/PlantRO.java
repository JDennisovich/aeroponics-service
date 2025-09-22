package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.User;
import jakarta.validation.constraints.NotBlank;

public record PlantRO(
    int id,
    @NotBlank(message = "Name is mandatory") String name,
    @NotBlank(message = "Minimum pH level is mandatory") int min_ph_level,
    @NotBlank(message = "Max pH level is mandatory") int max_ph_level,
    @NotBlank(message = "Minimum ppm is mandatory") int min_ppm, // Updated field name
    @NotBlank(message = "Max ppm is mandatory") int max_ppm

) {
    public Plant toEntity(Plant plant,User user) {
        if (plant == null) {
            plant = new Plant();
        }
        plant.setName(name);
        plant.setMin_ph_level(min_ph_level);
        plant.setMax_ph_level(max_ph_level);
        plant.setMin_ppm(min_ppm); // Updated field name
        plant.setMax_ppm(max_ppm); // Updated field name
        plant.setUser(user);

        return plant;
    }
}