package com.devices.app.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.OffsetDateTime;

@Setter
@Getter
public class UserCreationRequest {
    private int userID;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 50, message = "Tên đăng nhập không được quá 50 ký tự")
    private String userName;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 26, message = "Mật khẩu phải có từ 8 đến 26 ký tự")
    private String password;

    @Min(value = 0, message = "RoleId không hợp lệ")
    @Max(value = 3, message = "RoleId không hợp lệ")
    private int roleID;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Họ không được để trống")
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    private String lastName;

    @Pattern(regexp = "^\\d{10,15}$", message = "Số điện thoại phải có 10-15 chữ số")
    private String phone;

    @Min(value = 0, message = "Trạng thái người dùng không hợp lệ")
    @Max(value = 1, message = "Trạng thái người dùng không hợp lệ")
    private int userStatus;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private OffsetDateTime birthDay;

    private OffsetDateTime userCreateDate; // Không cần validate nếu được sinh tự động

    private String avt; // Có thể thêm regex nếu cần validate URL ảnh

    @Min(value = 0, message = "Giới tính không hợp lệ")
    @Max(value = 2, message = "Giới tính không hợp lệ")
    private int gender;

    @Min(value = 1, message = "Phòng ban không hợp lệ")
    @Max(value = 8, message = "Phòng ban không hợp lệ")
    private int department;

    private String expertise;

    @Min(value = 0, message = "Số năm kinh nghiệm không hợp lệ")
    private int experience;

    @PositiveOrZero(message = "Lương không thể là số âm")
    private int salary;

    @Min(value = 0, message = "Trạng thái nhân viên không hợp lệ")
    @Max(value = 1, message = "Trạng thái nhân viên không hợp lệ")
    private int staffStatus;

    private OffsetDateTime staffCreateDate;

    @Pattern(regexp = "^\\d{10,20}$", message = "Số tài khoản ngân hàng không hợp lệ")
    private String bankAccount;

    private String bankName;

    private String position;
}
