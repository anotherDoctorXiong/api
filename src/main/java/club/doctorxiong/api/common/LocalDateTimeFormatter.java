package club.doctorxiong.api.common;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 日期公共常量和方法
 *
 * @author xiongxin
 */
public class LocalDateTimeFormatter {

    /**
     * 毫秒数
     */
    public static Long SECOND_MILLISECOND = 1000L;

    /**
     * 每分钟毫秒数
     */
    public static Long MINUTE_MILLISECOND = 60 * 1000L;

    /**
     * 十分钟毫秒数
     */
    public static Long TEN_MINUTE_MILLISECOND = 10 * 60 * 1000L;

    /**
     * 二十分钟毫秒数
     */
    public static Long TWENTY_MINUTE_MILLISECOND = 20 * 60 * 1000L;

    /**
     * 每小时毫秒数
     */
    public static Long HOUR_MILLISECOND = 60 * 60 * 1000L;

    /**
     * 每小时毫秒数
     */
    public static Long THREE_HOUR_MILLISECOND = 3 * 60 * 60 * 1000L;

    /**
     * 十小时毫秒数
     */
    public static Long TEN_HOUR_MILLISECOND = 60 * 60 * 1000L;

    /**
     * 每日毫秒数
     */
    public static Long DAY_MILLISECOND = 24 * 60 * 60 * 1000L;

    /**
     * 通用格式
     */
    public static DateTimeFormatter DATE_FORMAT_COMMON = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 通用格式
     */
    public static DateTimeFormatter TIME_FORMAT_COMMON = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 没有分隔符的格式
     */
    public static DateTimeFormatter FORMAT_ONE = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 获取当前时间
     */
    public static LocalDateTime getTimeNow() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     */
    public static LocalDate getToday() {
        return LocalDate.now();
    }

    /**
     * 判断双休
     */
    public static boolean isWeekDay(LocalDate localDate) {
        DayOfWeek week = localDate.getDayOfWeek();
        return week == DayOfWeek.SATURDAY || week == DayOfWeek.SUNDAY;
    }

    /**
     * 获取当天剩余时间毫秒
     */
    public static long getTodayLastMilliseconds() {
        LocalDateTime now = getTimeNow();
        LocalDateTime midnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return ChronoUnit.MILLIS.between(now, midnight);
    }

    /**
     * 获取开盘时间
     */
    public static LocalDateTime getTimeOfOpenAM(LocalDateTime localDateTime) {
        return localDateTime.withHour(9).withMinute(30).withSecond(0).withNano(0);
    }



    /**
     * 获取开盘时间
     */
    public static LocalDateTime getTimeOfOpenAM(LocalDate localDate) {
        return localDate.atStartOfDay().withHour(9).withMinute(30).withSecond(0).withNano(0);
    }

    /**
     * 获取上午收盘时间
     */
    public static LocalDateTime getTimeOfCloseAM(LocalDateTime localDateTime) {
        return localDateTime.withHour(11).withMinute(30).withSecond(0).withNano(0);
    }

    /**
     * 获取上午收盘时间
     */
    public static LocalDateTime getTimeOfCloseAM(LocalDate localDate) {
        return localDate.atStartOfDay().withHour(11).withMinute(30).withSecond(0).withNano(0);
    }

    /**
     * 获取下午开盘
     */
    public static LocalDateTime getTimeOfOpenPM(LocalDateTime localDateTime) {
        return localDateTime.withHour(13).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取下午开盘
     */
    public static LocalDateTime getTimeOfOpenPM(LocalDate localDate) {
        return localDate.atStartOfDay().withHour(13).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取下午收盘
     */
    public static LocalDateTime getTimeOfClosePM(LocalDateTime localDateTime) {
        return localDateTime.withHour(15).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取下午收盘
     */
    public static LocalDateTime getTimeOfClosePM(LocalDate localDate) {
        return localDate.atStartOfDay().withHour(15).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 格式化输出过期时间
     */
    public static String printValidTime(long expireTime) {
        return Instant.ofEpochMilli(System.currentTimeMillis() + expireTime).atZone(ZoneOffset.ofHours(8)).toLocalDateTime().toString();
    }

    /**
     * 时间戳转换
     */
    public static LocalDate getLocalDateByTimestamp(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
    }

    /**
     * 时间戳转换
     */
    public static LocalDateTime getLocalDateTimeByTimestamp(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    /**
     * 字符串格式化时间
     */
    public static LocalDateTime getLocalDateTimeWithStr(String timeStr,DateTimeFormatter formatter){
        return LocalDateTime.parse(timeStr,formatter);
    }

}
