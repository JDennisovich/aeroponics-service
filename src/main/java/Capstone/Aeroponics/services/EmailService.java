package Capstone.Aeroponics.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${sendgrid.from.email}")
    private String fromEmail;
    
    @Value("${sendgrid.from.name}")
    private String fromName;

    /**
     * Send OTP email to user
     * @param toEmail the recipient email
     * @param otpCode the OTP code
     * @param firstName the user's first name
     */
    public void sendOtpEmail(String toEmail, String otpCode, String firstName) {
        log.info("[EmailService.sendOtpEmail]::invoked for email: {}", toEmail);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(String.format("%s <%s>", fromName, fromEmail));
            message.setTo(toEmail);
            message.setSubject("Aeroponics - Email Verification OTP");
            message.setText(buildOtpEmailBody(otpCode, firstName));
            
            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);
            
        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}, error: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
    
    /**
     * Send change password OTP email to user
     * @param toEmail the recipient email
     * @param otpCode the OTP code
     * @param firstName the user's first name
     */
    public void sendChangePasswordOtpEmail(String toEmail, String otpCode, String firstName) {
        log.info("[EmailService.sendChangePasswordOtpEmail]::invoked for email: {}", toEmail);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(String.format("%s <%s>", fromName, fromEmail));
            message.setTo(toEmail);
            message.setSubject("Aeroponics - Change Password OTP");
            message.setText(buildChangePasswordOtpEmailBody(otpCode, firstName));
            
            mailSender.send(message);
            log.info("Change password OTP email sent successfully to: {}", toEmail);
            
        } catch (Exception e) {
            log.error("Failed to send change password OTP email to: {}, error: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send change password OTP email", e);
        }
    }
    
    /**
     * Build the email body for OTP
     * @param otpCode the OTP code
     * @param firstName the user's first name
     * @return the email body
     */
    private String buildOtpEmailBody(String otpCode, String firstName) {
        return String.format("""
            Dear %s,
            
            Thank you for registering with %s!
            
            Your email verification OTP is: %s
            
            This OTP is valid for 5 minutes only.
            
            If you did not request this verification, please ignore this email.
            
            Best regards,
            %s Team
            """, firstName, fromName, otpCode, fromName);
    }
    
    /**
     * Build the email body for change password OTP
     * @param otpCode the OTP code
     * @param firstName the user's first name
     * @return the email body
     */
    private String buildChangePasswordOtpEmailBody(String otpCode, String firstName) {
        return String.format("""
            Dear %s,
            
            You have requested to change your password for your %s account.
            
            Your password change OTP is: %s
            
            This OTP is valid for 5 minutes only.
            
            If you did not request this password change, please ignore this email and consider changing your password immediately.
            
            For security reasons, please do not share this OTP with anyone.
            
            Best regards,
            %s Team
            """, firstName, fromName, otpCode, fromName);
    }
}
