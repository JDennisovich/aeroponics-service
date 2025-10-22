# Railway Database Setup Guide

## Railway MySQL Credentials

From your Railway dashboard, you have these variables:
```
MYSQL_DATABASE="railway"
MYSQL_ROOT_PASSWORD="KGziwforModQGBuhRdlKIyXhADoyCgbX"
MYSQLUSER="root"
MYSQLPORT="3306"
MYSQLHOST="${{RAILWAY_PRIVATE_DOMAIN}}" or use TCP Proxy
```

## How to Update Your `.env` File

### Option 1: Using Railway Private Network (Recommended for Railway deployment)

If deploying on Railway, update your `.env` file with:

```env
# Database Configuration - Railway MySQL
DB_URL=jdbc:mysql://${RAILWAY_PRIVATE_DOMAIN}:3306/railway
DB_USERNAME=root
DB_PASSWORD=KGziwforModQGBuhRdlKIyXhADoyCgbX
DB_DRIVER=com.mysql.cj.jdbc.Driver
```

### Option 2: Using Railway TCP Proxy (For local development)

If you need to connect from your local machine, Railway provides a TCP proxy. You'll need to get:
- `RAILWAY_TCP_PROXY_DOMAIN` (e.g., `proxy.railway.app`)
- `RAILWAY_TCP_PROXY_PORT` (e.g., `12345`)

Update your `.env` file with:

```env
# Database Configuration - Railway MySQL (TCP Proxy for Local Development)
DB_URL=jdbc:mysql://[RAILWAY_TCP_PROXY_DOMAIN]:[RAILWAY_TCP_PROXY_PORT]/railway?useSSL=true&requireSSL=false
DB_USERNAME=root
DB_PASSWORD=KGziwforModQGBuhRdlKIyXhADoyCgbX
DB_DRIVER=com.mysql.cj.jdbc.Driver
```

**Replace:**
- `[RAILWAY_TCP_PROXY_DOMAIN]` with your actual Railway TCP proxy domain
- `[RAILWAY_TCP_PROXY_PORT]` with your actual Railway TCP proxy port

## Complete `.env` Example for Railway

```env
# Database Configuration - Railway MySQL
DB_URL=jdbc:mysql://[YOUR_RAILWAY_HOST]:[PORT]/railway
DB_USERNAME=root
DB_PASSWORD=KGziwforModQGBuhRdlKIyXhADoyCgbX
DB_DRIVER=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true
JPA_DIALECT=org.hibernate.dialect.MySQLDialect

# JWT Configuration
JWT_EXPIRY_MINUTES=30
JWT_REFRESH_EXPIRY_HOURS=24
JWT_ISSUER=Aeroponics
JWT_PRIVATE_KEY=classpath:private_key.pem
JWT_PUBLIC_KEY=classpath:public_key.pem

# Server Configuration
SERVER_PORT=8080

# File Upload Configuration
MAX_FILE_SIZE=200MB
MAX_REQUEST_SIZE=200MB

# Image Storage Configuration
FILE_IMAGE_DIR=${user.home}/aeroponics/images/
FILE_IMAGE_URL=http://localhost:8080/resources/

# Logging Configuration
LOG_FILE_NAME=/var/log/aeroponics/Aeroponics.log
LOG_FILE_PATTERN=/var/log/aeroponics/rolling/security-test.%d{yyyy-MM-dd}.%i.gz.log
LOG_MAX_FILE_SIZE=30MB
LOG_TOTAL_SIZE_CAP=1GB

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=urbanfarm142@gmail.com
MAIL_PASSWORD=rgnvnvocxetzkhth
MAIL_SMTP_AUTH=true
MAIL_SMTP_STARTTLS_ENABLE=true
MAIL_SMTP_STARTTLS_REQUIRED=true
```

## Steps to Apply

1. **Open your `.env` file** (in the root of `aeroponics-service` folder)

2. **Replace the Database Configuration section** (lines 2-5) with the Railway credentials above

3. **Get your Railway TCP Proxy information:**
   - Go to your Railway project dashboard
   - Click on your MySQL database service
   - Look for "TCP Proxy" section
   - Copy the domain and port

4. **Update the DB_URL** with the correct host and port

5. **Save the file** and restart your Spring Boot application

## Testing the Connection

After updating, run your Spring Boot application and check the logs for:
```
✅ Successfully connected to Railway MySQL
```

If you see connection errors, verify:
- The TCP proxy domain and port are correct
- The password matches exactly
- Your firewall allows the connection
- Railway service is running

## Railway Environment Variables (for deployment)

When deploying to Railway, you can also set these as Railway environment variables instead of using a `.env` file:

1. Go to Railway project → Variables
2. Add each variable individually:
   - `DB_URL` = `jdbc:mysql://${{RAILWAY_PRIVATE_DOMAIN}}:3306/railway`
   - `DB_USERNAME` = `root`
   - `DB_PASSWORD` = `${{MYSQL_ROOT_PASSWORD}}`
   - (Add all other variables)

Railway will automatically inject these into your application.
