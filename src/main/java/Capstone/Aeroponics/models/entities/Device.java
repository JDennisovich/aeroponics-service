package Capstone.Aeroponics.models.entities;

import Capstone.Aeroponics.models.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "devices") // optional but recommended
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mac_address", unique = true, nullable = false)
    private String macAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeviceStatus status;

    @ManyToOne
    @JoinColumn(name = "tower_id", nullable = true)
    @ToString.Exclude
    private Tower tower;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}