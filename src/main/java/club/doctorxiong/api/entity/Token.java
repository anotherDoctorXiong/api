package club.doctorxiong.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String phone;

    /**
     * 失效日期
     */
    private LocalDateTime endDate;

    /**
     * token
     */
    private String token;

    /**
     * 1，2，3
     */
    private Integer type;


    public Token(String phone, LocalDateTime endDate, String token, Integer type) {
        this.phone = phone;
        this.endDate = endDate;
        this.token = token;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Token{" +
            "id=" + id +
            ", phone=" + phone +
            ", endDate=" + endDate +
            ", token=" + token +
            ", type=" + type +
        "}";
    }
}
