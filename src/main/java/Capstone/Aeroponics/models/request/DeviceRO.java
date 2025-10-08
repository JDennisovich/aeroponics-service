package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Device;
import Capstone.Aeroponics.models.enums.DeviceStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public record DeviceRO(
        Long id,
        @NotBlank(message = "MAC address is mandatory") String mac_address,
        String ip_address,
        DeviceStatus status,
        Long tower_id
) {
    public Device toEntity(Device device) {
        if (Objects.isNull(device)) {
            device = new Device();
        }
        device.setMacAddress(mac_address);
        device.setIpAddress(ip_address);
        if (status != null) {
            device.setStatus(status);
        } else {
            // Default to FREE status if not provided
            device.setStatus(DeviceStatus.FREE);
        }

        return device;
    }
}
