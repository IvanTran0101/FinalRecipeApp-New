package vn.edu.tdtu.anhminh.myapplication.Utils;

public class Result<T> {
    public enum Status{
        SUCCESS,
        ERROR,
        LOADING
    }

    public final T data;
    public final String message;
    public final Status status;

    private Result(Status status, T data, String message){
        this.status = status;
        this.data = data;
        this.message = message;
    }
    public static <T> Result<T> success(T data){
        return new Result<>(Status.SUCCESS,data, null);
    }

    public static <T> Result<T> error(T data){
        return new Result<>(Status.ERROR,data, null);
    }

    public static <T> Result<T> loading(T data){
        return new Result<>(Status.LOADING,data, null);
    }

}
