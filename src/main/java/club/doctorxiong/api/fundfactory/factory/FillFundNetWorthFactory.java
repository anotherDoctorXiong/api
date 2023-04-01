package club.doctorxiong.api.fundfactory.factory;

import club.doctorxiong.api.fundfactory.FillFundDetailFactoryService;
import club.doctorxiong.api.fundfactory.FundDetailInterface;
import club.doctorxiong.api.fundfactory.FundOptionalEnum;
import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.common.dto.NetWorthDataDTO;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.uitls.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XIONGXIN
 * @title: FundDefaultFactory
 * @projectName miniprogress
 * @description: 填充基金净值信息
 * @date 2021/1/30 14:17
 */
@Service
public class FillFundNetWorthFactory implements FundDetailInterface , InitializingBean {
    @Autowired
    private FillFundDetailFactoryService fillFundDetailFactoryService;
    @Override
    public void fillFundDetail(String input, FundDTO fundDTODetail) {
        JSONArray jsonArray = JSONArray.parseArray(StringUtil.getValue(input));
        if(jsonArray.size()==0){
            return;
        }
        String[][] netWorthData=jsonToTwoArr2(jsonArray);
        fundDTODetail.setNetWorthData(netWorthData);
        int index=netWorthData.length-1;
        fundDTODetail.setNetWorthDate(LocalDate.parse(netWorthData[index][0]));
        fundDTODetail.setNetWorth(new BigDecimal(netWorthData[index][1]));
        fundDTODetail.setDayGrowth(netWorthData[index][2]);

        LocalDate sevenDayBefore = fundDTODetail.getNetWorthDate().plusDays(-7);
        while (index>=0&&sevenDayBefore.toString().compareTo(netWorthData[index][0])<0){
            index--;
        }
        //如果是第一条数据则说明数据长度不足,无法计算出周涨幅
        if(index>0){
            BigDecimal lastWeekNetWorth=new BigDecimal(netWorthData[index][1]);
            fundDTODetail.setLastWeekGrowth(fundDTODetail.getNetWorth().subtract(lastWeekNetWorth).multiply(new BigDecimal("100")).divide(lastWeekNetWorth,4, RoundingMode.HALF_UP).toString());
        }
    }

    @Override
    public void afterPropertiesSet()  {
        fillFundDetailFactoryService.registerOnFactory(FundOptionalEnum.FUND_NET_WORTH_DATA.getOptionalName(),this);
    }

    public static String[][] jsonToTwoArr2(JSONArray jsonArray) {
        List<NetWorthDataDTO> list = jsonArray.toJavaList(NetWorthDataDTO.class);
        return list.parallelStream().map(netWorthDataDTO -> netWorthDataDTO.getDataArr()).collect(Collectors.toList()).toArray(new String[list.size()][4]);
    }
}
