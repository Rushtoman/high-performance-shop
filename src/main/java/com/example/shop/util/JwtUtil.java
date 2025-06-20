package com.example.shop.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.security.Key;

/**
 * JWT工具类，生成和校验JWT令牌
 */
public class JwtUtil {
    // 密钥，必须32位以上
    private static final String SECRET = "shop-jwt-secret-key-shop-jwt-secret-key";
    // 令牌有效期：1小时（单位：毫秒）
    private static final long EXPIRATION = 60 * 60 * 1000;
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * 生成JWT令牌
     * @param username 用户名
     * @return JWT字符串
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    /**
     * 从JWT中解析用户名
     * @param token JWT字符串
     * @return 用户名
     */
    public static String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 校验JWT令牌有效性
     * @param token JWT字符串
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
} 