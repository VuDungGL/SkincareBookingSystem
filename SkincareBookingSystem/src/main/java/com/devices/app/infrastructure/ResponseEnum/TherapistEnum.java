package com.devices.app.infrastructure.ResponseEnum;

import lombok.Getter;

@Getter
public enum TherapistEnum {
    SUCCESS(200, "Thành công"),
    NO_CONTENT(100, "Không có dữ liệu"),
    UNAUTHORIZED(401, "Không có quyền truy cập");


    private final int value;
    private final String message;

    TherapistEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }
    public static TherapistEnum fromValue(int value) {
        for (TherapistEnum e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy TherapistEnum với value: " + value);
    }

}
