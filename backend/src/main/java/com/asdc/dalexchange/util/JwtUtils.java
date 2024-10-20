package com.asdc.dalexchange.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtUtils is a utility class for generating and validating JWT tokens.
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Generates a JWT token for the given username.
     *
     * @param username the username
     * @return the JWT token
     */
    public String generateToken(String username) {
        log.info("Generating token for username: {}", username);
        Map<String, Object> claims = new HashMap<>();
        String token = doGenerateToken(claims, username);
        log.info("Token generated successfully for username: {}", username);
        return token;
    }

    /**
     * Generates a JWT token with the given claims and subject.
     *
     * @param claims the claims
     * @param subject the subject (username)
     * @return the JWT token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username
     */
    public String getUsernameFromToken(String token) {
        log.debug("Extracting username from token.");
        String username = getClaimFromToken(token, Claims::getSubject);
        log.debug("Extracted username: {}", username);
        return username;
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param <T> the type of the claim
     * @param token the JWT token
     * @param claimsResolver a function to extract the claim
     * @return the claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims getAllClaimsFromToken(String token) {
        log.debug("Extracting all claims from token.");
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates the JWT token with the given username.
     *
     * @param token the JWT token
     * @param username the username
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, String username) {
        log.info("Validating token for username: {}", username);
        final String tokenUsername = getUsernameFromToken(token);
        Boolean isValid = (tokenUsername.equals(username) && !isTokenExpired(token));
        if (isValid) {
            log.info("Token is valid for username: {}", username);
        } else {
            log.warn("Token is invalid for username: {}", username);
        }
        return isValid;
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        Boolean isExpired = expiration.before(new Date());
        log.debug("Token expiration date: {}", expiration);
        return isExpired;
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        log.debug("Extracting expiration date from token.");
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
