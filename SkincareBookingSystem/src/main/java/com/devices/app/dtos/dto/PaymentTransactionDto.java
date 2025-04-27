package com.devices.app.dtos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionDto {
    private Integer bookingDetailID;
    private BigDecimal amount;
    private String paymentMethod;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
    private OffsetDateTime transactionDate;
    private String transactionCode;
    private String transactionStatus;
    private String paymentStatus;
}
