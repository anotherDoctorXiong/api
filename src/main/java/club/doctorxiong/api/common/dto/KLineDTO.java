package club.doctorxiong.api.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Auther: 熊鑫
 * @Date: 2020/9/4 15
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KLineDTO extends CacheDTO implements Serializable {

    /**
     * 这个是解压后填充
     */
    private String[][] arrData;

    private String[] lastData;

    /**
     * 压缩后的数据
     */
    private byte[] bytesData;

}
