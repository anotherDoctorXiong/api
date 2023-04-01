package club.doctorxiong.api.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能：分页请求参数
 *
 * @author zhongshenghua
 * @date 2018/6/12 14:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -5355454567696138248L;

    /**
     * 页码
     */
    private int  pageIndex = 1;

    /**
     * 页大小
     */
    private int pageSize = 20;
    
    /**
     * 静态构造方法，方便构建
     * @param  pageIndex 页码
     * @param pageSize 页大小
     * @return 分页请求参数对象
     */
    public static PageRequest newPageRequest(int  pageIndex, int pageSize){
        return new PageRequest( pageIndex, pageSize);
    }

    /**
     * 分页开始下标
     */
    public int getStartNo() {
        if( pageIndex <= 0 || pageSize <= 0){
            throw new IllegalArgumentException("The parameters are not valid.");
        }
        return (this. pageIndex - 1) * this.pageSize;
    }

}
