package club.doctorxiong.api.service.impl;

import club.doctorxiong.api.entity.VisitStatus;
import club.doctorxiong.api.mapper.VisitStatusMapper;
import club.doctorxiong.api.service.IVisitStatusService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-02
 */
@Service
public class VisitStatusServiceImpl extends ServiceImpl<VisitStatusMapper, VisitStatus> implements IVisitStatusService {
    @Override
    public List<VisitStatus> getListOrderByDate() {
        LambdaQueryWrapper<VisitStatus> queryWrapper = new QueryWrapper().lambda();
        queryWrapper.orderByAsc(true, VisitStatus::getTime);
        return baseMapper.selectList(queryWrapper);
    }

}
