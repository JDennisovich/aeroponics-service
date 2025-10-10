package Capstone.Aeroponics.services;

import Capstone.Aeroponics.models.DTO.device.DeviceDTO;
import Capstone.Aeroponics.models.entities.Device;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.enums.DeviceStatus;
import Capstone.Aeroponics.repositories.DeviceRepository;
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
        } catch (Exception e) {
            String errorMessage = "Error while unassigning devices from tower";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }
}