package Capstone.Aeroponics.repositories;

import Capstone.Aeroponics.models.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    
    Optional<Otp> findByEmailAndOtpCodeAndIsUsedFalse(String email, String otpCode);
    
    List<Otp> findByEmailAndIsUsedFalse(String email);
    
    @Query("SELECT o FROM Otp o WHERE o.email = :email AND o.isUsed = false AND o.expiresAt > :currentTime ORDER BY o.createdAt DESC")
    List<Otp> findActiveOtpsByEmail(@Param("email") String email, @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT o FROM Otp o WHERE o.email = :email AND o.isUsed = false AND o.expiresAt <= :currentTime")
    List<Otp> findExpiredOtpsByEmail(@Param("email") String email, @Param("currentTime") LocalDateTime currentTime);
    
    void deleteByEmailAndIsUsedTrue(String email);
    
    void deleteByExpiresAtBefore(LocalDateTime expiredTime);
}
