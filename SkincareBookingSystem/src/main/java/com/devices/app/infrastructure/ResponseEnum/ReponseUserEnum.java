package com.devices.app.infrastructure.ResponseEnum;

public enum ReponseUserEnum {
    SUCCESS(200,"Thành công"),
    NOT_FOUND(400,"User không tồn tại"),
    UN_AUTHORIZED(401, "Không đủ quyền"),
    FAILED(100, "Thất bại");

    private final String message;
    private final int value;

    ReponseUserEnum(int value, String message) {
        this.message = message;
        this.value = value;
    }
    public String getMessage() {
        return message;
    }
    public int getValue() {return value;}
}
