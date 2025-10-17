package Capstone.Aeroponics.services;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import Capstone.Aeroponics.models.DTO.device.DeviceDTO;
import Capstone.Aeroponics.models.DTO.tower.TowerDTO;
import Capstone.Aeroponics.models.entities.Nutrient_log;
import Capstone.Aeroponics.models.entities.Schedule;
import Capstone.Aeroponics.repositories.Nutrient_logRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import Capstone.Aeroponics.exception.ResourceNotFoundException;
import Capstone.Aeroponics.models.request.TowerRO;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.repositories.TowerRepository;
import Capstone.Aeroponics.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor

//Fix save of tower with schedules
//Integ of manage tower at save tower
public class TowerService {
    public static final String TOWERS = "Towers";

    public static final String TOWER = "Tower";

    private final TowerRepository towerRepository;

    private final Nutrient_logRepository Nutrient_logRepository;
    
    private final DeviceService deviceService;

    public List<TowerDTO> getAll() {
        try {
            List<Tower> towers = towerRepository.findAll();
            List<TowerDTO> towerDTOs = towers.stream()
                    .map(TowerDTO::new)
                    .toList();

            log.info(TOWERS + " found: " + towerDTOs.size());
            return towerDTOs;
        } catch (Exception e) {
            String errorMessage = "Error while getting " + TOWERS;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }


    public List<TowerDTO> getTowersByUserId(Long id) {
        try {
            List<Tower> towers = towerRepository.findByUserId(id);
            log.info(TOWERS + " found for userId " + id + ": " + towers.size());

            // Use the TowerDTO constructor
            return towers.stream()
                    .map(TowerDTO::new)
                    .toList(); // If on Java 8 → use .collect(Collectors.toList())

        } catch (Exception e) {
            String errorMessage = "Error while getting " + TOWERS + " for userId " + id;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }


    public Optional<Tower> getById(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }

        return towerRepository.findById(id);
    }

    public TowerDTO getTowerByIdDTO(Long id) {
        try {
            Optional<Tower> tower = getById(id);

            if (tower.isEmpty()) {
                throw new Exception(TOWER + " not found.");
            }

            log.info(TOWER + " found.");
            return new TowerDTO(tower.get());  // wrap with DTO
        } catch (Exception e) {
            String errorMessage = "Error while getting " + TOWER;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public Tower getTowerById(Long id) {
        try {
            Optional<Tower> tower = getById(id);

            if (tower.isEmpty()) {
                throw new Exception(TOWER + " not found.");
            }
            log.info(TOWER + " found.");
            return tower.get();
        } catch (Exception e) {
            String errorMessage = "Error while getting " + TOWER;
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public Tower save(TowerRO towerRO) {
        try {
            // Convert RO → Entity
            Tower tower = towerRO.toEntity(null);
            tower.setStatus(true);

            // Validate schedules count against frequency
            if (towerRO.schedules() != null) {
                int scheduleCount = towerRO.schedules().size();
                if (scheduleCount != towerRO.frequency()) {
                    throw new ServiceException(
                            "Invalid number of schedules: expected " + towerRO.frequency()
                                    + " but got " + scheduleCount
                    );
                }

                // Map schedules to entity and link back to tower
                tower.setSchedules(
                        towerRO.schedules().stream()
                                .map(scheduleRO -> {
                                    Schedule schedule = scheduleRO.toEntity(null);
                                    schedule.setId(null); // Force INSERT, not UPDATE
                                    schedule.setTower(tower);
                                    return schedule;
                                })
                                .toList()
                );
            }

            // Save tower with schedules (device will be assigned later)
            Tower savedTower = towerRepository.save(tower);
            log.info("Tower created successfully: {} with ID: {}", savedTower.getName(), savedTower.getId());
            return savedTower;

        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(TOWER);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void update(Long id, TowerRO towerRO) {
        try {
            // ✅ Fetch existing tower with schedules
            Tower existingTower = getTowerById(id);
            if (existingTower == null) {
                throw new ResourceNotFoundException(TOWER + " not found");
            }

            // ✅ Update tower fields
            existingTower.setName(towerRO.name());
            existingTower.setUser(towerRO.user());
            existingTower.setPlant(towerRO.plant());
            existingTower.setFrequency(towerRO.frequency());
            existingTower.setStart_date(towerRO.start_date());
            existingTower.setEnd_date(towerRO.end_date());
            if (towerRO.status() != null) {
                existingTower.setStatus(towerRO.status());
            }

            // ✅ Handle schedules - update existing ones by index, preserving IDs
            if (towerRO.schedules() != null) {
                int scheduleCount = towerRO.schedules().size();
                if (scheduleCount != towerRO.frequency()) {
                    throw new ServiceException(
                            "Invalid number of schedules: expected " + towerRO.frequency()
                                    + " but got " + scheduleCount
                    );
                }

                List<Schedule> existingSchedules = existingTower.getSchedules();
                
                // Update existing schedules in place - only update start_time, preserve ID
                if (existingSchedules != null && !existingSchedules.isEmpty()) {
                    for (int i = 0; i < towerRO.schedules().size() && i < existingSchedules.size(); i++) {
                        Schedule existingSchedule = existingSchedules.get(i);
                        // Only update the start_time, keep the existing ID and tower reference
                        existingSchedule.setStart_time(towerRO.schedules().get(i).start_time());
                    }
                }
            }

            // ✅ Save (JPA will handle update)
            towerRepository.save(existingTower);

        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(TOWER);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void delete(Long id) {
        try {
            Tower tower = getTowerById(id);

            if (Objects.isNull(tower)) {
                throw new ResourceNotFoundException(TOWER + " not found");
            }

            // Unassign all devices from this tower before deleting
            deviceService.unassignDevicesFromTower(tower);
            
            towerRepository.delete(tower);
            log.info(TOWER + " deleted successfully with id: " + id);
        } catch (Exception e) {
            String errorMessage = MessageUtils.deleteErrorMessage(TOWER);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Assign a device to a tower
    public void assignDeviceToTower(Long towerId, Long deviceId) {
        try {
            Tower tower = getTowerById(towerId);
            if (tower == null) {
                throw new ResourceNotFoundException("Tower not found with id: " + towerId);
            }

            deviceService.assignDeviceToTower(deviceId, tower);
            log.info("Device {} assigned to tower {}", deviceId, tower.getName());
        } catch (Exception e) {
            String errorMessage = "Error assigning device to tower";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Get device assigned to a tower
    public List<DeviceDTO> getDeviceByTowerId(Long towerId) {
        try {
            log.info("Attempting to retrieve devices for tower ID: {}", towerId);
            
            Tower tower = getTowerById(towerId);
            if (tower == null) {
                log.error("Tower not found with id: {}", towerId);
                throw new ResourceNotFoundException("Tower not found with id: " + towerId);
            }
            
            log.info("Tower found: {}, fetching devices...", tower.getName());

            // Get devices assigned to this tower through DeviceService
            List<DeviceDTO> devices = deviceService.getDevicesByTower(tower);
            
            if (devices.isEmpty()) {
                log.info("No devices assigned to tower {}", tower.getName());
            } else {
                log.info("Found {} device(s) for tower {}", devices.size(), tower.getName());
            }
            
            return devices;
        } catch (ResourceNotFoundException e) {
            log.error("Tower not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            String errorMessage = "Error retrieving devices for tower " + towerId;
            log.error(errorMessage + ": " + e.getClass().getName() + " - " + e.getMessage(), e);
            throw new ServiceException(errorMessage, e);
        }
    }
}