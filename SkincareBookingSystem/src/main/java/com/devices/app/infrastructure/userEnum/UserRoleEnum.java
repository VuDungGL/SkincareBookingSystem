package com.devices.app.infrastructure.userEnum;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    MASTER_ADMIN(0, "Master Admin"),
    ADMIN(1, "Admin"),
    MEMBER(2, "Member"),
    STAFF(3, "Staff");

    private final int value;
    private final String description;

    UserRoleEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static UserRoleEnum fromValue(int value) {
        for (UserRoleEnum e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy EnumRoleUser với value: " + value);
    }
}
