package com.devices.app.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnnualStatisticsDto {
    int year;
    int month;
    int day;
    double total;

    public AnnualStatisticsDto() {

    }

    public AnnualStatisticsDto( int month, double total) {
        this.month = month;
        this.total = total;
    }

}
