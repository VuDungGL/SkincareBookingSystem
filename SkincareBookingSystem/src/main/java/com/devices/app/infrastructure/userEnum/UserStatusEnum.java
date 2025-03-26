package com.devices.app.infrastructure.userEnum;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    NEW_CUSTOMER(0,"Khách hàng mới"),
    LOYAL_CUSTOMER(1, "Khách hàng quen thuộc"),
    VIP_CUSTOMER(2, "Khách hàng VIP"),
    OLD_CUSTOMER(3, "Khách hàng cũ");

    private final int value;
    private final String description;

    UserStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }
    public static UserStatusEnum fromValue(int value) {
        for (UserStatusEnum e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy UserStatusEnum với value: " + value);
    }
}
