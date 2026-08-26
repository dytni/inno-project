package by.dytni.gateway.service.impl;

import static by.dytni.gateway.GatewayConstants.HttpHeader.ROLE_CLAIM;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import by.dytni.gateway.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {


    @Value("${jwt.secret}")
    private String secret;


    @Override
    public boolean validate(String token) {
        log.info("Validate access token {}", token);
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (ExpiredJwtException e) {
            log.debug("JWT token expired");
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature");
        } catch (JwtException e) {
            log.debug("Invalid JWT token");
        }

        return false;
    }

    @Override
    public Long getUserId(String token) {
        log.info("Get user id from token {}", token);
        return Long.parseLong(extractAllClaims(token).getSubject());
    }

    @Override
    public String getUserRole(String token) {
        log.info("Get user role from token {}", token);
        return extractAllClaims(token).get(ROLE_CLAIM,  String.class);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}