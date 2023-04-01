package club.doctorxiong.api.fundfactory.factory;

import club.doctorxiong.api.common.LocalDateTimeFormatter;
import club.doctorxiong.api.fundfactory.FillFundDetailFactoryService;
import club.doctorxiong.api.fundfactory.FundDetailInterface;
import club.doctorxiong.api.fundfactory.FundOptionalEnum;
import club.doctorxiong.api.common.dto.FundDTO;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.uitls.StringUtil;

/**
 * @author XIONGXIN
 * @title: FundDefaultFactory
 * @description: 填充基金代码
 * @date 2021/1/30 14:17
 */
@Service
public class FillFundMillionCopiesIncomeFactory implements FundDetailInterface , InitializingBean {
    @Autowired
    private FillFundDetailFactoryService fillFundDetailFactoryService;
    @Override
    public void fillFundDetail(String input, FundDTO fundDTODetail) {
        JSONArray jsonArray = JSONArray.parseArray(StringUtil.getValue(input));
        if(jsonArray.size()==0){
            return;
        }
        //货币基金每万份收益
        fundDTODetail.setMillionCopiesIncomeData(StringUtil.jsonToTwoArr1(jsonArray));
        jsonArray=jsonArray.getJSONArray(jsonArray.size() - 1);
        fundDTODetail.setMillionCopiesIncomeDate(LocalDateTimeFormatter.getLocalDateByTimestamp(jsonArray.getLong(0)));
        fundDTODetail.setMillionCopiesIncome(jsonArray.getBigDecimal(1));

    }

    @Override
    public void afterPropertiesSet()  {
        fillFundDetailFactoryService.registerOnFactory(FundOptionalEnum.FUND_MILLION_COPIES_INCOME.getOptionalName(),this);
    }
}
