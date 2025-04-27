package com.devices.app.config.vnpayConfig;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "vnpay")
public class VnpayConfig {
    private String secretKey;
    private String tmnCode;
    private String url;
    private String urlResult;
}
