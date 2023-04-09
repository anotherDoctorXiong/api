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

    private String[][] arrData;

}
