package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.Schedule;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.entities.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record TowerRO(
        Long id,
        @NotNull(message = "User is mandatory") User user,
        @NotNull(message = "Plant is mandatory") Plant plant,
        @NotNull(message = "Name is mandatory") String name,
        @NotNull(message = "Frequency is mandatory") int frequency,
        @NotNull(message = "Start date is mandatory") LocalDate start_date,
        @NotNull(message = "End date is mandatory") LocalDate end_date,
        @NotNull(message = "Watering duration is mandatory") Integer watering_duration,
        Boolean status, // Optional status field
        List<ScheduleRO> schedules // ✅ Nested schedules
) {
    public Tower toEntity(Tower tower) {
        if (tower == null) {
            tower = new Tower();
        }

        if (id != null) {
            tower.setId(id);
        }

        tower.setUser(user);
        tower.setPlant(plant);
        tower.setName(name);
        tower.setStatus(Objects.nonNull(status) ? status : true); // Use provided status or default to true
        tower.setFrequency(frequency);
        tower.setStart_date(start_date);
        tower.setEnd_date(end_date);
        tower.setWatering_duration(watering_duration);

        // ✅ Handle nested schedules safely
        if (schedules != null && !schedules.isEmpty()) {
            final Tower finalTower = tower;

            List<Schedule> scheduleEntities = schedules.stream()
                    .filter(Objects::nonNull) // skip null ScheduleRO
                    .map(scheduleRO -> {
                        Schedule schedule = scheduleRO.toEntity(new Schedule());

                        // safeguard against null start_time
                        if (schedule.getStart_time() == null) {
                            return null; // skip invalid schedule
                        }

                        schedule.setTower(finalTower); // link back to tower
                        return schedule;
                    })
                    .filter(Objects::nonNull) // ensure only valid schedules
                    .collect(Collectors.toList());

            tower.setSchedules(scheduleEntities);
        }

        return tower;
    }
}
