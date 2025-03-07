package Capstone.Aeroponics.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import Capstone.Aeroponics.exception.ResourceNotFoundException;
import Capstone.Aeroponics.models.RO.PlantRO;
import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.repositories.PlantRepository;
import Capstone.Aeroponics.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantService {
    public static final String PLANTS = "Plants";

    public static final String PLANT = "Plant";
    
    private final PlantRepository plantRepository;

    //Note: paayos ng Http response, naka success kahit empty dapat empty yung message response. Pa double check naden ng lahat
    public List<Plant> getall() {
        try {
            List<Plant> plants = plantRepository.findAll();
            log.info(PLANT + " found: " + plants.size());
            return plants;
        } catch (Exception e) {
            String errormessage = "Error while getting " + PLANTS;
            log.error(errormessage);
            throw new ServiceException(errormessage, e);
        }
    }
    
    public Optional<Plant> getById(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }

        return plantRepository.findById(id);
    }

    public Plant getPlantById(Long id) {
        try {
            Optional<Plant> plant = getById(id);

            if (plant.isEmpty()) {
                throw new Exception("Plant not found.");
            }
            log.info(PLANT + " found.");
            return plant.get();
        } catch (Exception e) {
            String errorMessage = "Error while getting " + PLANT;
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void save(PlantRO plantRO) {
        try {
            plantRepository.save(plantRO.toEntity(null));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(PLANT);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void update(Long id, PlantRO plantRO) {
        try {
            Plant plant = getPlantById(id);

            if (Objects.isNull(plant)) {
                throw new ResourceNotFoundException("Plant not found");
            }

            plantRepository.save(plantRO.toEntity(plant));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(PLANT);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void delete(Long id) {
        try {
            Plant plant = getPlantById(id);

            if (Objects.isNull(plant)) {
                throw new ResourceNotFoundException("Plant not found");
            }

            plantRepository.delete(plant);
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(PLANT);
            log.error(errorMessage);
            throw new ServiceException(errorMessage, e);
        }
    }
}
