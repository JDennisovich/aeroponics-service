package Capstone.Aeroponics.models.RO;

import Capstone.Aeroponics.models.entities.Plant;
import jakarta.validation.constraints.NotBlank;

public record PlantRO(
    int id,
    @NotBlank(message =  "Name is mandatory") String name,
    @NotBlank(message =  "ph_level is mandatory") int ph_level,
    @NotBlank(message =  "tds is mandatory") int tds,
    @NotBlank(message =  "user_id is mandatory") int user_id
) {
    public Plant toEntity(Plant plant) {
        if (plant == null) {
            plant = new Plant();
        }
        plant.setName(name);
        plant.setPh_level(ph_level);
        plant.setTds(tds);
        plant.setUser_id(user_id);

        return plant;
    }
    //Note: paayos ng to entity
}