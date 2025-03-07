package Capstone.Aeroponics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Capstone.Aeroponics.models.entities.Plant;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    
} 