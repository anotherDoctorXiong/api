package club.doctorxiong.api.fundfactory.factory;

import club.doctorxiong.api.fundfactory.FillFundDetailFactoryService;
import club.doctorxiong.api.fundfactory.FundDetailInterface;
import club.doctorxiong.api.common.dto.FundDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.uitls.StringUtil;

/**
 * @author XIONGXIN
 * @title: FundDefaultFactory
 * @projectName miniprogress
 * @description: 填充基金代码
 * @date 2021/1/30 14:17
 */
@Service
public class FillFundTypeFactory implements FundDetailInterface , InitializingBean {
    @Autowired
    private FillFundDetailFactoryService fillFundDetailFactoryService;
    private String HB_TYPE = "货币型";
    @Override
    public void fillFundDetail(String input, FundDTO fundDTODetail) {
        if(Boolean.valueOf(StringUtil.getValue(input))){
            fundDTODetail.setType(HB_TYPE);
        }
    }

    @Override
    public void afterPropertiesSet()  {
        // fillFundDetailFactoryService.registerOnFactory(FundOptionalEnum.FUND_HB.getOptionalName(),this);
    }
}
