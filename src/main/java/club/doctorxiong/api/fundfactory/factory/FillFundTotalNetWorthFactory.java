package club.doctorxiong.api.fundfactory.factory;

import club.doctorxiong.api.fundfactory.FillFundDetailFactoryService;
import club.doctorxiong.api.fundfactory.FundDetailInterface;
import club.doctorxiong.api.fundfactory.FundOptionalEnum;
import club.doctorxiong.api.common.dto.FundDTO;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.uitls.StringUtil;

import java.math.BigDecimal;


/**
 * @author XIONGXIN
 * @title: FundDefaultFactory
 * @projectName miniprogress
 * @description: 填充基金净值信息
 * @date 2021/1/30 14:17
 */
@Service
public class FillFundTotalNetWorthFactory implements FundDetailInterface , InitializingBean {
    @Autowired
    private FillFundDetailFactoryService fillFundDetailFactoryService;
    @Override
    public void fillFundDetail(String input, FundDTO fundDTODetail) {
        JSONArray jsonArray = JSONArray.parseArray(StringUtil.getValue(input));
        if(jsonArray.size()==0){
            return;
        }
        fundDTODetail.setTotalNetWorthData(StringUtil.jsonToTwoArr1(jsonArray));
        fundDTODetail.setTotalWorth((BigDecimal) jsonArray.getJSONArray(jsonArray.size() - 1).get(1));
    }

    @Override
    public void afterPropertiesSet()  {
        fillFundDetailFactoryService.registerOnFactory(FundOptionalEnum.FUND_TOTAL_NET_WORTH_DATA.getOptionalName(),this);
    }
}
