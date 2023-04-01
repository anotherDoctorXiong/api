package club.doctorxiong.api.service;



import club.doctorxiong.api.common.dto.EmailInfoDTO;
import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.common.dto.StockDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;


/**
 * @Auther: 熊鑫
 * @Date: 2019/11/14 20
 * @Description:
 */
@Service
@Slf4j
public class PositionService  {


    @Autowired
    private StockService stockService;

    private static BigDecimal halfPosition = new BigDecimal("50");




    /**
     * @param list
     * @param currentPrice
     * @param isMax
     * @name: cleanList
     * @auther:
     * @return: java.util.LinkedList<java.lang.String>
     * @date: 2020/2/28 11:20
     * @description: 双端链表清理, 返回的的链表表头为最大值或最小值
     */
    public LinkedList<String> cleanList(LinkedList<String> list, String currentPrice, boolean isMax) {
        while (list.size() > 0 && (isMax ? list.getFirst().compareTo(currentPrice) < 0 : list.getFirst().compareTo(currentPrice) > 0)) {
            list.removeFirst();
        }
        list.addLast(currentPrice);
        return list;
    }
    /**
     * @name: currentPosition
     * @auther:
     * @param max
     * @param min
     * @param average
     * @param currentPrice
     * @return: int
     * @date: 2020/2/29 11:40
     * @description: 计算目标仓位,就是普通的加减乘除,只是为了不丢失精度而略显繁琐
     */
    public int currentPosition(BigDecimal max,BigDecimal min,BigDecimal average,BigDecimal currentPrice){

        BigDecimal currentPosition;
        if (currentPrice.compareTo(average) > 0) {
            currentPosition = currentPrice.compareTo(max)>=0?BigDecimal.ZERO:halfPosition.subtract(currentPrice.subtract(average).multiply(halfPosition).divide(max.subtract(average), 0, RoundingMode.HALF_UP));
        } else if(currentPrice.compareTo(average) < 0){
            currentPosition = currentPrice.compareTo(min)<=0?new BigDecimal(100):halfPosition.add(average.subtract(currentPrice).multiply(halfPosition).divide(average.subtract(min), 0, RoundingMode.HALF_UP));
        }else {
            currentPosition = halfPosition;
        }
        return  currentPosition.intValue();
    }



    /**
     * @param fundDTO
     * @name: getTodayPosition
     * @auther: 熊鑫
     * @return: int
     * @date: 2020/2/27 14:21
     * @description: 计算当前基金的推送仓位信息,由于计算任务需要拆分,使用Future
     */

    public EmailInfoDTO getFundEmailInfo(FundDTO fundDTO) {
        EmailInfoDTO result = new EmailInfoDTO();
        BigDecimal currentPrice;

        //默认使用最后一日的单位净值计算
        currentPrice = fundDTO.getExpectWorth();
        result.setCode(fundDTO.getCode());
        result.setName(fundDTO.getName());
        result.setFund((byte)1);
        result.setDate(fundDTO.getExpectWorthDate());

        result.setCurrentPrice(currentPrice.toString());
        int index = fundDTO.getNetWorthData().length - 1;
        int startIndex = index - 245 > 0 ? index - 245 : 0;
        //计算近一年的平均值,最大值,最小值
        BigDecimal max = currentPrice;
        BigDecimal min = currentPrice;
        BigDecimal total = BigDecimal.ZERO;
        for (int i = startIndex + 1; i <= index; i++) {
            BigDecimal indexPrice = new BigDecimal(fundDTO.getNetWorthData()[i][1]);
            if (indexPrice.compareTo(max) == 1) {
                max = indexPrice;
            }
            if (indexPrice.compareTo(min) == -1) {
                min = indexPrice;
            }
            total = total.add(indexPrice);
        }
        BigDecimal average = total.divide(new BigDecimal(index-startIndex+1), 4, RoundingMode.HALF_UP);
        //大于平均和小于平均各一半仓位
        result.setPosition(currentPosition(max,min,average,currentPrice));
        result.setMax(max.toString());
        result.setMin(min.toString());
        return result;
    }

    /**
     * @param stockDTO
     * @name: getTodayPosition
     * @auther: 熊鑫
     * @return: int
     * @date: 2020/2/27 14:21
     * @description: 计算当前股票的推送仓位信息,由于计算任务需要拆分,使用Future
     */

    public EmailInfoDTO getStockEmailInfo(StockDTO stockDTO) {
        EmailInfoDTO result = new EmailInfoDTO();
        BigDecimal currentPrice=new BigDecimal(stockDTO.getPrice());

        //默认使用最后一日的单位净值计算
        result.setCode(stockDTO.getCode());
        result.setName(stockDTO.getName());
        //todo result.setDate(stock.getDate());
        result.setFund((byte)0);
        result.setCurrentPrice(currentPrice.toString());
        //使用前复权数据进行计算
        String[][] arr=stockService.getDayData(stockDTO.getCode(),null,null,1);
        int index = arr.length - 1;
        int startIndex = index - 245 > 0 ? index - 245 : 0;
        //计算近一年的平均值,最大值,最小值
        BigDecimal max = currentPrice;
        BigDecimal min = currentPrice;
        BigDecimal total = BigDecimal.ZERO;
        for (int i = startIndex + 1; i <= index; i++) {
            BigDecimal indexClose = new BigDecimal(arr[i][2]);
            BigDecimal indexMax = new BigDecimal(arr[i][3]);
            BigDecimal indexMin = new BigDecimal(arr[i][4]);
            if (indexMax.compareTo(max) == 1) {
                max = indexMax;
            }
            if (indexMin.compareTo(min) == -1) {
                min = indexMin;
            }
            total = total.add(indexClose);
        }
        BigDecimal average = total.divide(new BigDecimal(index-startIndex+1), 4, RoundingMode.HALF_UP);
        //大于平均和小于平均各一半仓位
        result.setPosition(currentPosition(max,min,average,currentPrice));
        result.setMax(max.toString());
        result.setMin(min.toString());
        return result;
    }
}
