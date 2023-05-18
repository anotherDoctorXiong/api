package club.doctorxiong.api.component;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static club.doctorxiong.api.common.LocalDateTimeFormatter.*;


/**
 * @Author: 熊鑫
 * @Date: 2019/6/20 17
 * @Description: 处理redis缓存时间和数据库有效时间
 */
@Component
@Slf4j
public class ExpireComponent {


    private final String OPEN_AM = "open_am";
    private final String CLOSE_AM = "close_am";
    private final String OPEN_PM = "open_pm";
    private final String CLOSE_PM = "close_pm";
    private final String DAY_END = "day_end";
    private final String WEEKDAY = "weekday";


    public Long getTimestampOfOpenAM() {
        return timestampCache.get(OPEN_AM);
    }

    public Long getTimestampOfOpenPM() {
        return timestampCache.get(OPEN_PM);
    }

    public Long getTimestampOfCloseAM() {
        return timestampCache.get(CLOSE_AM);
    }

    public Long getTimestampOfClosePM() {
        return timestampCache.get(CLOSE_PM); }

    public Long getTimestampOfDayEnd() {
        return timestampCache.get(DAY_END); }


    /**
     * 对应时间戳的每日缓存
     */
    LoadingCache<String, Long> timestampCache = Caffeine.newBuilder()
            .expireAfterWrite(getSecondsUntilMidnight(), TimeUnit.SECONDS)
            .maximumSize(100)
            .build(key -> getTimestamp(key));

    private static long getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        if(now.getHour() == 0){
            return 60 * 60;
        }
        LocalDateTime midnight = now.toLocalDate().atTime(LocalTime.MAX);
        Duration duration = Duration.between(now, midnight);
        return duration.getSeconds();
    }

    private Long getTimestamp(String key) {
        Long timestamp = System.currentTimeMillis() / 1000;
        switch (key) {
            case OPEN_AM:
                timestamp = LocalDateTime.now().withHour(9).withMinute(30).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.ofHours(8));
                break;
            case CLOSE_AM:
                timestamp = LocalDateTime.now().withHour(11).withMinute(30).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.ofHours(8));
                break;
            case OPEN_PM:
                timestamp = LocalDateTime.now().withHour(13).withMinute(0).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.ofHours(8));
                break;
            case CLOSE_PM:
                timestamp = LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.ofHours(8));
                break;
            case DAY_END:
                timestamp = LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.ofHours(8));
                break;
            default:
                log.error(String.format("getTimestamp error key{%s}", key));
                break;
        }
        return timestamp;
    }

    public static void main(String[] args) {
        ExpireComponent expireComponent = new ExpireComponent();
        System.out.println(LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.ofHours(8)));
        System.out.println(System.currentTimeMillis()/1000);
        System.out.println(expireComponent.getTimestampOfClosePM());
    }
}
