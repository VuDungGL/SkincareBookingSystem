package com.devices.app.services;

import com.devices.app.config.jwt.JwtProperties;
import com.devices.app.dtos.response.TokenInfo;
import com.devices.app.infrastructure.userEnum.UserRoleEnum;
import com.devices.app.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTService {
    private final JwtProperties jwtProperties;

    public JWTService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Claims decodeToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8))
                    .requireIssuer(jwtProperties.getIssuer())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }

    public TokenInfo generateTokenMaster() {
        Key key = Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 60 * 60 * 1000L);
        String token = Jwts.builder()
                .setSubject("MASTER_ADMIN")
                .claim("id", 0) // ID giả định, hoặc "MASTER"
                .claim("firstName", "Master" )
                .claim("password", "")
                .claim("lastName", "Admin" )
                .claim("typ", 0) // Enum giá trị là 0 chẳng hạn
                .claim("email", "MAdminBookingSystem@gmail.com")
                .claim("birthdate", "1970-01-01")
                .claim("gender", 1)
                .claim("avt", "/assets/images/base/admin/default-users/male-employee-wearing.png")
                .claim("phone", "0000000000")
                .claim("status", 1)
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        LocalDateTime expireDateTime = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return new TokenInfo(token,expireDateTime) ;
    }

    public TokenInfo generateToken(Users user, int expireMinutes) {
        Key key = Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireMinutes * 60 * 1000L);


        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("id", UUID.randomUUID().toString())
                .claim("firstName", (user.getFirstName() != null ? user.getFirstName() : "") )
                .claim("lastName", (user.getLastName() != null ? user.getLastName() : "") )
                .claim("password", user.getPassword()!= null? user.getPassword(): "")
                .claim("typ", user.getRoleID() != null ? user.getRoleID() : 0)
                .claim("email", user.getEmail() != null ? user.getEmail() : "")
                .claim("birthdate", user.getBirthDay() != null ? user.getBirthDay().toString() : "")
                .claim("gender", user.getGender() != null ? user.getGender() : 0)
                .claim("avt", user.getAvt() != null
                        ? user.getAvt()
                        : "assets/images/base/admin/default-users/female-user-wearing.png")
                .claim("phone", user.getPhone() != null ? user.getPhone() : "")
                .claim("status", user.getStatus() != null ? user.getStatus() : 0)

                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        LocalDateTime expireDateTime = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return new TokenInfo(token,expireDateTime) ;
    }


    //refesh token khi hết hạn
    public String refreshToken(String oldToken) {
        Claims claims = decodeToken(oldToken);
        if (claims == null) {
            return "";
        }

        // Lấy thông tin từ properties
        String key = jwtProperties.getKey();
        int refreshTokenExpiry = jwtProperties.getRefreshToken().getExpireTime();

        // Tạo lại token mới
        Key secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiry * 60 * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    //Lấy thông tin User từ token
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("access_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
