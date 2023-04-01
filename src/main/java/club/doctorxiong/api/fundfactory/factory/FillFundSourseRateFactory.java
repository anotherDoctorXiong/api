package club.doctorxiong.api.fundfactory.factory;

import club.doctorxiong.api.fundfactory.FillFundDetailFactoryService;
import club.doctorxiong.api.fundfactory.FundDetailInterface;
import club.doctorxiong.api.fundfactory.FundOptionalEnum;
import club.doctorxiong.api.common.dto.FundDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.uitls.StringUtil;

/**
 * @author XIONGXIN
 * @title: FundDefaultFactory
 * @projectName miniprogress
 * @description: 填充基金
 * @date 2021/1/30 14:17
 */
@Service
public class FillFundSourseRateFactory implements FundDetailInterface , InitializingBean {
    @Autowired
    private FillFundDetailFactoryService fillFundDetailFactoryService;
    @Override
    public void fillFundDetail(String input, FundDTO fundDTODetail) {
        fundDTODetail.setBuySourceRate(StringUtil.getSubValue(input));
    }

    @Override
    public void afterPropertiesSet()  {
        fillFundDetailFactoryService.registerOnFactory(FundOptionalEnum.FUND_SOURCE_RATE.getOptionalName(),this);
    }
}
