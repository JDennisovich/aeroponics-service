package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Plant;
import jakarta.validation.constraints.NotBlank;

public record PlantRO(
    int id,
    @NotBlank(message = "Name is mandatory") String name,
    @NotBlank(message = "ph_level is mandatory") int ph_level,
    @NotBlank(message = "ppm is mandatory") int ppm, // Updated field name
    @NotBlank(message = "user_id is mandatory") int user_id
) {
    public Plant toEntity(Plant plant) {
        if (plant == null) {
            plant = new Plant();
        }
        plant.setName(name);
        plant.setPh_level(ph_level);
        plant.setPpm(ppm); 
        plant.setUser_id(user_id);

        return plant;
    }
}