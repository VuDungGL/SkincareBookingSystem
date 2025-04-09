package com.devices.app.infrastructure.userEnum;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER(2),
    ADMIN(1),
    MASTER_ADMIN(0);

    private final int value;

    UserRoleEnum(int value) {
        this.value = value;
    }

    public static UserRoleEnum fromValue(int value) {
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role value: " + value);
    }
}

