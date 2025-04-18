package com.devices.app.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
    private Integer id = null;
    private String serviceName;
    private Integer price;
    private Integer duration;
    private String description;
    private MultipartFile image;
    private Integer categoryID = null;
}
