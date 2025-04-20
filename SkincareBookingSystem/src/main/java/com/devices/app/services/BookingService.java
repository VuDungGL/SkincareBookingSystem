package com.devices.app.services;

import com.devices.app.dtos.dto.AnnualStatisticsDto;
import com.devices.app.dtos.dto.BookingDto;
import com.devices.app.dtos.dto.RevenueDto;
import com.devices.app.dtos.dto.SkinTherapistDto;
import com.devices.app.dtos.requests.CreateBookingRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.Booking;
import com.devices.app.models.BookingDetail;
import com.devices.app.models.Services;
import com.devices.app.models.WorkSchedule;
import com.devices.app.repository.*;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FeedbackRepository feedbackRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final ServicesService servicesService;

    public BookingService(BookingRepository bookingRepository, FeedbackRepository feedbackRepository, WorkScheduleRepository workScheduleRepository, BookingDetailRepository bookingDetailRepository, ServicesService servicesService) {
        this.bookingRepository = bookingRepository;
        this.feedbackRepository = feedbackRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.bookingDetailRepository = bookingDetailRepository;
        this.servicesService = servicesService;
    }

    public long GetTotal(int status) {
        return bookingRepository.getTotalBookingByStatus(status);
    }

    public Double GetRating() {
        Double rating = feedbackRepository.getRatingFeedback();
        return (rating != null) ? rating : 0.0;
    }

    public int GetTotalFeedback() {
        return feedbackRepository.getTotalFeedback();
    }

    public BookingDto BestSaler() {
        List<BookingDto> bestSellers = bookingRepository.findBestSaler();
        return bestSellers.isEmpty() ? null : bestSellers.get(0);
    }

    public RevenueDto getMonthlyRevenue() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1);

        return bookingRepository.getMonthlyRevenue(Date.valueOf(startDate), Date.valueOf(endDate));
    }
    public List<AnnualStatisticsDto> AnnualSale(int year){
        List<Tuple> results = bookingRepository.AnnualSale(year);

        return results.stream()
                .map(row -> new AnnualStatisticsDto(
                        ((Number) row.get(0)).intValue(),
                        ((Number) row.get(1)).doubleValue()
                ))
                .collect(Collectors.toList());
    }
    public List<AnnualStatisticsDto> AnnualSaleOnLast7Day(int isPaid, int year) {
        List<Tuple> results = bookingRepository.AnnualSaleOnLast7Day(isPaid, year);

        return results.stream()
                .map(row -> new AnnualStatisticsDto(
                        row.get(0, Integer.class),
                        ((Number) row.get(1)).doubleValue()
                ))
                .collect(Collectors.toList());
    }
    public List<SkinTherapistDto> getUsersByPage(int pageIndex, int pageSize) {
        int offset = (pageIndex - 1) * pageSize + 1;
        return bookingRepository.GetListUserWorkMonth(offset, pageSize);

    }

    public ApiResponse<BookingDetail> onBooking(CreateBookingRequest request) {
        if(request == null) {
            return new ApiResponse<>(100, "Đặt lịch thất bại!", null);
        }

        Booking booking = new Booking();
        booking.setUserID(request.getUserID());
        booking.setBookingDate(Instant.now());
        bookingRepository.save(booking);

        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setBookingID(booking.getId());
        bookingDetail.setServiceID(request.getServiceID());
        bookingDetail.setFullName(request.getFullName());
        bookingDetail.setPhone(request.getPhone());
        bookingDetail.setEmail(request.getEmail());
        bookingDetail.setNote(request.getNote());
        bookingDetail.setStatus(0);
        bookingDetail.setIsPaid(false);
        bookingDetail.setSkinTherapistID(null);
        bookingDetail.setPrice(request.getPrice());
        bookingDetail.setPromotionID(null);
        bookingDetailRepository.save(bookingDetail);

        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setBookingDetailID(bookingDetail.getId());
        ApiResponse<Services> service = servicesService.findById(request.getServiceID());
        int duration = service.getData().getDuration();
        LocalTime endTime = request.getStartTime().plusMinutes(duration);
        workSchedule.setWorkDate(request.getWorkDate());
        workSchedule.setStartTime(request.getStartTime());
        workSchedule.setEndTime(endTime);
        workScheduleRepository.save(workSchedule);
        return new ApiResponse<>(200,"Đặt lịch thành công",bookingDetail);
    }

    public ApiResponse<String> onChooseTherapist(Integer therapistID, Integer bookingDetailID) {
        if(bookingDetailID == null) {
            return new ApiResponse<>(100,"Thất bại", null);
        }
        Optional<BookingDetail> bookingDetailOp = bookingDetailRepository.findById(bookingDetailID);
        BookingDetail bookingDetail =  bookingDetailOp.get();
        bookingDetail.setSkinTherapistID(therapistID);
        bookingDetailRepository.save(bookingDetail);
        return new ApiResponse<>(200,"Thành công", null);
    }
}