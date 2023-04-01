package club.doctorxiong.api.controller.v1;


import club.doctorxiong.api.common.CommonResponse;
import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.common.dto.FundPositionDTO;
import club.doctorxiong.api.common.dto.FundShowDataDTO;
import club.doctorxiong.api.service.FundService;
import club.doctorxiong.api.common.request.FundRankRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import club.doctorxiong.api.common.page.PageData;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;



/**
 * 基金接口
 * @Author: 熊鑫
 * @Date: 2019/6/17 14
 * @Description: 接受并验证参数
 */

@RestController
@RequestMapping(value = "/fund")
public class FundController {


    @Resource
    private FundService fundService;




    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public CommonResponse<FundDTO> fundDetail(
            @RequestParam("code") String code,
            @RequestParam(required = false, value = "startDate") LocalDate startDate,
            @RequestParam(required = false, value = "endDate")  LocalDate endDate
    ) {
        return CommonResponse.OK(fundService.getFund(code, startDate, endDate));
    }

    @RequestMapping(value = "/detail/list", method = RequestMethod.GET)
    public CommonResponse<List<FundDTO>> fundDetailList(
            @RequestParam("code") String codeStr,
            @RequestParam(required = false, value = "startDate")  LocalDate startDate,
            @RequestParam(required = false, value = "endDate")  LocalDate endDate
    ) {
        return CommonResponse.OK(fundService.getFundList(codeStr, startDate, endDate));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public CommonResponse<List<FundShowDataDTO>> fund(@RequestParam(value = "code") String codeStr) {
        return CommonResponse.OK(fundService.getFundList(codeStr));
    }


    @RequestMapping(value = "/rank", method = RequestMethod.POST)
    public CommonResponse<PageData<FundShowDataDTO>> fundRank(@RequestBody FundRankRequest request) {
        return CommonResponse.OK(fundService.getFundRank(request));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResponse<List<String[]>> getAllFund(@RequestParam(required = false) String keyWord) {
        return CommonResponse.OK(fundService.getAllFund(keyWord));
    }

    /**
     * @param
     * @name: hotRank
     * @auther: 熊鑫
     * @return:
     * @date: 2019/6/27 23:30
     * @description: 热门基金返回近年来收益较高且基金规模较大的基金
     */
    @RequestMapping(value = "/hot", method = RequestMethod.GET)
    public CommonResponse<List<FundShowDataDTO>> hotRank() {
        FundRankRequest request = new FundRankRequest();
        request.setSe(100);
        request.setSc("3y");
        return CommonResponse.OK(fundService.getFundRank(request).getRank());
    }

    @RequestMapping(value = "/position", method = RequestMethod.GET)
    public CommonResponse<FundPositionDTO> getFundPosition(@RequestParam String code) {
        return CommonResponse.OK(fundService.getFundPosition(code));
    }


}
