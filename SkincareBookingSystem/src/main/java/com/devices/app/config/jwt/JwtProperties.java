package com.devices.app.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.Key;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String issuer;
    private String audience;
    private String key;
    private AccessToken accessToken;
    private RefreshToken refreshToken;

    @Setter
    @Getter
    public static class AccessToken {
        private int expireTime;

    }

    @Setter
    @Getter
    public static class RefreshToken {
        private int expireTime;

    }

}
