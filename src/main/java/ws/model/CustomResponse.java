package ws.model;

/**
 * Reponse cutomisee
 */
public class CustomResponse<T> {
    public int status = 200;
    public T datas;

    private CustomResponse(int status, T datas) {
        this.status = status;
        this.datas = datas;
    }

    public static <T> CustomResponse<T> of (int status, T datas) {
        return new CustomResponse<T>(status, datas);
    }

    public static <T> CustomResponse<T> success (T datas) {
        return CustomResponse.<T>of(200, datas);
    }

    public static <T> CustomResponse<T> error (T datas) {
        return CustomResponse.<T>of(400, datas);
    }
}