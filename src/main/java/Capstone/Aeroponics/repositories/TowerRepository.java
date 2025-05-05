package Capstone.Aeroponics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Capstone.Aeroponics.models.entities.Tower;

@Repository
public interface TowerRepository extends JpaRepository<Tower, Long> {
	
}
