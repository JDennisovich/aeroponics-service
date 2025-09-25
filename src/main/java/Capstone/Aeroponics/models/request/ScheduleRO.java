package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Schedule;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ScheduleRO(
        long id,
        @NotNull(message = "Start hour is mandatory") LocalTime start_time
) {
    public Schedule toEntity(Schedule schedule) {
        if (schedule == null) {
            schedule = new Schedule();
        }
        schedule.setStart_time(start_time);
        return schedule;
    }
}
