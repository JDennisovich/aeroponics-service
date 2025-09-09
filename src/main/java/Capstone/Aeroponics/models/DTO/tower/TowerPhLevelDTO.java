package Capstone.Aeroponics.models.DTO.tower;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TowerPhLevelDTO {

    @Id
    private long id;
    private int phLevel;
    private LocalTime time;

    public TowerPhLevelDTO(int phLevel, LocalTime time) {
        this.phLevel = phLevel;
        this.time = time;
    }
}
