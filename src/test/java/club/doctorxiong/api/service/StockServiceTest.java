package club.doctorxiong.api.service;
import club.doctorxiong.api.common.request.StockRankRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;

import java.time.LocalDate;

/**
* StockServiceTester.
*
* @Author: DoctorXiong.club
* @CreateDate: 2023-04-09
*/
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Test
    public void testGetIndustryRank() {
        JSONObject.toJSONString(stockService.getStock("000001"));
        JSONObject.toJSONString(stockService.getIndustryRank());
        JSONObject.toJSONString(stockService.getStockRank(new StockRankRequest()));
        JSONObject.toJSONString(stockService.getDayData("sh000001", LocalDate.of(2022,01,01),LocalDate.of(2023,03,01),1));
    }
    @Test
    public void testGetStockRank() {
        stockService.getStockRank(new StockRankRequest());
        stockService.getStockRank(new StockRankRequest());
    }
    @Test
    public void testGetDayData() {
        // System.out.println(JSONObject.toJSONString(stockService.getDayData()));
    }
    @Test
    public void testGetDateRangeData() {

        System.out.println(JSONObject.toJSONString(stockService.getDateRangeData("sh000001", LocalDate.of(2022,01,01),LocalDate.of(2023,03,01),1,true)));
        System.out.println(JSONObject.toJSONString(stockService.getStock("000001")));

        System.out.println(JSONObject.toJSONString(stockService.getStockAll("")));

    }
    @Test
    public void testGetStock() {
        // System.out.println(JSONObject.toJSONString(stockService.getStock()));
    }
    @Test
    public void testGetStockList() {
        // System.out.println(JSONObject.toJSONString(stockService.getStockList()));
    }
    @Test
    public void testGetStockAll() {
        // System.out.println(JSONObject.toJSONString(stockService.getStockAll()));
    }
    @Test
    public void testGetIndexAll() {
        // System.out.println(JSONObject.toJSONString(stockService.getStockList()));
    }
}
