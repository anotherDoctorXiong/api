package club.doctorxiong.api.controller;


import club.doctorxiong.api.common.CommonResponse;
import club.doctorxiong.api.common.dto.DailyIndexDataDTO;
import club.doctorxiong.api.entity.DailyIndexData;
import club.doctorxiong.api.service.IDailyIndexDataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 直接按时间顺序返回数据库的数据
 * @Author: 熊鑫
 * @Date: 2020/5/10 16
 */
@RestController
public class InfoController {

    @Resource
    private IDailyIndexDataService dailyIndexDataService;

    @RequestMapping(value = "/dailyData",method = RequestMethod.GET)
    public CommonResponse<List<DailyIndexDataDTO>> getDailyData() {
        return CommonResponse.OK(dailyIndexDataService.list().stream().sorted(Comparator.comparing(DailyIndexData::getDate)).collect(Collectors.toList()));
    }
}
