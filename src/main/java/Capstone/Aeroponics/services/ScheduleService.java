package Capstone.Aeroponics.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import Capstone.Aeroponics.models.DTO.schedule.ScheduleDTO;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.request.ScheduleRO;
import Capstone.Aeroponics.repositories.TowerRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Capstone.Aeroponics.exception.ResourceNotFoundException;
import Capstone.Aeroponics.models.entities.Schedule;
import Capstone.Aeroponics.repositories.ScheduleRepository;
import Capstone.Aeroponics.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    public static final String SCHEDULES = "Schedules";
    public static final String SCHEDULE = "Schedule";
    
    private final ScheduleRepository scheduleRepository;
    private final TowerRepository towerRepository;

    public List<ScheduleDTO> getAll() {
        try {
            List<Schedule> schedules = scheduleRepository.findAll();

            List<ScheduleDTO> scheduleDTOs = schedules.stream()
                    .map(ScheduleDTO::new)
                    .toList();

            log.info(SCHEDULE + " found: " + scheduleDTOs.size());
            return scheduleDTOs;
        } catch (Exception e) {
            String errorMessage = "Error while getting " + SCHEDULES;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public Optional<Schedule> getById(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }

        return scheduleRepository.findById(id);
    }

    public Schedule getScheduleById(Long id) {
        try {
            Optional<Schedule> schedule = getById(id);

            if (schedule.isEmpty()) {
                throw new Exception("Schedule not found.");
            }
            log.info(SCHEDULE + " found.");
            return schedule.get();
        } catch (Exception e) {
            String errorMessage = "Error while getting " + SCHEDULE;
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void save(ScheduleRO scheduleRO, Long towerId) {
        try {
            Tower tower = towerRepository.findById(towerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tower not found"));

            Schedule schedule = scheduleRO.toEntity(null);
            schedule.setTower(tower);

            scheduleRepository.save(schedule);

        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(SCHEDULE);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void update(Long id, ScheduleRO scheduleRO, Long towerId) {
        try {
            Schedule schedule = getScheduleById(id);

            if (Objects.isNull(schedule)) {
                throw new ResourceNotFoundException("Schedule not found");
            }

            Tower tower = towerRepository.findById(towerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tower not found"));

            Schedule updatedSchedule = scheduleRO.toEntity(schedule);
            updatedSchedule.setTower(tower);

            scheduleRepository.save(updatedSchedule);

        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(SCHEDULE);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void delete(Long id) {
        try {
            Schedule schedule = getScheduleById(id);

            if (Objects.isNull(schedule)) {
                throw new ResourceNotFoundException("Schedule not found");
            }

            scheduleRepository.delete(schedule);
        } catch (Exception e) {
            String errorMessage = MessageUtils.deleteErrorMessage(SCHEDULE);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }
}
