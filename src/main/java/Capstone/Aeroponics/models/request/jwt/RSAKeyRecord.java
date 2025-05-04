package Capstone.Aeroponics.models.request.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * The type Rsa key record.
 */
@ConfigurationProperties(prefix = "jwt.rsa")
public record RSAKeyRecord(RSAPublicKey publicKey, RSAPrivateKey privateKey) {

}
