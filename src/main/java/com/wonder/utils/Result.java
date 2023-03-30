package com.wonder.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 5187687995319002219L;
    private Integer code;
    private Boolean success;
    private String message;
    private T data;

    public static <T> Result<T> definition(int code, String message, Boolean success, T data) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setSuccess(success);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok(String message, T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage(message);
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("操作成功");
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok(String message) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setSuccess(true);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> ok() {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setSuccess(true);
        r.setMessage("操作成功");
        return r;
    }

    public static <T> Result<T> error() {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setSuccess(false);
        r.setMessage("操作失败");
        return r;
    }

    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setSuccess(false);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setSuccess(false);
        r.setMessage(message);
        return r;
    }

}