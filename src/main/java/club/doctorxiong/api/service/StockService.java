package club.doctorxiong.api.service;

import club.doctorxiong.api.common.InnerException;
import club.doctorxiong.api.common.dto.ConvertBondDTO;
import club.doctorxiong.api.common.dto.IndustryDetailDTO;
import club.doctorxiong.api.common.dto.KLineDTO;
import club.doctorxiong.api.common.dto.StockDTO;
import club.doctorxiong.api.common.page.PageRequest;
import club.doctorxiong.api.common.request.KLineRequest;
import club.doctorxiong.api.common.request.StockRankRequest;
import club.doctorxiong.api.component.StockComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.common.page.PageData;

import club.doctorxiong.api.uitls.StringUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



/**
 * @Auther: 熊鑫
 * @Date: 2019/6/24 21
 * @Description: 不同K线的编码格式, 股票代码+d(日),w(周),m(月)+0(不复权),1(前复权),2(后复权)
 */
@Service
@Slf4j
public class StockService  {
    @Autowired
    private StockComponent stockComponent;


    /**
     * @name: getIndustryRank
     * @auther: 熊鑫
     * @return: java.util.List<club.doctorxiong.stub.dto.stock.IndustryDetail>
     * @date: 2020/7/16 22:49
     * @description: 获取板块排行
     */
    public List<IndustryDetailDTO> getIndustryRank() {
        return stockComponent.stockIndustryCache.get("");
    }

    public PageData<StockDTO> getStockRank(StockRankRequest request) {
        PageData<StockDTO> stockRank = stockComponent.stackRankCache.get(request);
        stockRank.setPageIndex(request.getPageIndex());
        stockRank.setPageSize(request.getPageSize());
        Integer allCount = stockComponent.stockCountCache.get(request.getNode());
        stockRank.setTotalRecord(allCount);
        List<StockDTO> stockDTOList = new ArrayList<>();
        stockRank.getRank().forEach(stockDTO -> {
            if(stockDTO.getCode() != null){
                stockDTOList.add(getStock(stockDTO.getCode()));
            }
        });
        stockRank.setRank(stockDTOList);
        return stockRank;
    }



    /**
     * @param type 区分复权数据
     * @name: getKLine
     * @auther: 熊鑫
     * @return: void
     * @date: 2020/9/2 16:10
     * @description:
     */
    public String[][] getDayData(String stockCode, LocalDate startDate, LocalDate endDate, int type) {

        if (type < 0 || type > 2) {
            InnerException.exInvalidParam("type(0-不复权,1-前复权,2-后复权)");
        }
        //key规则,股票代码+d(日),w(周),m(月)+0(不复权),1(前复权),2(后复权)
        stockCode = StringUtil.getTotalStockCode(stockCode);
        //K线数据为两部分组成,历史K线和当日K线,当日K线为实时K线数据一般由当日分时数据
        KLineDTO kLineDTO = stockComponent.KLineCache.get(new KLineRequest(type,stockCode));
        String[][] kLineData = kLineDTO.getArrData();
        if(kLineData == null || kLineData.length == 0){
            return new String[0][];
        }
        StockDTO stockDTO = getStock(stockCode);
        LocalDate stockDate = stockDTO.getDate().toLocalDate();
        if(kLineData.length == 0){
            return kLineData;
        }
        LocalDate kLineDate = LocalDate.parse(kLineData[kLineData.length-1][0]);
        // 日期;开盘;收盘;最高;最低;成交量(手)
        if(kLineDate.compareTo(stockDate) == 0)  {
            kLineData[kLineData.length -1] =  new String[]{stockDate.toString(),stockDTO.getOpen(),stockDTO.getPrice(),stockDTO.getHigh(),stockDTO.getLow(),stockDTO.getVolume()};
        }
        // 考虑开始时间和结束时间为null的情况
        int start = startDate == null ? 0 : StringUtil.getIndexOrLeft(kLineData, startDate.toString());
        int end = endDate == null ? (kLineData.length - 1) : StringUtil.getIndexOrRight(kLineData, endDate.toString());
        if (start <= end && (end - start) < kLineData.length) {
            return Arrays.copyOfRange(kLineData, start, end + 1);
        }else {
            return new String[][]{};
        }
    }


    /**
     * @param week 周K还是月K
     * @name: getMonthData
     * @auther: 熊鑫
     * @return: java.lang.String[][]
     * @date: 2020/9/6 22:53
     * @description:
     */
    public String[][] getDateRangeData(String stockCode, LocalDate startDate, LocalDate endDate, int type, boolean week) {
        //不同K线的编码格式,股票代码+d(日),w(周),m(月)+0(不复权),1(前复权),2(后复权)
        stockCode = StringUtil.getTotalStockCode(stockCode);
        String[][] kLineData = getDayData(stockCode, null, null, type);
        if(kLineData.length == 0){
            return kLineData;
        }
        Map<Integer, String[]> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Arrays.asList(kLineData).stream().forEach(arr -> {
            setWeekOrMonthData(calendar, arr, map, week);
        });
        kLineData = map.values().stream().sorted(new Comparator<String[]>() {
                                                     @Override
                                                     public int compare(String[] o1, String[] o2) {
                                                         return o1[0].compareTo(o2[0]);
                                                     }
                                                 }
        ).collect(Collectors.toList()).toArray(new String[map.size()][6]);
        //考虑开始时间和结束时间为null的情况
        int start = startDate == null ? 0 : StringUtil.getIndexOrLeft(kLineData, startDate.toString());
        int end = endDate == null ? (kLineData.length - 1) : StringUtil.getIndexOrRight(kLineData, endDate.toString());
        if (start > end) {
            InnerException.exInvalidParam("无效的时间区间");
        }
        return Arrays.copyOfRange(kLineData, start, end + 1);
    }

    /**
     * @param oneDayData
     * @param map
     * @name: setWeekData
     * @auther: 熊鑫
     * @return: java.util.concurrent.Future
     * @date: 2020/9/6 15:30
     * @description: 对计算周K月K
     */

    private void setWeekOrMonthData(Calendar calendar, String[] oneDayData, Map<Integer, String[]> map, boolean week) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oneDayData[0]);
            calendar.setTime(date);

            int key;
            if (week) {
                //对于跨年的一周需要特殊处理,WEEK_OF_YEAR均会返回1,将跨年的一周同步到下一年的第一周里面
                if (calendar.get(Calendar.WEEK_OF_YEAR) == 1 && calendar.get(Calendar.MONTH) > 0) {
                    key = calendar.get(Calendar.WEEK_OF_YEAR) * (calendar.get(Calendar.YEAR) + 1);
                } else {
                    key = calendar.get(Calendar.WEEK_OF_YEAR) * calendar.get(Calendar.YEAR);
                }
            } else {
                key = (calendar.get(Calendar.MONTH) + 1) * calendar.get(Calendar.YEAR);
            }
            if (!map.containsKey(key)) {
                map.put(key, oneDayData);
            } else {
                String[] arr = map.get(key);
                //数组{日期,开盘,收盘,最高,最低,成交量}多个数据合并
                //日期和收盘,显示最后一天,一般是周五
                if (arr[0].compareTo(oneDayData[0]) < 0) {
                    arr[0] = oneDayData[0];
                    arr[2] = oneDayData[2];
                } else {
                    //开盘,显示第一天的,一般是周一
                    arr[1] = oneDayData[1];
                }
                //最高价
                if (arr[3].compareTo(oneDayData[3]) < 0) {
                    arr[3] = oneDayData[3];
                }
                //最低价
                if (arr[4].compareTo(oneDayData[4]) > 0) {
                    arr[4] = oneDayData[4];
                }
                arr[5] = new BigDecimal(oneDayData[5]).add(new BigDecimal(arr[5])).toString();
                map.put(key, arr);
            }
        } catch (ParseException e) {
            return;
        }
    }

    /**
     * @param stockCode
     * @name: getStock
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.stock.Stock
     * @date: 2020/9/6 23:44
     * @description: 最新分时及股票数据, 此方法无需异常管理, 线程池会将异常结果全部丢弃
     */
    public StockDTO getStock(String stockCode) {
        stockCode = StringUtil.getTotalStockCode(stockCode);
        return stockComponent.stockCache.get(stockCode);
    }


    public List<StockDTO> getStockList(String stockCodeStr) {
        String[] arr = stockCodeStr.split(",");
        if (arr.length > 100) {
            arr = Arrays.copyOfRange(arr, 0, 100);
        }
        return Arrays.asList(arr).parallelStream().map(e -> {
            StockDTO one = SerializationUtils.clone(getStock(e));
            one.setMinData(null);
            return one;
        }).filter(StockDTO::validStock).collect(Collectors.toList());
    }




    /**
     * @param
     * @name: getStockAll
     * @auther: 熊鑫
     * @return: java.util.List<java.lang.String [ ]>
     * @date: 2020/8/25 21:47
     * @description: 返回所有股票(包含以退市)
     */
    public List<String[]> getStockAll(String keyWord) {
        List<String[]> allStock = stockComponent.allStockAndIndexCache.get(true);
        if (keyWord == null || keyWord.isEmpty()) {
            return allStock;
        }else if (StringUtil.isDigit(keyWord)) {
            return allStock.stream().filter(arr -> arr[0].contains(keyWord)).collect(Collectors.toList());
        } else {
            return allStock.stream().filter(arr -> arr[1].contains(keyWord)).collect(Collectors.toList());
        }
    }

    /**
     * @param
     * @name: getIndexAll
     * @auther: 熊鑫
     * @return: java.util.List<java.lang.String [ ]>
     * @date: 2020/8/25 22:14
     * @description: 获取所有指数
     */
    public List<String[]> getIndexAll() {
        return stockComponent.allStockAndIndexCache.get(false);
    }

    public List<ConvertBondDTO> getAllConvertBond(){
        return stockComponent.convertBondCache.get("");
    }

    public PageData<ConvertBondDTO> getConvertBondPage(PageRequest request){
        List<ConvertBondDTO> all = getAllConvertBond();
        PageData<ConvertBondDTO> convertBondPage = new PageData();
        convertBondPage.setPageIndex(request.getPageIndex());
        convertBondPage.setPageSize(request.getPageSize());
        convertBondPage.setTotalRecord(getAllConvertBond().size());
        convertBondPage.setRank(all.subList(request.getStartNo(),
                request.getPageIndex() * request.getPageSize() > convertBondPage.getTotalRecord() ? convertBondPage.getTotalRecord() : request.getPageIndex() * request.getPageSize()));
        return convertBondPage;
    }



}
