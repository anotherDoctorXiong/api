package club.doctorxiong.api.common.dto;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * @author : 熊鑫
 * @ClassName : Fund
 * @description : 基金详情信息
 * @date : 2019/6/6 16:43
 */
@Data
@ToString
@Slf4j
public class FundCacheDTO extends CacheDTO implements Serializable {
    /**
     * 基金对象
     * */
    private FundDTO fundDTO;
    /**
     * 基金代码
     * */
    private String code;
    /**
     * 基金压缩数据
     * */
    private byte[] fundBytesData;
    /**
     * 净值更新日期 指定格式
     * */
    private LocalDate netWorthDate;
}