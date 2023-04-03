package club.doctorxiong.api.controller.v1;


import club.doctorxiong.api.common.CommonResponse;
import club.doctorxiong.api.common.RedisKeyConstants;
import club.doctorxiong.api.common.dto.ConvertBondDTO;
import club.doctorxiong.api.common.dto.IndustryDetailDTO;
import club.doctorxiong.api.common.dto.StockDTO;
import club.doctorxiong.api.common.page.PageRequest;
import club.doctorxiong.api.component.CommonDataComponent;
import club.doctorxiong.api.service.StockService;
import club.doctorxiong.api.common.request.StockRankRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import club.doctorxiong.api.common.page.PageData;


import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static club.doctorxiong.api.component.CommonDataComponent.convertBondList;

/**
 * @auther: 熊鑫
 * @Date: 2019/6/24 21
 * @Description:
 */
@RestController
@RequestMapping(value = "/stock")
public class StockController {


    @Resource
    private StockService stockService;
    @Resource
    private CommonDataComponent commonDataComponent;




    /**
     * @param stockCode
     * @name: stockDetail
     * @auther: 熊鑫
     * @return:
     * @date: 2020/9/7 10:09
     * @description: 返回分时信息包括最新的股票信息
     */
    @RequestMapping(value = "/min", method = RequestMethod.GET)
    public CommonResponse<StockDTO> stockMinData(@RequestParam("code") String stockCode) {
        return CommonResponse.OK(stockService.getStock(stockCode));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public CommonResponse<List<StockDTO>> stockList(@RequestParam("code") String codeStr) {
        return CommonResponse.OK(stockService.getStockList(codeStr));
    }

    @RequestMapping(value = "/kline/day", method = RequestMethod.GET)
    public CommonResponse getKLine(@RequestParam("code") String code,
                                   @RequestParam(required = false, value = "startDate") LocalDate startDate,
                                   @RequestParam(required = false, value = "endDate")  LocalDate endDate,
                                   @RequestParam(required = false, value = "type", defaultValue = "0") Integer type) {


        return CommonResponse.OK(stockService.getDayData(code, startDate, endDate, type));
    }

    @RequestMapping(value = "/kline/week", method = RequestMethod.GET)
    public CommonResponse getKLineWeek(@RequestParam("code") String code,
                                       @RequestParam(required = false, value = "startDate") LocalDate startDate,
                                       @RequestParam(required = false, value = "endDate") LocalDate endDate,
                                       @RequestParam(required = false, value = "type", defaultValue = "0") Integer type) {

        return CommonResponse.OK(stockService.getDateRangeData(code, startDate, endDate, type,true));
    }

    @RequestMapping(value = "/kline/month", method = RequestMethod.GET)
    public CommonResponse getKLineMonth(@RequestParam("code") String code,
                                        @RequestParam(required = false, value = "startDate") LocalDate startDate,
                                        @RequestParam(required = false, value = "endDate") LocalDate endDate,
                                        @RequestParam(required = false, value = "type", defaultValue = "0") Integer type) {

        return CommonResponse.OK(stockService.getDateRangeData(code, startDate, endDate, type,false));
    }

    @RequestMapping(value = "/rank", method = RequestMethod.POST)
    public CommonResponse<PageData<StockDTO>> stockRank(@RequestBody StockRankRequest request) {
        return CommonResponse.OK(stockService.getStockRank(request));
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public CommonResponse<List<StockDTO>> getBoard() {
        return CommonResponse.OK(stockService.getStockList("sh000001,sz399300,sh000905,sz399001,sz399005,sz399006"));
    }

    @RequestMapping(value = "/industry/rank", method = RequestMethod.GET)
    public CommonResponse<List<IndustryDetailDTO>> IndustryRank() {
        return CommonResponse.OK(stockService.getIndustryRank());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResponse allStock(@RequestParam(required = false) String keyWord) {
        // 这里关注下接口性能,为了不改变接口返回结构妥协下
        return CommonResponse.OK(stockService.getStockAll(keyWord));
    }

    @RequestMapping(value = "/index/all", method = RequestMethod.GET)
    public CommonResponse allIndex() {
        return CommonResponse.OK(stockService.getIndexAll());
    }



    @RequestMapping(value = "/convertBond", method = RequestMethod.POST)
    public CommonResponse<PageData<ConvertBondDTO>> getConvertBond(@RequestBody PageRequest request) {
        if (!RedisKeyConstants.CONVERT_BOND_DATE_OFF_SET.isEqual(LocalDate.now()) || convertBondList.isEmpty()) {
            commonDataComponent.refreshConvertBondList();
            RedisKeyConstants.CONVERT_BOND_DATE_OFF_SET = LocalDate.now();
        }
        PageData<ConvertBondDTO> convertBondPage = new PageData();
        convertBondPage.setPageIndex(request.getPageIndex());
        convertBondPage.setPageSize(request.getPageSize());
        convertBondPage.setTotalRecord(convertBondList.size());
        convertBondPage.setRank(convertBondList.subList(request.getStartNo(),
                request.getPageIndex() * request.getPageSize() > convertBondPage.getTotalRecord() ? convertBondPage.getTotalRecord() : request.getPageIndex() * request.getPageSize()));
        return CommonResponse.OK(convertBondPage);
    }

    @RequestMapping(value = "/convertBond", method = RequestMethod.GET)
    public CommonResponse<List<ConvertBondDTO>> getConvertBond() {
        return CommonResponse.OK(convertBondList);
    }



}
