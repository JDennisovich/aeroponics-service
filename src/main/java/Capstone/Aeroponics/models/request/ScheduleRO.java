package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Schedule;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ScheduleRO (
    int id,
    @NotNull(message = "Tower id is mandatory") int tower_id,
    @NotNull(message = "Start hour is mandatory") int start_hour,
    @NotNull(message = "Start minute is mandatory") int start_minute,
    @NotNull(message = "Duration is mandatory") int durationMinute,
    @NotNull(message = "Start date is mandatory") LocalDate start_date,
    @NotNull(message = "End date is mandatory") LocalDate end_date
) {
    public Schedule toEntity(Schedule schedule) {
        if (schedule == null) {
            schedule = new Schedule();
        }
        schedule.setStart_hour(start_hour);
        schedule.setStart_minute(start_minute);
        schedule.setDurationMinute(durationMinute);
        schedule.setStart_date(start_date);
        schedule.setEnd_date(end_date);
        return schedule;
    }
}
