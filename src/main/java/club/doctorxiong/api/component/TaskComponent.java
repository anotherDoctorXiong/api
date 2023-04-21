package club.doctorxiong.api.component;


import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 定时任务
 * @author xiongxin
 */
@Slf4j
@Component
public class TaskComponent {

    @Autowired
    private FundComponent fundComponent;


    @Autowired
    private StockComponent stockComponent;



    /**
     * @name: cacheBoard
     * @auther: 熊鑫
     * @date: 2020/5/9 15:16
     * @description: 处理当天的网站访问情况
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void CacheStats(){
        CacheStats fundStats = fundComponent.fundCache.stats();
        log.info("fundStats命中次数:{}-缓存命中率:{}-未命中次数:{}-未命中率:{}",fundStats.hitCount(),fundStats.hitRate(),fundStats.missCount(),fundStats.missRate());


        CacheStats klineStats = stockComponent.stockCache.stats();
        log.info("klineStats命中次数:{}-缓存命中率:{}-未命中次数:{}-未命中率:{}",klineStats.hitCount(),klineStats.hitRate(),klineStats.missCount(),klineStats.missRate());

    }


}
