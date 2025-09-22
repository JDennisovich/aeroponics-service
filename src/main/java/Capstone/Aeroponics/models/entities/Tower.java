package Capstone.Aeroponics.models.entities;

import Capstone.Aeroponics.models.enums.WaterLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "towers")
public class Tower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(name = "name", nullable =  false)
    private String name;

    @Column(name = "status", nullable =  false)
    private Boolean status;

    @Column(name = "time", nullable =  false)
    private LocalTime time;

    @Column(name = "water_level", nullable = false)
    private WaterLevel waterLevel;

    @Column(name = "frequency", nullable = false)
    private int frequency;

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date;

    @Column(name = "end_date", nullable = false)
    private LocalDate end_date;
}