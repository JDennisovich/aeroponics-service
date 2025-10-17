package Capstone.Aeroponics.models.entities;

import Capstone.Aeroponics.models.enums.WaterLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @JsonIgnoreProperties({"schedules", "user"})
    private Tower tower;

    @Column (name = "time", nullable = false)
    private LocalDateTime time;

    @Column (name = "ph_level", nullable = false)
    private BigDecimal ph_level;

    @Column (name = "ppm", nullable = false)
    private BigDecimal ppm;

    @Enumerated(EnumType.STRING)
    @Column (name = "water_level", nullable = false)
    private WaterLevel water_level;

    @Column (name = "water_temperature")
    private BigDecimal water_temperature;
}