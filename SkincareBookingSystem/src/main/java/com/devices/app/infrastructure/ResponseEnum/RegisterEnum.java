package com.devices.app.infrastructure.ResponseEnum;

public enum RegisterEnum {
    SUCCESS_REGISTER("Đăng ký thành công!"),
    USERNAME_EXISTS("Tên đăng nhập đã tồn tại!"),
    EMAIL_EXISTS("Email đã tồn tại!");

    private final String message;

    RegisterEnum(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
