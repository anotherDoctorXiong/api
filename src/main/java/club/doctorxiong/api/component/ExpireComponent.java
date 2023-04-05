package club.doctorxiong.api.component;


import club.doctorxiong.api.service.IHolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static club.doctorxiong.api.common.LocalDateTimeFormatter.*;


/**
 * @Author: 熊鑫
 * @Date: 2019/6/20 17
 * @Description: 处理redis缓存时间和数据库有效时间
 */
@Component
@Slf4j
public class ExpireComponent {


    @Autowired
    private IHolidayService holidayService;



    /**
     * 返回对应的缓存时间 毫秒 开盘时间内，每分钟刷新一次
     * @author xiongxin
     * @param date 数据锚点时间
     * @return long
     */
    public long getMinuteDataExpireTime(LocalDateTime date){
        //是今天的数据
        LocalDateTime now = getTimeNow();
        if(date.toLocalDate().compareTo(getToday()) == 0){
            if(date.compareTo(getTimeOfCloseAM(now)) < 0){
                return MINUTE_MILLISECOND;
            }else if(date.compareTo(getTimeOfOpenPM(now)) < 0){
                // date可能存在更新不及时的情况
                long timeRange = ChronoUnit.MILLIS.between(now, getTimeOfOpenPM(date));
                return timeRange > 0?timeRange:MINUTE_MILLISECOND;
            }else if(date.compareTo(getTimeOfClosePM(now)) < 0){
                return MINUTE_MILLISECOND;
            } else {
                // 缓存至下个工作日的开盘
                return ChronoUnit.MILLIS.between(now,getTimeOfOpenAM(holidayService.getNextWorkDay()));
            }
        }else {
            // 给一点冗余时间
            if(now.compareTo(getTimeOfOpenAM(now)) > 0 && now.getMinute() - getTimeOfOpenAM(now).getMinute() < 30 && now.getHour() == getTimeOfOpenAM(now).getHour()){
                return ChronoUnit.MILLIS.between(now, getTimeOfOpenAM(now));
            }else {
                // 开盘了还没更新，缓存至下个工作日
                return ChronoUnit.MILLIS.between(now,getTimeOfOpenAM(holidayService.getNextWorkDay()));
            }
        }
    }

    /**
     * 每日刷新的数据过期时间
     * @author xiongxin
     * @param date 数据锚点时间
     * @return java.lang.Long
     */
    public Long getDailyDataExpireTime(LocalDate date){
        if(date == null){
            return TEN_MINUTE_MILLISECOND;
        }
        LocalDateTime now = getTimeNow();
        if(date.compareTo(getToday()) != 0 && !holidayService.isHoliday(now)){
            // 非今日数据，且今天为工作日
            LocalDateTime closeTime = getTimeOfClosePM(now);
            return now.compareTo(closeTime) < 0 ? ChronoUnit.MILLIS.between(now,closeTime) : TWENTY_MINUTE_MILLISECOND;
        }else {
            return ChronoUnit.MILLIS.between(now,getTimeOfClosePM(holidayService.getNextWorkDay())) + getThreeHoursRandomMillsSecond();
        }
    }

    /**
     * 获取三小时内的随机毫秒数,防止redis同时过期
     * @param
     * @return long
     */
    private static Long getThreeHoursRandomMillsSecond(){
        Long max=3600L,min=1000L;
        long randomNum = System.currentTimeMillis();
        return (randomNum%(max-min)+min)*SECOND_MILLISECOND;
    }

    /**
     * 每日刷新数据到期时间
     * @author xiongxin
     * @param date
     * @return java.time.LocalDateTime
     */
    public LocalDateTime getDailyDataValidTime(LocalDate date){
        LocalDate today = getToday();
        if(date.compareTo(today) != 0 && !holidayService.isHoliday(today)){
            // 非今日数据，且今天为工作日，返回今天收盘时间
            LocalDateTime closeTime = getTimeOfClosePM(today);
            return getTimeNow().compareTo(closeTime) < 0 ? closeTime : closeTime.plusMinutes(20);
        }else {
            // 返回下个工作日收盘时间
            return getTimeOfClosePM(holidayService.getNextWorkDay());
        }
    }
    /**
     * 开盘时间内的对应的过期时间
     * @author xiongxin
     * @param interval 缓存时间（分钟）
     * @return long
     */
    public long getDataExpireTime(int interval){
        LocalDateTime now = getTimeNow();
        if(!holidayService.isHoliday(getToday())){
            if(now.compareTo(getTimeOfOpenAM(now)) < 0){
                return ChronoUnit.MILLIS.between(now, getTimeOfOpenAM(now));
            }else if(now.compareTo(getTimeOfClosePM(now)) < 0){
                return interval * MINUTE_MILLISECOND;
            }
        }
        return ChronoUnit.MILLIS.between(now,getTimeOfOpenAM(holidayService.getNextWorkDay()));
    }
}
