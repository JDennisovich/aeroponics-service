package Capstone.Aeroponics.services;

import Capstone.Aeroponics.models.entities.Otp;
import Capstone.Aeroponics.repositories.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    
    private final OtpRepository otpRepository;
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;
    
    /**
     * Generate and save OTP for email verification
     * @param email the email address
     * @return the generated OTP code
     */
    public String generateOtp(String email) {
        log.info("[OtpService.generateOtp]::invoked for email: {}", email);
        
        // Clean up expired OTPs for this email
        cleanupExpiredOtps(email);
        
        // Generate new OTP
        String otpCode = generateRandomOtp();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(OTP_EXPIRY_MINUTES);
        
        // Save OTP to database
        Otp otp = Otp.builder()
                .email(email)
                .otpCode(otpCode)
                .createdAt(now)
                .expiresAt(expiresAt)
                .isUsed(false)
                .attempts(0)
                .build();
        
        otpRepository.save(otp);
        
        log.info("OTP generated successfully for email: {}", email);
        return otpCode;
    }
    
    /**
     * Verify OTP code
     * @param email the email address
     * @param otpCode the OTP code to verify
     * @return true if OTP is valid, false otherwise
     */
    public boolean verifyOtp(String email, String otpCode) {
        log.info("[OtpService.verifyOtp]::invoked for email: {}", email);
        
        Optional<Otp> otpOptional = otpRepository.findByEmailAndOtpCodeAndIsUsedFalse(email, otpCode);
        
        if (otpOptional.isEmpty()) {
            log.warn("OTP not found or already used for email: {}", email);
            return false;
        }
        
        Otp otp = otpOptional.get();
        
        // Check if OTP is expired
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("OTP expired for email: {}", email);
            otp.setIsUsed(true);
            otpRepository.save(otp);
            return false;
        }
        
        // Check if max attempts exceeded
        if (otp.getAttempts() >= MAX_ATTEMPTS) {
            log.warn("Max attempts exceeded for email: {}", email);
            otp.setIsUsed(true);
            otpRepository.save(otp);
            return false;
        }
        
        // Increment attempts
        otp.setAttempts(otp.getAttempts() + 1);
        
        // If OTP is correct, mark as used
        if (otp.getOtpCode().equals(otpCode)) {
            otp.setIsUsed(true);
            otpRepository.save(otp);
            log.info("OTP verified successfully for email: {}", email);
            return true;
        } else {
            otpRepository.save(otp);
            log.warn("Invalid OTP for email: {}", email);
            return false;
        }
    }
    
    /**
     * Check if user has active OTP
     * @param email the email address
     * @return true if user has active OTP, false otherwise
     */
    public boolean hasActiveOtp(String email) {
        List<Otp> activeOtps = otpRepository.findActiveOtpsByEmail(email, LocalDateTime.now());
        return !activeOtps.isEmpty();
    }
    
    /**
     * Clean up expired OTPs for a specific email
     * @param email the email address
     */
    private void cleanupExpiredOtps(String email) {
        List<Otp> expiredOtps = otpRepository.findExpiredOtpsByEmail(email, LocalDateTime.now());
        if (!expiredOtps.isEmpty()) {
            otpRepository.deleteAll(expiredOtps);
            log.info("Cleaned up {} expired OTPs for email: {}", expiredOtps.size(), email);
        }
    }
    
    /**
     * Generate random OTP code
     * @return the generated OTP code
     */
    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        
        return otp.toString();
    }
}
