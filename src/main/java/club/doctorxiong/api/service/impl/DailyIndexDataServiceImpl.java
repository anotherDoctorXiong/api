package club.doctorxiong.api.service.impl;


import club.doctorxiong.api.entity.DailyIndexData;
import club.doctorxiong.api.mapper.DailyIndexDataMapper;
import club.doctorxiong.api.service.IDailyIndexDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiongxin
 * @since 2022-06-25
 */
@Service
public class DailyIndexDataServiceImpl extends ServiceImpl<DailyIndexDataMapper, DailyIndexData> implements IDailyIndexDataService {

    @Override
    public void updateDailyData(DailyIndexData dailyIndexData) {
        System.out.println(dailyIndexData.toString());
        if(baseMapper.selectById(dailyIndexData.getDate())!=null){
            baseMapper.updateById(dailyIndexData);
        }else {
            baseMapper.insert(dailyIndexData);
        }
    }
}
