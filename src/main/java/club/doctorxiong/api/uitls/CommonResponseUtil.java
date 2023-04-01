package club.doctorxiong.api.uitls;


import club.doctorxiong.api.common.CommonResponse;
import club.doctorxiong.api.common.InnerException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author XiongXin
 * @date 2021/11/12
 */
@Slf4j
public class CommonResponseUtil {
    /**
     * 校验加返回
     *
     * @param response CommonResponse
     * @return T
     * @author xiongxin
     */
    public static <T> T getCommonReturn(CommonResponse<T> response) {
        if (response == null) {
            InnerException.ex(CommonResponse.Message.ERROR);
        }
        if (!response.isSuccess()) {
            InnerException.ex(response.getMessage(),response.getCode());
        }
        return response.getData();
    }
}
