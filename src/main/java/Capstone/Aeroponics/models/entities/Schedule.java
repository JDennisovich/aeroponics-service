package Capstone.Aeroponics.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tower_id", nullable = false)
    @ToString.Exclude
    private Tower tower;

    @Column(name = "start_time", nullable = false)
    private LocalTime start_time;

}
