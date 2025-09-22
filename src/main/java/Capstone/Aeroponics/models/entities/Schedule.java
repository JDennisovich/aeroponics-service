package Capstone.Aeroponics.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "tower_id", nullable = false)
    private Tower tower;

    @Column(name = "start_hour", nullable = false)
    private int start_hour;

    @Column(name = "start_minute", nullable = false)
    private int start_minute;

    @Column(name = "duration", nullable = false)
    private int durationMinute;

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date;

    @Column(name = "end_date", nullable = false)
    private LocalDate end_date;
}
