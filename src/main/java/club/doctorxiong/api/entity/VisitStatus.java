package club.doctorxiong.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * <p>
 * 
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-02
 */
@TableName("visit_status")
@Data
@AllArgsConstructor
public class VisitStatus implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private static final long serialVersionUID = 1L;

    private LocalDate time;

    private Long visitTimes;

    public VisitStatus(LocalDate time, Long visitTimes) {
        this.time = time;
        this.visitTimes = visitTimes;
    }


    @Override
    public String toString() {
        return "VisitStatus{" +
                "time=" + time +
                ", visitTimes=" + visitTimes +
                "}";
    }
}
