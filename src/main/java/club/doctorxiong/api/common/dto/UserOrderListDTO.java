package club.doctorxiong.api.common.dto;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiongxin
 * @Date: 2021/3/6 09
 * @Description:
 */
@ToString
@Data
public class UserOrderListDTO implements Serializable {


    private List<TokenDTO> tokenList;

}
