package club.doctorxiong.api.component;



import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.common.dto.FundExpectDataDTO;
import club.doctorxiong.api.common.dto.FundPositionDTO;
import club.doctorxiong.api.common.request.FundRankRequest;
import club.doctorxiong.api.fundfactory.FillFundDetailFactoryService;
import club.doctorxiong.api.uitls.StringUtil;
import club.doctorxiong.api.uitls.UrlUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static club.doctorxiong.api.common.LocalDateTimeFormatter.getLocalDateTimeByTimestamp;


/**
 * @Auther: 熊鑫
 * @Date: 2019/6/9 22
 * @Description: 从网络获取相关基金信息
 */
@Component
@Slf4j
public class FundComponent {
    @Autowired
    private OkHttpComponent httpSupport;

    @Autowired
    private FillFundDetailFactoryService fillFundDetail;


    /**
     * @param fund_code : 基金代码
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.Fund
     * @date: 2020/6/9 22:51
     * @Description: 通过基金代码获取最新的基金信息
     */
    public FundDTO getFundDetail(String fund_code)  {
        FundDTO fundDTODetail = new FundDTO();
        //生成获取相关信息的url
        String detail_url = UrlUtil.getFundDetailUrl(fund_code);
        //从基金详情url中获取基金信息
        String detail_str = httpSupport.get(detail_url);
        if(detail_str.contains("<html>")){
            return null;
        }
        String[] arr = detail_str.replace(" ","").split(";");
        for (int i = 0; i < arr.length; i++) {
            //经split分割的字符串数组为空时长度仍然为1
            if (arr[i].length() <= 1) {
                continue;
            }
            //从字符串获取相关数据
            fillFundDetail.fillFundDetail(arr[i], fundDTODetail);
        }
        if(fundDTODetail.getName() == null){
            log.error("getFundError detail_str");
        }
        return fundDTODetail;
    }



    public FundExpectDataDTO getFundExpectData(String fundCode){
        FundExpectDataDTO fundExpectDataDTO =new FundExpectDataDTO();
        //非货币型基金才需要查询最新净值估算
        String export_url = UrlUtil.getFundExportUrl(fundCode);
        String export_str = httpSupport.get(export_url);
        //部分类型的基金无法不提供净值估算
        if (export_str != null&&export_str.length()>10) {
            JSONObject export = JSONObject.parseObject(export_str.substring(export_str.indexOf('(') + 1, export_str.lastIndexOf(')')));
            fundExpectDataDTO.setExpectGrowth(export.getString("gszzl"));
            fundExpectDataDTO.setExpectWorth(new BigDecimal(export.getString("gsz")));
            fundExpectDataDTO.setExpectWorthDate(getLocalDateTimeByTimestamp(export.getLong("gztime")));
        }else {
            fundExpectDataDTO = null;
        }
        return fundExpectDataDTO;
    }



    /**
     * @name: getFundPosition
     * @auther: 熊鑫
     * @param code
     * @return: club.doctorxiong.stub.dto.fund.FundPosition
     * @date: 2020/5/26 10:18
     * @description: 获取基金持仓信息,此处是直接爬取的html标签,后期更新注意标签更换即可
     */
    public FundPositionDTO getFundPosition(String code){

        String res=httpSupport.get(UrlUtil.getHTMLUrl(code));

        FundPositionDTO fundPositionDTO =new FundPositionDTO();
        Document doc = Jsoup.parse(res);
        List<String[]> list=new LinkedList<>();
        Elements links = doc.getElementsByTag("tr");
        if(links.size()>0){
            fundPositionDTO.setTitle(doc.getElementsByClass("left").get(0).text());
            for(int i=1;i<links.size();i++){
                Elements one=links.get(i).getElementsByTag("td");
                String[] arr=new String[5];
                if(one.size()==9){
                    arr[0]=one.get(1).text();
                    arr[1]=one.get(2).text();
                    arr[2]=one.get(6).text();
                    arr[3]=one.get(7).text();
                    arr[4]=one.get(8).text();
                    list.add(arr);
                }
            }
        }else {
            log.info("该基金获取获取持仓详情-"+code);
            return fundPositionDTO;
        }

        fundPositionDTO.setStockList(list);
        String res1=httpSupport.get(UrlUtil.getHTMLUrl1(code));
        Document doc1 = Jsoup.parse(res1);
        Elements el=doc1.getElementsByClass("w782 comm tzxq");
        if(el.size()>0){
            Elements td=el.first().getElementsByTag("tbody").first().getElementsByTag("tr").first().getElementsByTag("td");
            if(td.size()>=5){
                try {
                    fundPositionDTO.setDate(LocalDate.parse(td.get(0).text()));
                } catch (Exception e) {
                    log.info("不做任何处理");
                }
                fundPositionDTO.setStock(td.get(1).text());
                fundPositionDTO.setBond(td.get(2).text());
                fundPositionDTO.setCash(td.get(3).text());
                fundPositionDTO.setTotal(td.get(td.size()-1).text());
            }
        }
        return fundPositionDTO;
    }




    /**
     * @description : 获取基金排行信息,主要用于基金的展示
     * @param fundRankRequest : 排行及分类的条件
     * @return : java.util.List<club.doctorxiong.stub.dto.fund.ShowFund>
     * @Exception :
     * @author : 熊鑫
     * @date   : 2019/6/11 14:17
     */
    public JSONObject getFundRank(FundRankRequest fundRankRequest) {
        Headers headers = new Headers.Builder().add("Referer","http://fund.eastmoney.com/data/fundranking.html").build();
        String fundRankUrl = UrlUtil.creatGetUrlWithParams(UrlUtil.getFundRankBaseUrl(),fundRankRequest);
        String str=httpSupport.getWithHeaders(fundRankUrl,headers).replaceAll(";","");
        return JSONObject.parseObject(StringUtil.getValue(str));
    }

    public JSONArray getAllFund() {
        String str=httpSupport.get(UrlUtil.getAllFundUrl());
        return JSONArray.parseArray(StringUtil.getValue(str).replaceAll(";",""));
    }
}
