package Capstone.Aeroponics.models.entities;

import Capstone.Aeroponics.models.enums.WaterLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @OneToMany(mappedBy = "tower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    @Column(name = "name", nullable =  false)
    private String name;

    @Column(name = "status", nullable =  false)
    private Boolean status;

    @Column(name = "time", nullable =  false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "water_level", nullable = false)
    private WaterLevel waterLevel;

    @Column(name = "frequency", nullable = false)
    private int frequency;

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date;

    @Column(name = "end_date", nullable = false)
    private LocalDate end_date;

}