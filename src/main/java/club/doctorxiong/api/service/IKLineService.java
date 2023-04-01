package club.doctorxiong.api.service;

import club.doctorxiong.api.entity.KLine;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-04
 */
public interface IKLineService extends IService<KLine> {

    /**
     * 关键字查找
     * @author xiongxin
     * @param key key
     * @return club.doctorxiong.miniprogress.entity.KLine
     */
    KLine getByKey(String key);

}
