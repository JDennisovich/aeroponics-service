package Capstone.Aeroponics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Capstone.Aeroponics.models.entities.Tower;

import java.util.List;

@Repository
public interface TowerRepository extends JpaRepository<Tower, Long> {
    List<Tower> findByUserId(Long id);
}
