package com.devices.app.infrastructure.userEnum;

import lombok.Getter;

@Getter
public enum StaffStatusEnum {
    ACTIVE(0,"Đã kích hoạt"),
    SUSPENDED(1, "Đã hủy"),
    RESIGNED(2,"Đã từ chức");

    private final int value;
    private final String description;

    StaffStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static StaffStatusEnum fromValue(int value) {
        for (StaffStatusEnum e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy StaffStatusEnum với value: " + value);
    }
}
