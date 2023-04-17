package club.doctorxiong.api.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-02
 */

@NoArgsConstructor
@Data
@ToString
public class TokenDTO {


    private String phone;

    /**
     * 失效日期
     */
    private LocalDate endDate;

    /**
     * token
     */
    private String token;

    /**
     * 1，2，3
     */
    private Integer type;

    /**
     * 访问次数
     */
    private Integer times = 0;

    public TokenDTO(String ip) {
        this.token = ip;
        this.type = 1;
    }

    /**
     * token 访问
     */
    public boolean tokenRefreshTimes(){
        this.times ++;
        if(this.type.equals(1) && this.times > 100){
            return false;
        }
        if(this.type.equals(3) && this.times > 10000){
            return false;
        }
        return true;
    }




}
