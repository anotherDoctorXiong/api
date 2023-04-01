package club.doctorxiong.api.common.page;

import club.doctorxiong.api.uitls.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 功能：分页数据传输对象
 *
 * @author zhongshenghua
 * @date 2018/6/12 14:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<E> extends Page
        implements Serializable{

    private static final long serialVersionUID = 8148912660169002849L;

    /**
     * 结果集
     */
    private List<E> rank;


    
    /**
     * 初始化分页信息
     * @param pageNum 页码
     * @param pageSize 页大小
     */
    public PageData(int pageNum, int pageSize) {
        this.setPageIndex(pageNum);
        this.setPageSize(pageSize);
        this.setTotalRecord(0);
        this.rank = Collections.emptyList();
    }

    /**
     * 初始化分页信息
     * @param pageRequest 分页请求参数
     */
    public PageData(PageRequest pageRequest) {
        this.setPageIndex(pageRequest.getPageIndex());
        this.setPageSize(pageRequest.getPageSize());
        this.setTotalRecord(0);
        this.rank = Collections.emptyList();
    }
    
    /**
     * @param list 数据列表
     * @param totalRecord 总记录数
     * @param pageRequest 分页请求对象
     */
    public PageData(List<E> list, int totalRecord, PageRequest pageRequest){
        this.setPageIndex(pageRequest.getPageIndex());
        this.setPageSize(pageRequest.getPageSize());
        this.setTotalRecord(totalRecord);
        this.rank = list;
    }


    /**
     * 创建一个空的分页数据对象
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static  PageData newEmptyPage(){
         PageData pageData = new  PageData();
        pageData.setRank(Collections.emptyList());
        return pageData;
    }


    /**
     * 转换分页中的实体类
     * @param resultList 需要转换的集合
     * @param pageData 返回的分页对象
     * @return
     */
    public static <R>  PageData<R> convertPageEntity(List<R> resultList,  PageData pageData) {
         PageData<R> resultPage = new  PageData<>();
        resultPage.setRank(resultList);
        resultPage.setPageIndex(pageData.getPageNum());
        resultPage.setPageSize(pageData.getPageSize());
        resultPage.setTotalRecord(pageData.getTotalRecord());
        return resultPage;
    }

    /**
     * 转换分页参数中的实体类
     * @param pageData 需要转换的分页对象
     * @param destinationClass 分页对象中目标实体类
     * @param <S> source：需要转换的分页对象
     * @param <D> destinationClass：分页对象中目标实体类Class
     * @return 转换后的结果
     */
    public static  <S, D>  PageData<D> convertPageEntity( PageData<S> pageData, Class<D> destinationClass) {
        //先转换实体对象
        List<D> dList = BeanUtil.mapList(pageData.getRank(), destinationClass);
         PageData<D> resultPage = new  PageData<>();
        resultPage.setRank(dList);
        resultPage.setPageIndex(pageData.getPageNum());
        resultPage.setPageSize(pageData.getPageSize());
        resultPage.setTotalRecord(pageData.getTotalRecord());
        return resultPage;
    }
}
