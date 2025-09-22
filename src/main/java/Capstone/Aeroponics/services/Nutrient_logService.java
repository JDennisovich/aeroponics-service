package Capstone.Aeroponics.services;

import Capstone.Aeroponics.exception.ResourceNotFoundException;
import Capstone.Aeroponics.models.entities.Nutrient_log;
import Capstone.Aeroponics.models.request.Nutrient_logsRO;
import Capstone.Aeroponics.repositories.Nutrient_logRepository;
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
public class Nutrient_logService {
    public static final String NUTRIENTS = "Nutrients";

    public static final String NUTRIENT = "Nutrient";

    private final Nutrient_logRepository nutrientLogRepository;

    public List<Nutrient_log> getAll() {
        try {
            List<Nutrient_log> nutrientLogs = nutrientLogRepository.findAll();
            log.info(NUTRIENT + " found: " + nutrientLogs.size());
            return nutrientLogs;
        } catch (Exception e) {
            String errorMessage = "Error while getting " + NUTRIENTS;
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public Optional<Nutrient_log> getById(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }

        return nutrientLogRepository.findById(id);
    }

    public Nutrient_log getNutrientById(Long id) {
        try {
            Optional<Nutrient_log> nutrient = getById(id);

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

    public void save(Nutrient_logsRO nutrientLogsRO) {
        try {
            nutrientLogRepository.save(nutrientLogsRO.toEntity(null));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(NUTRIENTS);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public void update(Long id, Nutrient_logsRO nutrientLogsRO) {
        try {
            Nutrient_log nutrientLog = getNutrientById(id);

            if (Objects.isNull(nutrientLog)) {
                throw new ResourceNotFoundException(NUTRIENT + " not found");
            }

            nutrientLogRepository.save(nutrientLogsRO.toEntity(nutrientLog));
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(NUTRIENT);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public void delete(Long id) {
        try {
            Nutrient_log nutrientLog = getNutrientById(id);

            if (Objects.isNull(nutrientLog)) {
                throw new ResourceNotFoundException(NUTRIENT + " not found");
            }

            nutrientLogRepository.delete(nutrientLog);
        } catch (Exception e) {
            String errorMessage = MessageUtils.deleteErrorMessage(NUTRIENT);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
