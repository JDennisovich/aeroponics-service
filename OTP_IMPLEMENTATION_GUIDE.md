# OTP Implementation Guide

## Overview
This implementation adds OTP (One-Time Password) functionality to the user registration process using Java Mail Sender. Users must verify their email address with an OTP before completing registration.

## New Components Added

### 1. Database Entity
- **Otp.java**: Entity to store OTP codes with expiration and usage tracking
- **OtpRepository.java**: Repository for OTP database operations

### 2. Services
- **OtpService.java**: Handles OTP generation, validation, and cleanup
- **EmailService.java**: Sends OTP emails using Java Mail Sender

### 3. Request Models
- **SendOtpRequest.java**: For requesting OTP
- **VerifyOtpAndRegisterRequest.java**: For OTP verification with registration
- **OtpRequest.java**: For simple OTP verification

### 4. Updated Controllers
- **OAuthController.java**: Added new endpoints for OTP functionality

## API Endpoints

### 1. Send OTP
```
POST /oauth/send-otp
Content-Type: application/json

{
    "email": "user@example.com"
}
```

### 2. Verify OTP and Register
```
POST /oauth/verify-otp-register
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe", 
    "email": "user@example.com",
    "password": "password123",
    "confirmPassword": "password123",
    "otpCode": "123456"
}
```

## Configuration

### Email Settings
Update `application.properties` with your email configuration:

```properties
# Email configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### Database Table
The OTP table will be automatically created with the following structure:
```sql
CREATE TABLE otp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    otp_code VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    expires_at DATETIME NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    attempts INT NOT NULL DEFAULT 0
);
```

## OTP Features

### Security Features
- **6-digit numeric OTP**: Randomly generated secure codes
- **5-minute expiration**: OTPs expire after 5 minutes
- **3-attempt limit**: Maximum 3 verification attempts per OTP
- **Single use**: OTPs can only be used once
- **Automatic cleanup**: Expired OTPs are automatically cleaned up

### Email Features
- **Professional email template**: Clean, branded email format
- **User-friendly content**: Clear instructions and OTP display
- **Error handling**: Comprehensive error handling for email failures

## Usage Flow

1. **User requests OTP**: Send email to `/oauth/send-otp`
2. **System generates OTP**: 6-digit code with 5-minute expiry
3. **Email sent**: User receives OTP via email
4. **User verifies**: Submit registration with OTP to `/oauth/verify-otp-register`
5. **System validates**: OTP is checked for validity and expiry
6. **User registered**: If valid, user account is created

## Error Handling

### Common Error Responses
- **400 Bad Request**: Invalid email format, missing fields
- **400 Bad Request**: OTP already sent (wait for expiry)
- **400 Bad Request**: User already exists
- **400 Bad Request**: Invalid or expired OTP
- **400 Bad Request**: Max attempts exceeded

## Security Considerations

1. **Rate Limiting**: Consider implementing rate limiting for OTP requests
2. **Email Validation**: Ensure email addresses are properly validated
3. **OTP Storage**: OTPs are stored securely with expiration
4. **Cleanup**: Regular cleanup of expired OTPs
5. **Logging**: Comprehensive logging for security auditing

## Testing

### Test Scenarios
1. **Valid OTP flow**: Complete registration with valid OTP
2. **Expired OTP**: Try using OTP after 5 minutes
3. **Invalid OTP**: Try using wrong OTP code
4. **Max attempts**: Try OTP verification 4 times
5. **Duplicate OTP request**: Request OTP multiple times
6. **Email validation**: Test with invalid email formats

### Sample Test Data
```json
{
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "TestPassword123!",
    "confirmPassword": "TestPassword123!",
    "otpCode": "123456"
}
```

## Notes

- The original `/oauth/register` endpoint now requires OTP verification
- Email configuration must be properly set up for production use
- Consider using environment variables for sensitive email credentials
- Monitor OTP usage and implement rate limiting if needed
