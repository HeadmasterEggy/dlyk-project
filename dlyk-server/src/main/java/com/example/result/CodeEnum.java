package com.example.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeEnum {
    OK(200, "成功"),

    FAIL(500, "失败"),

    TOKEN_IS_EMPTY(901, "请求token参数为空"),

    TOKEN_IS_EXPIRED(902, "请求token已过期"),

    TOKEN_IS_ERROR(903, "请求token有误"),

    TOKEN_IS_NOT_MATCH(904, "请求token不匹配");

    private int code;
    private String msg;
}
