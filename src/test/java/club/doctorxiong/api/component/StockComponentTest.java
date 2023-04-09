package club.doctorxiong.api.component;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;

/**
* StockComponentTester.
*
* @Author: DoctorXiong.club
* @CreateDate: 2023-04-06
*/
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class StockComponentTest {

    @Autowired
    private StockComponent stockComponent;

    @Test
    public void testGetStockData() {
        // System.out.println(JSONObject.toJSONString(stockComponent.getStockData("sh000001")));
    }
    @Test
    public void testGetKLineData() {
        // System.out.println(JSONObject.toJSONString(stockComponent.getKLineData()));
    }
    @Test
    public void testGetStockIndustryRank() {
        // System.out.println(JSONObject.toJSONString(stockComponent.getStockIndustryRank()));
    }
    @Test
    public void testGetSinaStockRank() {
        // System.out.println(JSONObject.toJSONString(stockComponent.getSinaStockRank()));
    }
    @Test
    public void testGetSinaStockCount() {
        // System.out.println(JSONObject.toJSONString(stockComponent.getSinaStockCount()));
    }
    @Test
    public void testGetAllStockOrIndex() {
        // System.out.println(JSONObject.toJSONString(stockComponent.getAllStockOrIndex()));
    }
    @Test
    public void testGetInnerFund() {
        // System.out.println(JSONObject.toJSONString(stockComponent.getInnerFund()));
    }
    @Test
    public void testGetConvertBondList() {
        // System.out.println(JSONObject.toJSONString(stockComponent.getConvertBondList()));
    }
}
