package Capstone.Aeroponics.models.entities;

import Capstone.Aeroponics.models.enums.WaterLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nutrients_log")
public class Nutrient_log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "tower_id", nullable = false)
    private Tower tower;

    @Column (name = "time", nullable = false)
    private LocalTime time;

    @Column (name = "ph_level", nullable = false)
    private BigDecimal ph_level;

    @Column (name = "ppm", nullable = false)
    private BigDecimal ppm;

    @Enumerated(EnumType.STRING)
    @Column (name = "water_level", nullable = false)
    private WaterLevel water_level;
}