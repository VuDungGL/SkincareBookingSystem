package com.devices.app.services;

import com.devices.app.dtos.dto.*;
import com.devices.app.dtos.requests.CreateBookingRequest;
import com.devices.app.dtos.response.ApiResponse;
import com.devices.app.models.*;
import com.devices.app.repository.*;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, FeedbackRepository feedbackRepository, WorkScheduleRepository workScheduleRepository, BookingDetailRepository bookingDetailRepository, ServicesService servicesService, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.feedbackRepository = feedbackRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.bookingDetailRepository = bookingDetailRepository;
        this.servicesService = servicesService;
        this.userRepository = userRepository;
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
    public ApiResponse<List<BookingDetail>> getBookingDetail() {
        return new ApiResponse<>(200, "Tìm thành công", bookingDetailRepository.findAll());
    }

    public Page<BookingDetailDto> getListBooking(String search, int page, int size, boolean isPaid) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Tuple> results = bookingDetailRepository.bookingDetail(search,pageable,isPaid);

            if (results.isEmpty()) {
                return Page.empty(pageable);
            }
            List<BookingDetailDto> dtoList = results.stream().map(tuple -> new BookingDetailDto(
                    Optional.ofNullable(tuple.get("BookingDetailID", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("Email", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("FullName", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("Phone", String.class)).orElse(""),
                    Optional.ofNullable(tuple.get("CreateDate", String.class))
                            .map(str -> LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    .atOffset(ZoneOffset.UTC))
                            .orElse(null),
                    Optional.ofNullable(tuple.get("SkinTherapistID", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("ServiceID", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("Price", Double.class)).orElse(0.0),
                    Optional.ofNullable(tuple.get("IsPaid", Boolean.class)).orElse(false),
                    Optional.ofNullable(tuple.get("Status", Integer.class)).orElse(0),
                    Optional.ofNullable(tuple.get("WorkDate", Date.class))
                            .map(date -> ((Date) date).toLocalDate())
                            .orElse(LocalDate.of(2000, 1, 1)),
                    Optional.ofNullable(tuple.get("StartTime", Time.class))
                            .map(time -> ((Time) time).toLocalTime())
                            .orElse(LocalTime.of(0, 0)),
                    Optional.ofNullable(tuple.get("EndTime", Time.class))
                            .map(time -> ((Time) time).toLocalTime())
                            .orElse(LocalTime.of(0, 0))
            )).collect(Collectors.toList());

            return new PageImpl<>(dtoList, pageable, results.getTotalElements());
        } catch (Exception ex) {
            return Page.empty(pageable);
        }
    }

    public ApiResponse<String> checkOutBooking(int bookingDetailID) {
        Optional<BookingDetail> bookingDetailOp = bookingDetailRepository.findById(bookingDetailID);
        BookingDetail bookingDetail =  bookingDetailOp.get();
        bookingDetail.setStatus(1);
        bookingDetailRepository.save(bookingDetail);
        Integer userID = bookingDetailRepository.getUserIDFromBooking(bookingDetailID);
        Optional<Users> userOp = userRepository.findById(userID);
        Users user = userOp.get();
        Integer total = bookingDetailRepository.getTotalBookingSuccessful(userID);
        if (total == 1 && user.getStatus() != 1) {
            user.setStatus(1);
            userRepository.save(user);
            return new ApiResponse<>(200,"Khách hàng đã trở thành khách hàng mới. Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.", null);
        }else if(total >= 2 && user.getStatus() != 2){
            user.setStatus(2);
            userRepository.save(user);
            return new ApiResponse<>(200,"Khách hàng đã trở thành khách hàng trung thành. Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.", null);
        }else if(total >= 5 && user.getStatus() != 3){
            user.setStatus(3);
            userRepository.save(user);
            return new ApiResponse<>(200,"Khách hàng đã trở thành khách hàng VIP. Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.", null);
        }
        return new ApiResponse<>(200,"Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.", null);
    }


}