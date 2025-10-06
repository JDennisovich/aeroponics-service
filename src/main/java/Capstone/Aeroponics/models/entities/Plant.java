package Capstone.Aeroponics.models.entities;

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

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plants")
public class Plant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "min_ph_level", nullable = false)
    private BigDecimal min_ph_level;

    @Column(name = "max_ph_level", nullable = false)
    private BigDecimal max_ph_level;

    @Column(name = "min_ppm", nullable = false)
    private int min_ppm;

    @Column(name = "max_ppm", nullable = false)
    private int max_ppm;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}