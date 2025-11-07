package Capstone.Aeroponics.repositories;

import Capstone.Aeroponics.models.entities.Device;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.models.enums.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByMacAddress(String mac_address);
    Optional<Device> findFirstByStatus(DeviceStatus status);
    List<Device> findAllByStatus(DeviceStatus status);
    List<Device> findByTower(Tower tower);
    List<Device> findAllByStatusAndUser(DeviceStatus status, User user);
}