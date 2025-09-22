package Capstone.Aeroponics.repositories;

import Capstone.Aeroponics.models.entities.Nutrient_log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Nutrient_logRepository extends JpaRepository<Nutrient_log, Long> {
    Optional<Nutrient_log> findTopByTowerIdOrderByTimeDesc(Long towerId);

    List<Nutrient_log> findByTowerIdOrderByTimeDesc(Long towerId);

}
