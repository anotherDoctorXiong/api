package club.doctorxiong.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName("token")
@NoArgsConstructor
@Data
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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


    public Token(String phone, LocalDate endDate, String token, Integer type) {
        this.phone = phone;
        this.endDate = endDate;
        this.token = token;
        this.type = type;
    }
}
