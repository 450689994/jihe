package com.tao.back_project.vo;

public class Result {
    private int code;   //200成功，500失败
    private boolean success;
    private String message;
    private Object data;

    public Result(int code, boolean success, String message, Object data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public int getCode(){
        return code;
    }

    public boolean isSuccess(){
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
