package Capstone.Aeroponics.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import Capstone.Aeroponics.models.DTO.nutrient.NutrientPhLevelDTO;
import Capstone.Aeroponics.models.DTO.nutrient.NutrientPpmDTO;
import Capstone.Aeroponics.models.entities.Nutrient_log;
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
public class TowerService {
    public static final String TOWERS = "Towers";

    public static final String TOWER = "Tower";

    private final TowerRepository towerRepository;

    private final Nutrient_logRepository Nutrient_logRepository;

    public List<Tower> getAll() {
        try {
            List<Tower> towers = towerRepository.findAll();
            log.info(TOWERS + " found: " + towers.size());
            return towers;
        } catch (Exception e) {
            String errorMessage = "Error while getting " + TOWERS;
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public List<Tower> getTowersByUserId(Long id) {
        try {
            List<Tower> towers = towerRepository.findByUserId(id);
            log.info(TOWERS + " found for userId " + id + ": " + towers.size());
            return towers;
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

    public NutrientPhLevelDTO getTowerPhLevel(Long towerId) {
        try {
            Optional<Nutrient_log> nutrient = Nutrient_logRepository
                    .findTopByTowerIdOrderByTimeDesc(towerId);

            if (nutrient.isEmpty()) {
                throw new Exception("No nutrient data found for tower " + towerId);
            }

            return new NutrientPhLevelDTO(nutrient.get());

        } catch (Exception e) {
            String errorMessage = "Error while getting pH level for tower " + towerId;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public List<NutrientPhLevelDTO> getTowerPhLevels(Long towerId) {
        try {
            List<Nutrient_log> nutrientLogs = Nutrient_logRepository
                    .findByTowerIdOrderByTimeDesc(towerId);

            if (nutrientLogs.isEmpty()) {
                throw new Exception("No nutrient data found for tower " + towerId);
            }

            return nutrientLogs.stream()
                    .map(NutrientPhLevelDTO::new)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            String errorMessage = "Error while getting pH levels for tower " + towerId;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public NutrientPpmDTO getTowerPpm(Long towerId) {
        try {
            Optional<Nutrient_log> nutrient = Nutrient_logRepository
                    .findTopByTowerIdOrderByTimeDesc(towerId);

            if (nutrient.isEmpty()) {
                throw new Exception("No nutrient data found for tower " + towerId);
            }

            return new NutrientPpmDTO(nutrient.get());

        } catch (Exception e) {
            String errorMessage = "Error while getting pH level for tower " + towerId;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public List<NutrientPhLevelDTO> getTowerPpms(Long towerId) {
        try {
            List<Nutrient_log> nutrientLogs = Nutrient_logRepository
                    .findByTowerIdOrderByTimeDesc(towerId);

            if (nutrientLogs.isEmpty()) {
                throw new Exception("No nutrient data found for tower " + towerId);
            }

            return nutrientLogs.stream()
                    .map(NutrientPhLevelDTO::new)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            String errorMessage = "Error while getting pH levels for tower " + towerId;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void save(TowerRO towerRO) {
        try {
            towerRepository.save(towerRO.toEntity(null));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(TOWER);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void update(Long id, TowerRO towerRO) {
        try {
            Tower tower = getTowerById(id);

            if (Objects.isNull(tower)) {
                throw new ResourceNotFoundException(TOWER + " not found");
            }

            towerRepository.save(towerRO.toEntity(tower));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(TOWER);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void delete(Long id) {
        try {
            Tower tower = getTowerById(id);

            if (Objects.isNull(tower)) {
                throw new ResourceNotFoundException(TOWER + " not found");
            }

            towerRepository.delete(tower);
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(TOWER);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }
}