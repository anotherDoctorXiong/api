package club.doctorxiong.api.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import java.io.Serializable;

/**
 * HTTP统一返回数据结构
 * @author xiongxin
 */
@Data
public class CommonResponse<T> implements Serializable {

    /**
     * 返回代码
     * 参考 <code>HttpResponse.Code</code>
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 链路ID
     */
    private String traceId;


    /**
     * 返回核心数据,相当于payload
     */
    private T data;

    private Object meta;


    /**
     * 构造一个操作成功的响应
     *
     * @param data
     * @return
     */
    public static <T> CommonResponse OK(T data) {
        CommonResponse response = new CommonResponse();
        response.setCode(Code.OK);
        response.setMessage(Message.OK);
        response.setData(data);
        return response;
    }

    public static <T> CommonResponse OK() {
        CommonResponse response = new CommonResponse();
        response.setCode(Code.OK);
        response.setMessage(Message.OK);
        return response;
    }


    /**
     * 一般性操作失败响应
     *
     * @param data
     * @return
     */
    public static <T> CommonResponse FAIL(T data) {
        CommonResponse response = new CommonResponse();
        response.setCode(Code.FAIL);
        response.setMessage(Message.FAIL);
        response.setData(data);
        return response;
    }
    /**
     * 一般性操作失败响应
     *
     * @param message
     * @return
     */
    public static <T> CommonResponse FAIL(String message) {
        CommonResponse response = new CommonResponse();
        response.setCode(Code.FAIL);
        response.setMessage(message);

        return response;
    }



    /**
     * HttpResponse类Code属性常量
     * 请参考HTTP状态码 https://www.restapitutorial.com/httpstatuscodes.html
     */
    public interface Code {

        /** 2XX 代码 **/
        int OK = 200;

        /** 4XX 代码 **/
        int FAIL = 400;

        int NOT_FUND = 404;

        int PRECONDITION_FAILD = 412;
        int PRECONDITION_REQUIRED = 428;

        /** 500 **/
        int ERROR = 500;

        /** 6xx代码为自定义错误，请定义到具体项目中 **/
        //6xx 代码定义: 略
    }

    /**
     * HttpResponse类Message属性常量
     */
    public interface Message {

        String OK = "操作成功";

        String FAIL = "操作失败";

        String ERROR = "内部错误";

        String NOT_FOUND = "NOT FOUND";

        String PRECONDITION_REQUIRED = "Precondition Required";

        String PRECONDITION_FAILED = "Precondition Failed";
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.getCode().equals(Code.OK);
    }


    /**
     * 添加一个处理异常的getData方法
     */
    /*@JsonIgnore
    public T getCommonReturn() {
        if (this == null) {
            InnerException.ex(Message.ERROR);
        }
        if (!this.isSuccess()) {
            InnerException.ex(this.getMessage(),this.getCode());
        }
        return this.getData();
    }*/
}
