package xyz.hsong.oexam.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

//忽略值为NULL的对象
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> {

    //返回状态码
    private int error;
    //返回信息
    private String msg;
    //返回数据
    private T data;

    private ServerResponse(int error) {
        this.error = error;
    }

    private ServerResponse(int error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    private ServerResponse(int error, T data) {
        this.error = error;
        this.data = data;
    }

    private ServerResponse(int error, String msg, T data) {
        this.error = error;
        this.msg = msg;
        this.data = data;
    }

    // 序列化时忽略此属性
    @JsonIgnore
    public boolean isSuccess() {
        return this.error == ResponseCode.SUCCESS.getCode();
    }

    public int getError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServerResponse<T> createSuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createSuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createSuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createError(String msg) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> ServerResponse<T> createError(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createError(int errorCode, String msg) {
        return new ServerResponse<T>(errorCode, msg);
    }

}
