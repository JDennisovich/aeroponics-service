# Railway MySQL Connection - Complete Guide

## Your Railway Credentials (from dashboard)
```
Database Name: railway
Username: root
Password: KGziwforModQGBuhRdlKIyXhADoyCgbX
Port: 3306
```
 
## What You Need to Find in Railway

The variables `${{RAILWAY_TCP_PROXY_DOMAIN}}` and `${{RAILWAY_TCP_PROXY_PORT}}` are **template placeholders**. You need to find the **actual values** in your Railway dashboard.

### How to Find TCP Proxy Values:

1. **Go to Railway Dashboard** (https://railway.app)
2. **Click on your MySQL database service**
3. **Click the "Connect" tab** (or "Variables" tab)
4. **Look for one of these sections:**
   - "TCP Proxy"
   - "Public Networking"
   - "Connection Info"

5. **You'll see actual values like:**
   ```
   Host: monorail.proxy.rlwy.net
   Port: 23456
   ```
   OR a full connection string like:
   ```
   mysql://root:password@monorail.proxy.rlwy.net:23456/railway
   ```

6. **Copy those values:**
   - The domain (e.g., `monorail.proxy.rlwy.net`)
   - The port number (e.g., `23456`)

---

## Update application.properties

**Current state (line 5):**
```properties
spring.datasource.url=jdbc:mysql://[RAILWAY_TCP_PROXY_DOMAIN]:[RAILWAY_TCP_PROXY_PORT]/railway
```

**After you find the values, replace it like this:**

**Example (if your domain is `monorail.proxy.rlwy.net` and port is `23456`):**
```properties
spring.datasource.url=jdbc:mysql://monorail.proxy.rlwy.net:23456/railway
```

---

## Complete Configuration

Your final `application.properties` database section should look like this:

```properties
# Railway MySQL Connection
spring.datasource.url=jdbc:mysql://[YOUR_ACTUAL_DOMAIN]:[YOUR_ACTUAL_PORT]/railway
spring.datasource.username=root
spring.datasource.password=KGziwforModQGBuhRdlKIyXhADoyCgbX
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

## Alternative: If You Can't Find TCP Proxy

If Railway doesn't show you TCP Proxy settings, you might need to:

1. **Enable Public Networking** in Railway MySQL settings
2. Or **use the Private Domain** (only works when deploying TO Railway)

### For deployment TO Railway (not local development):
```properties
spring.datasource.url=jdbc:mysql://${RAILWAY_PRIVATE_DOMAIN}:3306/railway
```

But for **local development**, you MUST use the TCP Proxy values.

---

## How to Test

1. Replace the placeholders with actual values
2. Save `application.properties`
3. Run your Spring Boot application
4. Check console for:
   - ✅ "Successfully connected to database"
   - Or ❌ Connection errors (check domain/port are correct)

---

## Quick Checklist

- [ ] Found TCP Proxy Domain in Railway dashboard
- [ ] Found TCP Proxy Port in Railway dashboard
- [ ] Replaced `[RAILWAY_TCP_PROXY_DOMAIN]` with actual domain
- [ ] Replaced `[RAILWAY_TCP_PROXY_PORT]` with actual port
- [ ] Verified database name is `railway` (not `aeroponics`)
- [ ] Password is `KGziwforModQGBuhRdlKIyXhADoyCgbX`
- [ ] Saved the file
- [ ] Restarted Spring Boot application

---

## Still Can't Find It?

Screenshot what you see in Railway dashboard under:
- MySQL service → Connect tab
- MySQL service → Variables tab
- MySQL service → Settings tab

The TCP proxy information should be visible in one of these locations.
