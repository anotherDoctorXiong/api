package club.doctorxiong.api.common.page;

import club.doctorxiong.api.common.dto.CacheDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能：分页参数
 *
 * @author zhongshenghua
 * @date 2018/7/4 16:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page extends CacheDTO implements Serializable {

    /**
     * 页码
     */
    private int pageIndex;
    /**
     * 页大小
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private int totalRecord;

    public int getPageSize() {
        if (pageSize <= 0) {
            pageSize = 20;
        }
        return pageSize;
    }

    /**
     * 返回页码，当页码为空或者小于等于0时，默认返回1
     */
    public int getPageNum() {
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        return pageIndex;
    }

    /**
     * 功能：获得总页数
     * 详细：
     * @return
     */
    public int getAllPages() {
        return (totalRecord + pageSize - 1) / getPageSize();
    }

}
