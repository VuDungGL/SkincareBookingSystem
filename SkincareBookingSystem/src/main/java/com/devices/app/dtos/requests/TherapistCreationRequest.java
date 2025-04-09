package com.devices.app.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

@Getter
@Setter
@Data
public class TherapistCreationRequest {

    @NotNull(message = "Skin Therapist ID không được để trống")
    private Integer skinTherapistID;

    @NotNull(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email phải có dạng abc@gmail.com")
    private String email;

    @NotNull(message = "Họ không được để trống")
    @NotBlank(message = "Họ không được để trống")
    @Pattern(regexp = "^[a-zA-ZÀ-Ỹà-ỹ\\s]+$", message = "Họ không được chứa ký tự đặc biệt")
    private String firstName;

    @NotNull(message = "Tên không được để trống")
    @NotBlank(message = "Tên không được để trống")
    @Pattern(regexp = "^[a-zA-ZÀ-Ỹà-ỹ\\s]+$", message = "Tên không được chứa ký tự đặc biệt")
    private String lastName;

    @NotNull(message = "Số điện thoại không được để trống")
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0[0-9]{9,11}$", message = "Số điện thoại phải bắt đầu bằng 0 và có 10-12 chữ số")
    private String phone;

    @NotNull(message = "Ảnh đại diện không được để trống")
    private String avt;

    @NotNull(message = "Giới tính không được để trống")
    @Min(value = 0, message = "Giới tính phải tối thiểu là 0")
    @Max(value = 2, message = "Giới tính tối đa có thể truyền là 2")
    private Integer gender;

    @NotNull(message = "Chuyên môn không được để trống")
    @NotBlank(message = "Chuyên môn không được để trống")
    private String expertise;

    @NotNull(message = "Kinh nghiệm không được để trống")
    @Min(value = 0, message = "Kinh nghiệm không thể âm")
    private Integer experience;

    @NotNull(message = "Lương không được để trống")
    @Min(value = 0, message = "Lương không thể âm")
    private Integer salary;

    @NotNull(message = "Trạng thái không được để trống")
    private Integer status;

    @NotNull(message = "Trạng thái không được để trống")
    private OffsetDateTime birthDate;

    private MultipartFile avatar;

}
