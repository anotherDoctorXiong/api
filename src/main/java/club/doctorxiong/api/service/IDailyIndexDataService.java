package club.doctorxiong.api.service;


import club.doctorxiong.api.entity.DailyIndexData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiongxin
 * @since 2022-06-25
 */
public interface IDailyIndexDataService extends IService<DailyIndexData> {

    public void updateDailyData(DailyIndexData dailyIndexData);

}
