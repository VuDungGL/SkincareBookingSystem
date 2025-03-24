package com.devices.app.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RevenueDto {
    private int year;
    private String month;
    private Double revenue;

    public RevenueDto(int year, String month, Double revenue) {
        this.year = year;
        this.month = month;
        this.revenue = revenue;
    }

}
