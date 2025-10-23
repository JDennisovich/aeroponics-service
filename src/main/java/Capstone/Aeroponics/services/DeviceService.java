package Capstone.Aeroponics.services;

import Capstone.Aeroponics.models.DTO.device.DeviceDTO;
import Capstone.Aeroponics.models.entities.Device;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.enums.DeviceStatus;
import Capstone.Aeroponics.models.enums.TowerStatus;
import Capstone.Aeroponics.repositories.DeviceRepository;
import Capstone.Aeroponics.repositories.TowerRepository;
import Capstone.Aeroponics.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {
    public static final String DEVICES = "Devices";
    public static final String DEVICE = "Device";

    private final DeviceRepository deviceRepository;
    private final TowerRepository towerRepository;

    // Register device as FREE (NodeMCU boot-up) - upsert logic
    public DeviceDTO registerDevice(String mac_address, String ip_address) {
        try {
            Device existingDevice = deviceRepository.findByMacAddress(mac_address).orElse(null);
            
            Device device = Device.builder()
                    .macAddress(mac_address)
                    .ipAddress(ip_address)
                    .status(DeviceStatus.FREE)
                    .build();
            
            if (existingDevice != null) {
                device.setId(existingDevice.getId());
            }
            
            Device savedDevice = deviceRepository.save(device);
            log.info(DEVICE + " registered successfully with MAC: " + mac_address);
            return new DeviceDTO(savedDevice);
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(DEVICE);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Find first available FREE device (returns DTO for controller)
    public DeviceDTO findFreeDevice() {
        try {
            Device device = deviceRepository.findFirstByStatus(DeviceStatus.FREE).orElse(null);
            if (device != null) {
                log.info("Free " + DEVICE + " found: " + device.getMacAddress());
                return new DeviceDTO(device);
            } else {
                log.info("No free " + DEVICE + " available");
                return null;
            }
        } catch (Exception e) {
            String errorMessage = "Error while finding free " + DEVICE;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Find first available FREE device (returns Entity for internal service use)
    public Device findFreeDeviceEntity() {
        try {
            Device device = deviceRepository.findFirstByStatus(DeviceStatus.FREE).orElse(null);
            if (device != null) {
                log.info("Free " + DEVICE + " found: " + device.getMacAddress());
            } else {
                log.info("No free " + DEVICE + " available");
            }
            return device;
        } catch (Exception e) {
            String errorMessage = "Error while finding free " + DEVICE;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Assign device (used when linking to a Tower)
    public Device assignDevice(Device device) {
        try {
            device.setStatus(DeviceStatus.ASSIGNED);
            Device savedDevice = deviceRepository.save(device);
            log.info(DEVICE + " assigned successfully with MAC: " + device.getMacAddress());
            return savedDevice;
        } catch (Exception e) {
            String errorMessage = MessageUtils.saveErrorMessage(DEVICE);
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Get all free devices (returns list of DTOs for controller)
    public List<DeviceDTO> getAllFreeDevices() {
        try {
            List<Device> devices = deviceRepository.findAllByStatus(DeviceStatus.FREE);
            log.info("Found " + devices.size() + " free " + DEVICES);
            return devices.stream()
                    .map(DeviceDTO::new)
                    .toList();
        } catch (Exception e) {
            String errorMessage = "Error while finding free " + DEVICES;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Unassign devices from a tower (set status to FREE and remove tower reference)
    public void unassignDevicesFromTower(Tower tower) {
        try {
            List<Device> devices = deviceRepository.findByTower(tower);
            for (Device device : devices) {
                device.setTower(null);
                device.setStatus(DeviceStatus.FREE);
                deviceRepository.save(device);
                log.info(DEVICE + " unassigned from tower: " + device.getMacAddress());
            }
            // If no devices remain assigned, mark tower inactive
            List<Device> remaining = deviceRepository.findByTower(tower);
            if (remaining == null || remaining.isEmpty()) {
                tower.setStatus(TowerStatus.INACTIVE);
                towerRepository.save(tower);
                log.info("Tower {} marked INACTIVE due to no assigned devices", tower.getName());
            }
        } catch (Exception e) {
            String errorMessage = "Error while unassigning devices from tower";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Unassign a specific device from its tower and mark tower inactive if none remain
    public void unassignDeviceFromTower(Long deviceId) {
        try {
            Device device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new ServiceException(DEVICE + " not found with id: " + deviceId));
            Tower tower = device.getTower();
            if (tower == null) {
                // Nothing to do; ensure device is FREE
                device.setStatus(DeviceStatus.FREE);
                deviceRepository.save(device);
                return;
            }

            device.setTower(null);
            device.setStatus(DeviceStatus.FREE);
            deviceRepository.save(device);
            log.info(DEVICE + " {} unassigned from tower {}", device.getMacAddress(), tower.getName());

            List<Device> remaining = deviceRepository.findByTower(tower);
            if (remaining == null || remaining.isEmpty()) {
                tower.setStatus(TowerStatus.INACTIVE);
                towerRepository.save(tower);
                log.info("Tower {} marked INACTIVE due to no assigned devices", tower.getName());
            }
        } catch (Exception e) {
            String errorMessage = "Error unassigning device from tower";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Get device by ID (returns DTO for controller)
    public DeviceDTO getDeviceById(Long id) {
        try {
            Device device = deviceRepository.findById(id)
                    .orElseThrow(() -> new ServiceException(DEVICE + " not found with id: " + id));
            log.info(DEVICE + " found with id: " + id);
            return new DeviceDTO(device);
        } catch (Exception e) {
            String errorMessage = "Error while finding " + DEVICE + " with id: " + id;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Get device by MAC address (for Arduino)
    public DeviceDTO getDeviceByMacAddress(String macAddress) {
        try {
            Device device = deviceRepository.findByMacAddress(macAddress)
                    .orElseThrow(() -> new ServiceException(DEVICE + " not found with MAC: " + macAddress));
            log.info(DEVICE + " found with MAC: " + macAddress);
            return new DeviceDTO(device);
        } catch (Exception e) {
            String errorMessage = "Error while finding " + DEVICE + " with MAC: " + macAddress;
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Get all devices assigned to a tower (returns list of DTOs)
    public List<DeviceDTO> getDevicesByTower(Tower tower) {
        try {
            List<Device> devices = deviceRepository.findByTower(tower);
            log.info("Found {} device(s) for tower: {}", devices.size(), tower.getName());
            return devices.stream()
                    .map(DeviceDTO::new)
                    .toList();
        } catch (Exception e) {
            String errorMessage = "Error while finding devices for tower";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    // Assign a specific device to a tower
    public void assignDeviceToTower(Long deviceId, Tower tower) {
        try {
            Device device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new ServiceException(DEVICE + " not found with id: " + deviceId));
            
            if (device.getStatus() != DeviceStatus.FREE) {
                throw new ServiceException(DEVICE + " is not available (already assigned)");
            }
            
            device.setTower(tower);
            device.setStatus(DeviceStatus.ASSIGNED);
            deviceRepository.save(device);
            log.info(DEVICE + " {} assigned to tower {}", device.getMacAddress(), tower.getName());
        } catch (Exception e) {
            String errorMessage = "Error assigning " + DEVICE + " to tower";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }
}