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
public class TokenDTO implements Serializable {


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

}
