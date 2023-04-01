package club.doctorxiong.api.component;


import club.doctorxiong.api.common.dto.ConvertBondDTO;
import club.doctorxiong.api.common.dto.IndustryDetailDTO;
import club.doctorxiong.api.common.dto.StockDTO;
import club.doctorxiong.api.common.request.StockRankRequest;
import club.doctorxiong.api.uitls.UrlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import club.doctorxiong.api.uitls.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author: 熊鑫
 * @Date: 2019/6/24 14
 * @Description: 获取股票相关数据并按照当前时间进行缓存,查询失败的代码存入数据库进行人工核对
 */
@Component
@Slf4j
public class StockComponent  {

    @Autowired
    private OkHttpComponent httpSupport;





    public StockDTO getStockData(String stockCode){
        String res=httpSupport.get(UrlUtil.getStockDataUrl(stockCode));
        JSONObject object=JSON.parseObject(StringUtil.getValue(res));
        if(object.getInteger("code")==-1){
            //无法获取该股票信息
            return new StockDTO();
        }
        JSONObject data=object.getJSONObject("data").getJSONObject(stockCode);
        //最新基础数据
        String[] stockData=data.getJSONObject("qt").getObject(stockCode,String[].class);
        StockDTO stockDTO =new StockDTO(stockData);


        //分时数据
        JSONObject jsonMinData=data.getJSONObject("data");
        String[] minArr=jsonMinData.getObject("data",String[].class);
        List<String[]> minList=new ArrayList<>();
        for(String min:minArr){
            String[] curMinArr = min.split(" ");
            // 科创板的成交量是股
            if(stockDTO.getType().contains("KCB")){
                curMinArr[2] = String.valueOf(Integer.valueOf(curMinArr[2])/100 + (Integer.valueOf(curMinArr[2])%100>50?1:0));
            }
            minList.add(curMinArr);
        }
        stockDTO.setMinData(minList);
        return stockDTO;
    }

    /**
     * @name: getKLineData
     * @auther: 熊鑫
     * @param stockCode
     * @param type
     * @return: club.doctorxiong.stub.dto.stock.Stock
     * @date: 2020/9/6 16:00
     * @description: 按照复权不同分为三类
     */
    public StockDTO getKLineData(String stockCode, int type)  {

        String stockUrl;
        String key;
        switch (type){
            case 1:
                stockUrl = UrlUtil.getStockKLineQFQUrl(stockCode);
                key="qfqday";
                break;
            case 2:
                stockUrl = UrlUtil.getStockKLineHFQUrl(stockCode);
                key="hfqday";
                break;
            default:
                stockUrl = UrlUtil.getStockKLineUrl(stockCode);
                key="day";
        }
        String res = httpSupport.get(stockUrl);
        JSONObject jsonObject= JSON.parseObject(res);
        if(jsonObject.getString("msg").contains("error")){
            //无法获取该股票信息
            return new StockDTO();
        }

        JSONObject data=jsonObject.getJSONObject("data").getJSONObject(stockCode);
        //最新基础数据
        String[] stockData=data.getJSONObject("qt").getObject(stockCode,String[].class);
        StockDTO stockDTO =new StockDTO(stockData);
        if(stockDTO == null || stockDTO.getName() == null){
            CommonDataComponent.allStockCode.remove(stockCode);
        }
        if("ZS".equals(stockDTO.getType())){
            key="day";
        }
        JSONObject keyData=jsonObject.getJSONObject("data").getJSONObject(stockCode);
        //K线数据
        String klineData=keyData.containsKey(key)?keyData.getString(key):keyData.getString("day");
        stockDTO.setArrData(JSON.parseObject(klineData,String[][].class));
        return stockDTO;
    }


    /**
     * @name: getStockDetail
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.stock.StockDetail
     * @date: 2019/6/24 21:32
     * @description: 获取股票详情和日K数据
     */
    public List<IndustryDetailDTO> getStockIndustryRank()  {
        String industryStr = httpSupport.get(UrlUtil.getSWUrl());
        JSONArray jsonArray = JSONArray.parseArray(industryStr);
        JSONArray jsonArray1 = jsonArray.getJSONArray(1);
        JSONArray jsonArray2 = jsonArray1.getJSONArray(0);
        JSONArray jsonArray3 = jsonArray2.getJSONArray(1);
        JSONArray jsonArray4 = jsonArray3.getJSONArray(1);
        JSONArray jsonArray5 = jsonArray4.getJSONArray(1);
        List<IndustryDetailDTO> res = jsonArray5.stream().map(arr -> {
            JSONArray jsonArray6 = (JSONArray)arr;
            return new IndustryDetailDTO(jsonArray6.getString(2),jsonArray6.getString(0));
        }).collect(Collectors.toList());
        return res;
    }

    /**
     * @name: industryStockCodeStr
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/7/17 10:12
     * @description: 获取板块的股票代码字符串,偷懒直接返回json
     * {t:'pt012069/chr',p:1,total:72,l:10,o:0,data:'sz002839,sh601577,sh600908,sh601128,sz002807,sz002142,sh601916,sh601009,sh600000,sh600926'}
     */
    public JSONArray getSinaStockRank(StockRankRequest request){
        String stockCodeStr=httpSupport.get(UrlUtil.getSinaStockRankUrl(request));
        return JSONArray.parseArray(stockCodeStr);
    }

    public Integer getSinaStockCount(String node){
        return Integer.valueOf(httpSupport.get(UrlUtil.getSinaStockCountUrl(node)).replaceAll("\"",""));
    }

    public List<String[]> getAllStockOrIndex(boolean stock){
        String all;
        if(stock){
            all=httpSupport.get(UrlUtil.getAllStockUrl());
        }else {
            all=httpSupport.get(UrlUtil.getAllIndexUrl());
        }

        List<String[]> allStock=new ArrayList<>();
        String[] arr1 = all.toLowerCase().split("<br/>");
        for (int j = 1; j < arr1.length ; j++) {
            String[] arr2 = arr1[j].split(",");
            if(arr2.length >= 3){
                allStock.add(Arrays.copyOfRange(arr2,1,3));
            }
        }
        return allStock;
    }

    /**
     * @name: getInnerFund
     * @auther: 熊鑫
     * @param
     * @return: void
     * @date: 2021/9/16 16:20
     * @description: 获取场内基金
     */
    public List<String[]> getInnerFund(){
        String allFund = httpSupport.get(UrlUtil.getAllInnerFund());
        JSONObject json = JSONObject.parseObject(allFund);
        JSONArray arr = json.getJSONArray("rows");
        return arr.stream().map(obj ->{
            JSONObject jsonObject = (JSONObject)obj;
            String[] cellArr = new String[2];
            String code = jsonObject.getString("id");
            code = StringUtil.getTotalStockCode(code);
            cellArr[0] = code;
            cellArr[1] = jsonObject.getJSONObject("cell").getString("fund_nm");
            return cellArr;
        }).collect(Collectors.toList());
    }

    public List<ConvertBondDTO> getConvertBondList(){
        Integer pageIndex = 1;
        String convertBondRank = httpSupport.get(UrlUtil.getConvertBondRankUrl(pageIndex));
        JSONObject jsonObject = JSONObject.parseObject(convertBondRank);
        JSONArray list = jsonObject.getJSONObject("result").getJSONArray("data");
        while (jsonObject.getJSONObject("result").getInteger("pages").equals(pageIndex)){
            convertBondRank = httpSupport.get(UrlUtil.getConvertBondRankUrl(++pageIndex));
            jsonObject = JSONObject.parseObject(convertBondRank);
            list.addAll(jsonObject.getJSONObject("result").getJSONArray("data"));
        }
        return list.toJavaList(ConvertBondDTO.class);
    }
}


