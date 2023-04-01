package club.doctorxiong.api.common;




/**
 * Service支持类
 * 提供ServiceImpl实现支持
 *
 * @author
 */

public class InnerException {


    /**
     * 抛出异常 - 错误参数
     *
     * @param errorMessage
     */
    public static void exInvalidParam(String errorMessage) {
        throw new CommonException(errorMessage, CommonResponse.Code.PRECONDITION_FAILD);
    }

    /**
     * 抛出异常 - 资源找不到
     *
     * @param errorMessage
     */
    public static void exNotFound(String errorMessage) {
        throw new CommonException(errorMessage,CommonResponse.Code.NOT_FUND);
    }


    /**
     * 抛出异常 - 丢失参数
     *
     * @param paramName 参数名称
     */
    public static void exMissingParam(String paramName) {
        throw new CommonException(String.format("缺少参数:%s", paramName), CommonResponse.Code.PRECONDITION_FAILD);
    }

    /**
     * 抛出一个自定义异常
     *
     * @param errorMessage 错误消息
     * @param code         代码
     * @param cause        引致异常的底层异常
     */
    public static void ex(String errorMessage, int code, Throwable cause) {
        throw new CommonException(errorMessage, code, cause);
    }

    /**
     * 抛出一个内部异常
     *
     * @param errorMessage 错误消息
     */
    public static void ex(String errorMessage) {
        throw new CommonException(errorMessage,CommonResponse.Code.ERROR);
    }



    /**
     * 抛出一个自定义异常
     *
     * @param errorMessage 错误消息
     * @param code         代码
     */
    public static void ex(String errorMessage, int code) {
        throw new CommonException(errorMessage, code);
    }
    /**
     * @auther: 熊鑫
     * @return:
     * @date: 2019/6/17 14:20
     * @Description: 自定义异常类
     */
    public static class CommonException extends RuntimeException {
        private Integer code;

        public Integer getCode() {
            return this.code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }


        public CommonException(String message, Integer code) {
            super(message);
            this.code = code;
        }
        public CommonException(String message, Integer code, Throwable cause) {
            super(message, cause);
            this.code = code;
        }
    }


}
