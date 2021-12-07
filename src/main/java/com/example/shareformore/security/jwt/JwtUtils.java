package com.example.shareformore.security.jwt;

import com.example.shareformore.entity.User;
import com.example.shareformore.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Component
public class JwtUtils {
    final private static HashMap<String, Object> header = new HashMap<>();
    private static String issuer;
    private static String secret;
    private static long timeLimit;

    private JwtService jwtService;
    private JwtConfig jwtConfig;

    @Autowired
    public JwtUtils(JwtConfig jwtConfig, JwtService jwtService) {
        this.jwtConfig = jwtConfig;
        this.jwtService = jwtService;
    }

    @PostConstruct
    public void init() {
        issuer = jwtConfig.getIssuer();
        secret = jwtConfig.getSecret();
        timeLimit = jwtConfig.getTimeLimit();
        header.put("type", "token");
        header.put("alg", "HS512");
    }

    //根据用户生成令牌
    public String generateToken(String username) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder().
                setHeader(header).
                setId(UUID.randomUUID().toString()).
                setIssuer(issuer).
                setAudience(username).
                setIssuedAt(new Date(currentTime)).
                setExpiration(new Date(currentTime + timeLimit)).
                signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // 获取用户名
    public static String getUsername(String token) {
        return getAllClaims(token).getAudience();
    }

    // 验证令牌有效性
    public User validateToken(String token) {
        final Claims claims = getAllClaims(token);
        final String username = claims.getAudience();
        final Date expiration = claims.getExpiration();
        if (expiration.after(new Date())) {
            User user = (User) jwtService.loadUserByUsername(username);
            if (user != null) {
                return user;
            }
        }
        return null;
    }

    //从令牌获取全部Claim
    private static Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}