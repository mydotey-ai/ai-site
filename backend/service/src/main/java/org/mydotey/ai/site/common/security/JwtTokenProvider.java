package org.mydotey.ai.site.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Token 提供者
 *
 * @author AI-Site
 */
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public JwtTokenProvider(
            @Value("${jwt.secret:ai-site-jwt-secret-key-must-be-at-least-256-bits-long}") String secret,
            @Value("${jwt.access-token-validity:3600000}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity:604800000}") long refreshTokenValidity
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    /**
     * 生成访问令牌
     *
     * @param username 用户名
     * @return JWT 令牌
     */
    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenValidity);
    }

    /**
     * 生成刷新令牌
     *
     * @param username 用户名
     * @return JWT 令牌
     */
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenValidity);
    }

    /**
     * 生成令牌
     */
    private String generateToken(String username, long validity) {
        Date now = new Date();
        Date validityDate = new Date(now.getTime() + validity);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(validityDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token JWT 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * 验证令牌
     *
     * @param token JWT 令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 检查令牌是否过期
     *
     * @param token JWT 令牌
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
}