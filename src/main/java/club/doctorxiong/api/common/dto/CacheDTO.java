package club.doctorxiong.api.common.dto;

import lombok.Data;

/**
 * @author DoctorXiong
 * @Description
 * @createTime 2023年04月06日 09:53
 */
@Data
public class CacheDTO {
    /**
     * 数据获取失败 一般是网络原因
     */
    private Integer requestFail = 0;

    /**
     * 数据解析失败  数据源格式问题
     */
    private Integer resolveFail = 0;
}
