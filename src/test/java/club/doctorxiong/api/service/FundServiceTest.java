package club.doctorxiong.api.service;
import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.common.page.PageData;
import club.doctorxiong.api.common.request.FundRankRequest;
import club.doctorxiong.api.entity.Token;
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
    @Autowired
    private ITokenService tokenService;

    private String fundCode = "003634";



    @Test
    public void testGetFundListForCodeStrStartDateEndDate() {
        List<Token> tokenList = tokenService.list();

        String json = JSONArray.toJSONString(tokenList);
        System.out.println(json);
        List<Token> tokens = JSONArray.parseArray(json,Token.class);

        String filePath = "C:\\Users\\xiong\\IdeaProjects\\api\\token.txt";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testGetFundExpect() {
        // System.out.println(JSONObject.toJSONString(fundService.getFundExpect()));
    }
    @Test
    public void testGetFundListStr() {
        fundService.getFund("016018",null,null,true);
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
