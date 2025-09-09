package Capstone.Aeroponics.services;

import Capstone.Aeroponics.exception.ResourceNotFoundException;
import Capstone.Aeroponics.models.entities.Nutrient;
import Capstone.Aeroponics.models.request.NutrientRO;
import Capstone.Aeroponics.repositories.NutrientRepository;
import Capstone.Aeroponics.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NutrientService {
    public static final String NUTRIENTS = "Nutrients";

    public static final String NUTRIENT = "Nutrient";

    private final NutrientRepository nutrientRepository;

    public List<Nutrient> getAll() {
        try {
            List<Nutrient> nutrients = nutrientRepository.findAll();
            log.info(NUTRIENT + " found: " + nutrients.size());
            return nutrients;
        } catch (Exception e) {
            String errorMessage = "Error while getting " + NUTRIENTS;
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public Optional<Nutrient> getById(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }

        return nutrientRepository.findById(id);
    }

    public Nutrient getNutrientById(Long id) {
        try {
            Optional<Nutrient> nutrient = getById(id);

            if (nutrient.isEmpty()) {
                throw new Exception(NUTRIENT + " not found.");
            }
            log.info(NUTRIENT + " found.");
            return nutrient.get();
        } catch (Exception e) {
            String errorMessage = "Error while getting " + NUTRIENT;
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public void save(NutrientRO nutrientRO) {
        try {
            nutrientRepository.save(nutrientRO.toEntity(null));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(NUTRIENTS);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public void update(Long id, NutrientRO nutrientRO) {
        try {
            Nutrient nutrient = getNutrientById(id);

            if (Objects.isNull(nutrient)) {
                throw new ResourceNotFoundException(NUTRIENT + " not found");
            }

            nutrientRepository.save(nutrientRO.toEntity(nutrient));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(NUTRIENT);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public void delete(Long id) {
        try {
            Nutrient nutrient = getNutrientById(id);

            if (Objects.isNull(nutrient)) {
                throw new ResourceNotFoundException(NUTRIENT + " not found");
            }

            nutrientRepository.delete(nutrient);
        } catch (Exception e) {
            String errorMessage = MessageUtils.deleteErrorMessage(NUTRIENT);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
