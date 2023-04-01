package club.doctorxiong.api.service;

import club.doctorxiong.api.entity.VisitStatus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-02
 */
public interface IVisitStatusService extends IService<VisitStatus> {

    /**
     * 按时间排序返回
     * @author xiongxin
     * @return java.util.List<club.doctorxiong.miniprogress.entity.VisitStatus>
     */
    public List<VisitStatus> getListOrderByDate();

}
