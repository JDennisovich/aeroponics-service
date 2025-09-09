package Capstone.Aeroponics.models.DTO.tower;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TowerPpmDTO {

    @Id
    private long id;
    private int ppm;
    private LocalTime time;

    public TowerPpmDTO(int ppm, LocalTime time) {
        this.ppm = ppm;
        this.time = time;
    }
}
