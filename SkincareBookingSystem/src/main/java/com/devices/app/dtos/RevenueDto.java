package com.devices.app.dtos;

public class RevenueDto {
    private int year;
    private String month;
    private Double revenue;

    public RevenueDto(int year, String month, Double revenue) {
        this.year = year;
        this.month = month;
        this.revenue = revenue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}
