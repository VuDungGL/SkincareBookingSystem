package com.devices.app.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "app.emailsetting")
@Getter
@Setter
public class EmailSetting {
    private String sender;
    private String loginPassword;
    private String password;
}