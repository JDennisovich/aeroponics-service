package Capstone.Aeroponics.repositories;

import Capstone.Aeroponics.models.entities.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
    Optional<Nutrient> findTopByTowerIdOrderByTimeDesc(Long towerId);

    List<Nutrient> findByTowerIdOrderByTimeDesc(Long towerId);

}
