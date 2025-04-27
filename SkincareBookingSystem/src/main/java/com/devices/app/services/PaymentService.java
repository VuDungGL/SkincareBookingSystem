package com.devices.app.services;

import com.devices.app.dtos.dto.PaymentTransactionDto;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Payment;
import com.devices.app.models.PaymentTransaction;
import com.devices.app.repository.PaymentRepository;
import com.devices.app.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;

    public PaymentService(PaymentTransactionRepository paymentTransactionRepository, PaymentRepository paymentRepository, BookingService bookingService) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
    }

    @Transactional
    public ApiResponse<String> saveTransaction(PaymentTransactionDto paymentTransaction) {
        Optional<Payment> existingPaymentOpt = paymentRepository.findByBookingID(paymentTransaction.getBookingDetailID());

        if (existingPaymentOpt.isPresent()) {
            return new ApiResponse<>(100, "Lịch đã được thanh toán", null);
        }
        BigDecimal amount = paymentTransaction.getAmount().divide(new BigDecimal(100));
        Integer bookingID = bookingService.getBooking(paymentTransaction.getBookingDetailID()).getData().getId();
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setBookingID(bookingID);
        payment.setPaymentMethod(paymentTransaction.getPaymentMethod());
        payment.setTransactionDate(paymentTransaction.getTransactionDate() != null
                ? paymentTransaction.getTransactionDate()
                : OffsetDateTime.now());
        payment.setPaymentStatus(paymentTransaction.getPaymentStatus());
        payment.setPaymentStatus(paymentTransaction.getPaymentStatus());

        paymentRepository.save(payment);

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setPaymentID(payment.getId());
        transaction.setTransactionCode(paymentTransaction.getTransactionCode());
        transaction.setTransactionStatus(paymentTransaction.getTransactionStatus());

        paymentTransactionRepository.save(transaction);

        return new ApiResponse<>(200, "OK", null);
    }

}
