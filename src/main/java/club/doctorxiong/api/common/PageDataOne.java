package club.doctorxiong.api.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: 熊鑫
 * @Date: 2019/6/27 23
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDataOne<T> implements Serializable {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer allPages;
    private T rank;
}
