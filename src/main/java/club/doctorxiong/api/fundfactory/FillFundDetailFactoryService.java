package club.doctorxiong.api.fundfactory;


import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.uitls.StringUtil;

import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author XIONGXIN
 * @title: CallServiceFactory
 * @date 2021/1/7 16:05
 */
@Service
public class FillFundDetailFactoryService {


    private static Map<String, FundDetailInterface> factoryMap = new ConcurrentHashMap<>();


    public void fillFundDetail(String input, FundDTO fundDTODetail) {
        String optionalName = StringUtil.getKey(input);
        if (factoryMap.containsKey(optionalName)) {
            factoryMap.get(optionalName).fillFundDetail(input, fundDTODetail);
        }

    }

    public void registerOnFactory(String optionalName, FundDetailInterface service) {
        if (containMethod(optionalName)) {
            factoryMap.put(optionalName, service);
        }
    }

    public boolean containMethod(String optionalName) {
        boolean contain = false;
        for (FundOptionalEnum currentMethod : FundOptionalEnum.values()) {
            if (currentMethod.getOptionalName().equals(optionalName)) {
                contain = true;
            }
        }
        return contain;
    }
}
