package club.doctorxiong.api.service;



import club.doctorxiong.api.entity.Holiday;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 节假日表 服务类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-06
 */
public interface IHolidayService extends IService<Holiday> {

    /**
     * 是否为节假日
     * @author xiongxin
     * @param localDate
     * @return boolean
     */
    boolean isHoliday(LocalDate localDate);

    /**
     * 是否为节假日
     * @author xiongxin
     * @param localDateTime
     * @return boolean
     */
    boolean isHoliday(LocalDateTime localDateTime);

    /**
     * 从当天开始算，获取下一个工作日
     * @author xiongxin
     * @return java.time.LocalDate
     */
    LocalDate getNextWorkDay();

    /**
     * 获取下一个工作日
     * @author xiongxin
     * @param localDate
     * @return java.time.LocalDate
     */
    LocalDate getNextWorkDay(LocalDate localDate);

    /**
     * 获取下一个工作日
     * @author xiongxin
     * @param localDateTime
     * @return java.time.LocalDate
     */
    LocalDate getNextWorkDay(LocalDateTime localDateTime);

}
