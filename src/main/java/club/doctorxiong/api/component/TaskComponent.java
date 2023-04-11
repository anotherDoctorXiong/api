package club.doctorxiong.api.component;


import club.doctorxiong.api.common.dto.StockDTO;
import club.doctorxiong.api.service.*;
import club.doctorxiong.api.common.RedisKeyConstants;
import club.doctorxiong.api.entity.DailyIndexData;
import club.doctorxiong.api.entity.Email;
import club.doctorxiong.api.entity.VisitStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 定时任务
 * @author xiongxin
 */
@Slf4j
@Component
public class TaskComponent {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @Autowired
    private IVisitStatusService visitStatusService;
    @Autowired
    private IDailyIndexDataService dailyIndexDataService;

    @Autowired
    private IEmailService emailService;
    @Autowired
    private StockService stockService;
    @Autowired
    private PushService pushService;







  /*  *//**
     * @name: cacheBoard
     * @auther: 熊鑫
     * @date: 2020/5/9 15:16
     * @description: 处理当天的网站访问情况
     *//*
    @Scheduled(cron = "0 5/* * * * ?")
    public void finishTodayVisitTimes(){
        ValueOperations valueOperations=stringRedisTemplate.opsForValue();
        Long visitTime=0L;
        if(stringRedisTemplate.hasKey(RedisKeyConstants.TOTAL_VISIT_TIMES)){
            visitTime =Long.valueOf(valueOperations.get(RedisKeyConstants.TOTAL_VISIT_TIMES).toString());
        }
        visitStatusService.getBaseMapper().insert(new VisitStatus(LocalDate.now(),visitTime));
        stringRedisTemplate.delete(RedisKeyConstants.TOTAL_VISIT_TIMES);
    }*/

    /**
     * @name: startTrade
     * @date: 2019/10/23 16:36
     * @description: 每日资讯推送
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void startPushDailyContent() {
        LocalDateTime now = LocalDateTime.now();
        List<Email> emailList = emailService.list().stream().filter(v -> v.getEndDate().compareTo(now) > 0).collect(Collectors.toList());
        //pushService.pushJin10Content(emailList);
        StockDTO stockDTO = stockService.getStock("sh000001");
        /*String[][] arr = stockService.getDayData("sh000001",LocalDate.of(2022,8,1),null,0);

        for (int i = 0; i < arr.length; i++) {
            dailyIndexDataService.updateDailyData(DailyIndexData.builder().date(LocalDate.parse(arr[i][0])).indexData(new BigDecimal(arr[i][2])).build());
        }*/
        DailyIndexData dailyIndexData = new DailyIndexData();
        dailyIndexData.setDate(stockDTO.getDate().toLocalDate());
        dailyIndexData.setIndexData(new BigDecimal(stockDTO.getPrice()));
        dailyIndexData.setIndexGrowth(new BigDecimal(stockDTO.getChangePercent()));
        dailyIndexDataService.updateDailyData(dailyIndexData);
        pushService.getMainFunds();
        pushService.getNorthFunds();
    }
}
