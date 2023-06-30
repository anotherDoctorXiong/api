package club.doctorxiong.api.component;

import club.doctorxiong.api.common.request.FundRankRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;

/**
* FundComponentTester.
*
* @Author: DoctorXiong.club
* @CreateDate: 2023-04-03
*/
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class FundComponentTest {

    @Autowired
    private FundComponent fundComponent;

    @Test
    public void testGetFundDetail() {
        // System.out.println(JSONObject.toJSONString(fundComponent.getFundDetail()));
    }

    @Test
    public void testGetFundExpectDataTest() {
        // System.out.println(JSONObject.toJSONString(fundComponent.getFundExpectDataTest()));
    }
    @Test
    public void testGetFundPosition() {
        // System.out.println(JSONObject.toJSONString(fundComponent.getFundPosition()));
    }
    @Test
    public void testGetFundRank() {
        // System.out.println(JSONObject.toJSONString(fundComponent.getFundRank()));
    }
    @Test
    public void testGetAllFund() {
        // System.out.println(JSONObject.toJSONString(fundComponent.getAllFund()));
    }
}
