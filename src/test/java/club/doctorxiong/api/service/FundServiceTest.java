package club.doctorxiong.api.service;
import club.doctorxiong.api.common.dto.FundShowDataDTO;
import club.doctorxiong.api.common.page.PageData;
import club.doctorxiong.api.common.request.FundRankRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;

import java.time.LocalDate;

/**
* FundServiceTester.
*
* @Author: DoctorXiong.club
* @CreateDate: 2023-04-06
*/
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class FundServiceTest {

    @Autowired
    private FundService fundService;

    private String fundCode = "003634";

    @Test
    public void testGetFund() {
        System.out.println(JSONObject.toJSONString(fundService.getFund(fundCode,null,null)));
        FundRankRequest request = new FundRankRequest();
        for (int i = 0; i < 10; i++) {
            request.setPi(i);
            PageData<FundShowDataDTO> pageData = fundService.getFundRank(request);
            pageData.getRank().forEach(fundShowDataDTO -> {
                fundService.getFund(fundShowDataDTO.getCode(), LocalDate.of(2022,10,10),null).toString();
            });
        }

    }
    @Test
    public void testGetFundListForCodeStrStartDateEndDate() {
        // System.out.println(JSONObject.toJSONString(fundService.getFundListForCodeStrStartDateEndDate()));
    }
    @Test
    public void testGetFundExpect() {
        // System.out.println(JSONObject.toJSONString(fundService.getFundExpect()));
    }
    @Test
    public void testGetFundListStr() {
        // System.out.println(JSONObject.toJSONString(fundService.getFundListStr()));
    }
    @Test
    public void testGetFundRank() {
        // System.out.println(JSONObject.toJSONString(fundService.getFundRank()));
    }
    @Test
    public void testGetFundPosition() {
        // System.out.println(JSONObject.toJSONString(fundService.getFundPosition()));
    }
    @Test
    public void testGetAllFund() {
        System.out.println(fundService.getAllFund(null).size());
        System.out.println(fundService.getAllFund("ETF").size());
        fundService.getAllFund("etf").forEach(v -> {
            System.out.println(v.toString());
        });
        //System.out.println(JSONObject.toJSONString(fundService.getAllFund(null)));
    }
}
