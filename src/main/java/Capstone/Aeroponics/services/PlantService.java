package Capstone.Aeroponics.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import Capstone.Aeroponics.models.DTO.plant.PlantDTO;
import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.models.request.PlantRO;
import Capstone.Aeroponics.repositories.UserRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Capstone.Aeroponics.exception.ResourceNotFoundException;
import Capstone.Aeroponics.models.entities.Plant;
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

    private final UserRepository userRepository;

    public List<PlantDTO> getAll() {
        try {
            List<Plant> plants = plantRepository.findAll();

            List<PlantDTO> plantDTOs = plants.stream()
                    .map(PlantDTO::new)  // use your constructor
                    .toList();

            log.info(PLANT + " found: " + plantDTOs.size());
            return plantDTOs;
        } catch (Exception e) {
            String errorMessage = "Error while getting " + PLANTS;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
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

    public List<String> getAllPlantNames() {
        try {
            List<Plant> plants = plantRepository.findAll();
            List<String> plantNames = plants.stream()
                    .map(Plant::getName)
                    .toList(); // Java 16+ (use .collect(Collectors.toList()) for older versions)

            log.info("Plant names found: " + plantNames.size());
            return plantNames;
        } catch (Exception e) {
            String errorMessage = "Error while getting plant names";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }


    public void save(PlantRO plantRO) {
        try {
            // get the logged-in user's email (or username) from JWT
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            // fetch the actual User entity
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // convert RO -> Entity with current user
            Plant plant = plantRO.toEntity(null, currentUser);

            plantRepository.save(plant);

        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage("PLANT");
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void saveAll(List<PlantRO> plantROList) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            List<Plant> plants = plantROList.stream()
                    .map(ro -> ro.toEntity(null, currentUser))
                    .toList();

            plantRepository.saveAll(plants);

        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(PLANT);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public List<PlantDTO> getPlantsByUserId(Long id) {
        try {
            List<Plant> plants = plantRepository.findByUserId(id);
            log.info(PLANTS + " found for userId " + id + ": " + plants.size());

            // Use the PlantDTO constructor
            return plants.stream()
                    .map(PlantDTO::new)
                    .toList(); // If on Java 8 → use .collect(Collectors.toList())

        } catch (Exception e) {
            String errorMessage = "Error while getting " + PLANTS + " for userId " + id;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public void update(Long id, PlantRO plantRO) {
        try {
            Plant plant = getPlantById(id);

            if (Objects.isNull(plant)) {
                throw new ResourceNotFoundException("Plant not found");
            }

            // get logged-in user from JWT
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // update plant with logged-in user
            plantRepository.save(plantRO.toEntity(plant, currentUser));

        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(PLANT);
            log.error(errorMessage, e);
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
