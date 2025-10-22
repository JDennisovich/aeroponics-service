# Environment Variables Setup

## Overview
This project uses environment variables for configuration management. Sensitive data and environment-specific settings are stored in a `.env` file.

## Setup Instructions

1. **Copy the example file:**
   ```bash
   cp .env.example .env
   ```

2. **Update the `.env` file with your actual values:**
   - Database credentials
   - Email credentials (use Gmail App Password, not your regular password)
   - JWT configuration
   - File paths and URLs

## Important Environment Variables

### Database Configuration
- `DB_URL`: Your MySQL database connection URL
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password (leave empty if no password)

### Email Configuration
- `MAIL_USERNAME`: Your Gmail address
- `MAIL_PASSWORD`: Gmail App Password (see below)

### JWT Configuration
- `JWT_EXPIRY_MINUTES`: Access token expiry time
- `JWT_REFRESH_EXPIRY_HOURS`: Refresh token expiry time

## Getting Gmail App Password

1. Go to your Google Account settings
2. Navigate to Security → 2-Step Verification
3. Scroll down to "App passwords"
4. Generate a new app password for "Mail"
5. Copy the 16-character password to `MAIL_PASSWORD` in `.env`

## Security Notes

- **Never commit `.env` to version control** (already added to `.gitignore`)
- Share `.env.example` instead of `.env` with team members
- Each developer should maintain their own `.env` file
- Use different values for development, staging, and production environments

## Loading Environment Variables

Spring Boot automatically loads environment variables from:
1. System environment variables
2. `.env` file (with proper configuration)
3. `application.properties` (references environment variables)

The application uses `${VARIABLE_NAME}` syntax in `application.properties` to reference values from the `.env` file.
