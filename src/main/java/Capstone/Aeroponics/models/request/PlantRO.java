package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.User;
import jakarta.validation.constraints.NotBlank;

public record PlantRO(
    int id,
    @NotBlank(message = "Name is mandatory") String name,
    @NotBlank(message = "ph_level is mandatory") int ph_level,
    @NotBlank(message = "ppm is mandatory") int ppm // Updated field name
) {
    public Plant toEntity(Plant plant,User user) {
        if (plant == null) {
            plant = new Plant();
        }
        plant.setName(name);
        plant.setPh_level(ph_level);
        plant.setPpm(ppm); 
        plant.setUser(user);

        return plant;
    }
}