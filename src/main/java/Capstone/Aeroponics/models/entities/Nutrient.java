package Capstone.Aeroponics.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nutrients")
public class Nutrient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "tower_id", nullable = false)
    private Tower tower;

    @Column (name = "time", nullable = false)
    private LocalTime time;

    @Column (name = "ph_level", nullable = false)
    private int ph_level;

    @Column (name = "ppm", nullable = false)
    private int ppm;
}