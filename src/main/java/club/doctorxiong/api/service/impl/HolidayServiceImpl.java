package club.doctorxiong.api.service.impl;

import club.doctorxiong.api.common.LocalDateTimeFormatter;
import club.doctorxiong.api.entity.Holiday;
import club.doctorxiong.api.mapper.HolidayMapper;
import club.doctorxiong.api.service.IHolidayService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static club.doctorxiong.api.common.RedisKeyConstants.getHolidayCacheKey;

/**
 * <p>
 * 节假日表 服务实现类
 * </p>
 *
 * @author xiongxin
 * @since 2021-12-06
 */
@Service
public class HolidayServiceImpl extends ServiceImpl<HolidayMapper, Holiday> implements IHolidayService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean isHoliday(LocalDate localDate) {
        String redisKey = getHolidayCacheKey(localDate);
        if (redisTemplate.hasKey(redisKey)) {
            return (Boolean) redisTemplate.opsForValue().get(redisKey);
        } else {
            LambdaQueryWrapper<Holiday> queryWrapper = new QueryWrapper().lambda();
            queryWrapper.eq(Holiday::getIsDeleted, 1);
            List<Holiday> holidayList = baseMapper.selectList(queryWrapper);
            Boolean dateIsHoliday = holidayList.stream().anyMatch(holiday -> holiday.getCivicHoliday().compareTo(localDate) == 0) || LocalDateTimeFormatter.isWeekDay(localDate);
            redisTemplate.opsForValue().set(redisKey, dateIsHoliday, LocalDateTimeFormatter.DAY_MILLISECOND, TimeUnit.MILLISECONDS);
            return dateIsHoliday;
        }
    }

    @Override
    public boolean isHoliday(LocalDateTime localDateTime) {
        return isHoliday(localDateTime.toLocalDate());
    }

    @Override
    public LocalDate getNextWorkDay() {
        LocalDate indexDate = LocalDateTimeFormatter.getToday().plusDays(1);
        while (isHoliday(indexDate)){
            indexDate = indexDate.plusDays(1);
        }
        return indexDate;
    }

    @Override
    public LocalDate getNextWorkDay(LocalDate localDate) {
        LocalDate indexDate = localDate.plusDays(1);
        while (isHoliday(indexDate)){
            indexDate = indexDate.plusDays(1);
        }
        return indexDate;
    }

    @Override
    public LocalDate getNextWorkDay(LocalDateTime localDateTime) {
        LocalDate indexDate = localDateTime.toLocalDate().plusDays(1);
        while (isHoliday(indexDate)){
            indexDate = indexDate.plusDays(1);
        }
        return indexDate;
    }

}
