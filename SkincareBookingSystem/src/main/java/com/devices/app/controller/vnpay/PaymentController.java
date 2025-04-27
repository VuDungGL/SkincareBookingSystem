package com.devices.app.controller.vnpay;

import com.devices.app.dtos.dto.PaymentTransactionDto;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.services.BookingService;
import com.devices.app.services.PaymentService;
import com.devices.app.services.VnpayService;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final VnpayService vnpayService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public PaymentController(VnpayService vnpayService, BookingService bookingService, PaymentService paymentService) {
        this.vnpayService = vnpayService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    @PostMapping("/create-payment-url")
    public ResponseEntity<String> createPaymentUrl(@RequestBody Map<String, String> requestParams) {
        try {

            JsonObject paymentUrl = vnpayService.createPaymentUrl(requestParams);
            return ResponseEntity.ok(paymentUrl.toString());
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("code", "99");
            errorResponse.addProperty("message", "Create payment url failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @PostMapping("/isPaid")
    public ResponseEntity<String> isPaid(@RequestBody Map<String, Integer> requestParams) {
        Integer bookingDetailID = requestParams.get("bookingDetailID");
        bookingService.isPaid(bookingDetailID);
        return ResponseEntity.ok("Paid successfully");
    }

    @PostMapping("/saveTransaction")
    public ResponseEntity<ApiResponse<String>> saveTransaction(@RequestBody PaymentTransactionDto paymentTransactionDto) {
        ApiResponse<String> response = paymentService.saveTransaction(paymentTransactionDto);
        if (response.getStatus() == 200) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }

}
