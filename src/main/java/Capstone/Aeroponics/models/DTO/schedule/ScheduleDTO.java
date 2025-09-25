package Capstone.Aeroponics.models.DTO.schedule;

import Capstone.Aeroponics.models.entities.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private long id;
    private LocalTime startTime;

    public ScheduleDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.startTime = schedule.getStart_time();
    }
}
