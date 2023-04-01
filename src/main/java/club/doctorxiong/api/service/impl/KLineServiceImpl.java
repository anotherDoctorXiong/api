package club.doctorxiong.api.service.impl;

import club.doctorxiong.api.entity.KLine;
import club.doctorxiong.api.mapper.KLineMapper;
import club.doctorxiong.api.service.IKLineService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-04
 */
@Service
public class KLineServiceImpl extends ServiceImpl<KLineMapper, KLine> implements IKLineService {

    @Override
    public KLine getByKey(String key) {
        LambdaQueryWrapper<KLine> queryWrapper = new QueryWrapper().lambda();
        queryWrapper.eq(KLine::getCode,key);
        return baseMapper.selectOne(queryWrapper);
    }
}
