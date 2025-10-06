# Change Password OTP Implementation Guide

## Overview
This implementation adds OTP (One-Time Password) functionality for changing user passwords. Users must verify their email address with an OTP before they can change their password, providing an additional layer of security.

## Reused Components
The change password OTP functionality reuses the existing infrastructure:
- ✅ **Otp Entity**: Same database table for storing OTPs
- ✅ **OtpRepository**: Same repository for OTP operations
- ✅ **OtpService**: Same service for OTP generation and validation
- ✅ **EmailService**: Extended with change password email template

## New Components Added

### 1. Request Models
- **ChangePasswordOtpRequest.java**: For requesting change password OTP
- **VerifyChangePasswordOtpRequest.java**: For OTP verification with password change

### 2. Enhanced Services
- **EmailService**: Added `sendChangePasswordOtpEmail()` method
- **OAuthService**: Added change password OTP methods

### 3. Enhanced Controllers
- **OAuthController**: Added change password OTP endpoints

## API Endpoints

### 1. Send Change Password OTP
```
POST /oauth/change-password-otp
Content-Type: application/json

{
    "email": "user@example.com"
}
```

**Response:**
```json
{
    "status": 200,
    "message": "OTP sent successfully to your email for password change",
    "timestamp": "2024-01-15T10:30:00Z"
}
```

### 2. Verify OTP and Change Password
```
POST /oauth/verify-change-password-otp
Content-Type: application/json

{
    "email": "user@example.com",
    "newPassword": "newPassword123",
    "confirmPassword": "newPassword123",
    "otpCode": "123456"
}
```

**Response:**
```json
{
    "status": 200,
    "message": "Password changed successfully",
    "timestamp": "2024-01-15T10:30:00Z"
}
```

## Security Features

### OTP Security
- ✅ **6-digit numeric OTP**: Randomly generated secure codes
- ✅ **5-minute expiration**: OTPs expire after 5 minutes
- ✅ **3-attempt limit**: Maximum 3 verification attempts per OTP
- ✅ **Single use**: OTPs can only be used once
- ✅ **Automatic cleanup**: Expired OTPs are automatically cleaned up

### Password Security
- ✅ **BCrypt encryption**: Passwords are encrypted using BCrypt
- ✅ **Password confirmation**: New password must be confirmed
- ✅ **User verification**: Only existing users can change passwords
- ✅ **OTP verification**: Must provide valid OTP to change password

## Email Template

### Change Password OTP Email
```
Subject: Aeroponics - Change Password OTP

Dear [FirstName],

You have requested to change your password for your Aeroponics account.

Your password change OTP is: [OTP_CODE]

This OTP is valid for 5 minutes only.

If you did not request this password change, please ignore this email and consider changing your password immediately.

For security reasons, please do not share this OTP with anyone.

Best regards,
Aeroponics Team
```

## Usage Flow

### 1. User Requests Password Change
1. User enters their email address
2. System verifies user exists
3. System generates and sends OTP
4. User receives email with OTP

### 2. User Verifies OTP and Changes Password
1. User enters new password and confirmation
2. User enters OTP code
3. System verifies OTP is valid and not expired
4. System updates user password
5. User receives confirmation

## Error Handling

### Common Error Responses

#### User Not Found
```json
{
    "status": 400,
    "message": "User not found with this email",
    "timestamp": "2024-01-15T10:30:00Z"
}
```

#### OTP Already Sent
```json
{
    "status": 400,
    "message": "OTP already sent. Please check your email or wait for it to expire.",
    "timestamp": "2024-01-15T10:30:00Z"
}
```

#### Invalid OTP
```json
{
    "status": 400,
    "message": "Invalid or expired OTP",
    "timestamp": "2024-01-15T10:30:00Z"
}
```

#### Passwords Don't Match
```json
{
    "status": 400,
    "message": "Passwords do not match",
    "timestamp": "2024-01-15T10:30:00Z"
}
```

## Frontend Implementation

### 1. Change Password Form
```html
<form id="changePasswordForm">
    <input type="email" id="email" placeholder="Email" required>
    <button type="button" onclick="requestChangePasswordOTP()">Send OTP</button>
</form>
```

### 2. OTP Verification Form
```html
<div id="otpForm" style="display: none;">
    <input type="password" id="newPassword" placeholder="New Password" required>
    <input type="password" id="confirmPassword" placeholder="Confirm Password" required>
    <input type="text" id="otpCode" placeholder="Enter 6-digit OTP" maxlength="6">
    <button onclick="verifyChangePasswordOTP()">Change Password</button>
</div>
```

### 3. JavaScript Implementation
```javascript
async function requestChangePasswordOTP() {
    const email = document.getElementById('email').value;
    
    try {
        const response = await fetch('/oauth/change-password-otp', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: email })
        });
        
        if (response.ok) {
            showOTPForm();
            startCountdown();
        } else {
            const error = await response.json();
            showError(error.message);
        }
    } catch (error) {
        showError('Failed to send OTP');
    }
}

async function verifyChangePasswordOTP() {
    const email = document.getElementById('email').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const otpCode = document.getElementById('otpCode').value;
    
    try {
        const response = await fetch('/oauth/verify-change-password-otp', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                email: email,
                newPassword: newPassword,
                confirmPassword: confirmPassword,
                otpCode: otpCode
            })
        });
        
        if (response.ok) {
            showSuccess('Password changed successfully!');
            redirectToLogin();
        } else {
            const error = await response.json();
            showError(error.message);
        }
    } catch (error) {
        showError('Password change failed');
    }
}
```

## Database Impact

### No New Tables Required
- ✅ **Reuses existing `otp` table**
- ✅ **No changes to `users` table**
- ✅ **No data migration needed**
- ✅ **Backward compatible**

### OTP Table Usage
The existing `otp` table is used for both:
- User registration OTPs
- Change password OTPs

OTPs are distinguished by context (registration vs password change) through the email address and usage pattern.

## Security Considerations

### 1. Rate Limiting
Consider implementing rate limiting for OTP requests to prevent abuse.

### 2. Email Validation
Ensure email addresses are properly validated before sending OTPs.

### 3. OTP Storage
OTPs are stored securely with expiration and usage tracking.

### 4. Password Strength
Consider implementing password strength requirements.

### 5. Logging
Comprehensive logging for security auditing and monitoring.

## Testing Scenarios

### 1. Valid Flow
- User exists and requests OTP
- OTP is sent successfully
- User verifies OTP with correct password
- Password is changed successfully

### 2. Invalid Scenarios
- User doesn't exist
- OTP is expired
- Invalid OTP code
- Passwords don't match
- Max attempts exceeded

### 3. Security Tests
- OTP reuse after successful change
- OTP reuse after expiration
- Multiple OTP requests
- Invalid email formats

## Notes

- The change password OTP functionality reuses all existing OTP infrastructure
- No database schema changes are required
- The implementation is fully backward compatible
- Email configuration must be properly set up for production use
- Consider implementing additional security measures like rate limiting

