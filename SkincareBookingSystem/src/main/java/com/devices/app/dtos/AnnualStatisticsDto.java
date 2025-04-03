package com.devices.app.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnnualStatisticsDto {
    private int year;
    private int month;
    private int day;
    private double total;

    public AnnualStatisticsDto() {

    }

    public AnnualStatisticsDto( int month, double total) {
        this.month = month;
        this.total = total;
    }

}
