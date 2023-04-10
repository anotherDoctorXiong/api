package club.doctorxiong.api.service;


import club.doctorxiong.api.common.RedisKeyConstants;

import club.doctorxiong.api.common.response.Jin10Response;
import club.doctorxiong.api.common.response.Jin10SearchResponse;
import club.doctorxiong.api.component.OkHttpComponent;
import club.doctorxiong.api.entity.DailyIndexData;
import club.doctorxiong.api.entity.Email;
import club.doctorxiong.api.common.dto.Jin10ReportData;
import club.doctorxiong.api.common.dto.Jin10SearchData;
import club.doctorxiong.api.uitls.UrlUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.uitls.StringUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: 熊鑫
 * @Date: 2020/8/13 17
 * @Description:
 */
@Service
@Slf4j
public class PushService {

    @Autowired
    private MessageService messageService;
    @Autowired
    private OkHttpComponent okHttpComponent;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IDailyIndexDataService iDailyIndexDataService;
    @Autowired
    private StockService stockService;

    private static BigDecimal divideFive = new BigDecimal(5);

    private static BigDecimal divideTen = new BigDecimal(10);

    private static BigDecimal divideTwenty = new BigDecimal(20);

    private Map<String,BigDecimal> weightMap = new ConcurrentHashMap<>();

    private static List<String> KEY_WORD_LIST = Arrays.asList("贷款市场报价利率","存款准备金率","M2","MLF","LPR");

    public static String SEARCH_APP_ID = "SRvUdAEfVxLO5NAd";


    /*public void pushEmail(List<Email> emails) {

        // 将所有订购的服务按邮箱分类以便同个邮箱多条推送在同一邮件中完成
        Map<String, List<String>> addressMap = new HashMap<>();
        for (Email email : emails) {
            if (addressMap.containsKey(email.getEmail())) {
                addressMap.get(email.getEmail()).add(email.getCode());
            } else {
                List<String> codeList = new ArrayList();
                codeList.add(email.getCode());
                addressMap.put(email.getEmail(), codeList);
            }
        }

        Map<String, String> pushMap = emails.parallelStream().map(email -> email.getCode()).distinct().collect(Collectors.toMap(code -> code, code -> {
            try {
                EmailInfoDTO positionInfo;
                if (code.length() == 6) {
                    //基金推送
                    FundDTO fundDTO = fundService.getFund(code, null, null);
                    if ("货币型".equals(fundDTO.getType()) || "债券型".equals(fundDTO.getType())) {
                        log.info("货币基金和债券基金无需该操作");
                        return "";
                    }
                    if (fundDTO.getNetWorthData().length < 100) {
                        log.info("基金(" + fundDTO.getName() + ")成立时间过短,无法建立数据模型");
                        return "";
                    }
                    //TODO 非工作日也可能出现法定节假日,直接跳过,表现为当天数据未更新
                    *//*if (fund.getExpectWorthDate() == null || fund.getExpectWorthDate().getDate() != new Date().getDate()) {
                        return "";
                    }*//*
                    positionInfo = positionService.getFundEmailInfo(fundDTO);
                } else {
                    StockDTO stockDTO = stockService.getStock(code);
                    if (stockDTO.getDate() == null || stockDTO.getDate().toLocalDate().compareTo(getToday()) == 0) {
                        return "";
                    }
                    positionInfo = positionService.getStockEmailInfo(stockDTO);
                }
                String msg = "<h3>" + positionInfo.getName() + "</h3>"
                        + "建议仓位:<h2>" + positionInfo.getPosition() + "</h2>"
                        + "<big>" + new SimpleDateFormat("YYYY年MM月dd日HH点mm分").format(positionInfo.getDate()) + "</big>&nbsp;&nbsp;"
                        + "当前价格<strong>" + positionInfo.getCurrentPrice() + "</strong>&nbsp;&nbsp;近一年最高价<strong>"
                        + positionInfo.getMax() + "</strong>&nbsp;&nbsp;近一年最低价<strong>" + positionInfo.getMin() + "</strong><br><br>";
                return msg;
            } catch (Exception e) {
                return "";
            }
        }));

        addressMap.entrySet().stream().forEach(
                forEachWrapper(e -> {
                    String pushMessage = e.getValue().stream().map(code -> pushMap.get(code)).filter(str -> str != null).collect(Collectors.joining());
                    messageService.sendMimeMessage(e.getKey(), "仓位推送", pushMessage);
                }));


    }*/

    public void pushJin10Content(List<Email> emails){
        // 开始推送金十数据(央行)
        List<Jin10ReportData> contentList = new LinkedList<>();
        String str = okHttpComponent.getNoException(UrlUtil.getJin10Data(LocalDateTime.now()));
        str = str.replaceAll("金十","");
        Jin10Response<Jin10ReportData> data = JSONObject.parseObject(str, new TypeReference<Jin10Response<Jin10ReportData>>(){});
        data.getData().forEach( currentContent -> {
            if(currentContent.getData().getContent() != null ){
                String content = currentContent.getData().getContent();
                if(KEY_WORD_LIST.stream().anyMatch(keyWord -> content.contains(keyWord))){
                    // 同上一条推送比较,相似度较高则判断为同一条
                    if(contentList.size() > 0){
                        String lastContent = contentList.get(contentList.size() - 1).getData().getContent();
                        if(StringUtil.levenshtein(content,lastContent) > 0.5f){
                            return;
                        }
                    }
                    contentList.add(currentContent);
                }
            }
        });
        StringBuffer stringBuffer = new StringBuffer();
        contentList.forEach( v -> {
            String contentMD5 = null;
            try {
                contentMD5 = StringUtil.getMd5(v.getData().getContent());
            } catch (NoSuchAlgorithmException e) {
                log.error("pushJin10Content md5 转换失败:" + v.getData().getContent());
            }
            // 防止重复发送数据
            if(redisTemplate.opsForSet().members(RedisKeyConstants.JIN_SHI_PUSH_DATA_SET).contains(contentMD5)){
                return;
            }else {
                redisTemplate.opsForSet().add(RedisKeyConstants.JIN_SHI_PUSH_DATA_SET,contentMD5);
            }
            String content = "<h3>北京时间:" + DateTimeFormatter.ofPattern("yyyy日MM月dd日 HH时mm分").format(v.getTime()) + "</h3>"
                    + v.getData().getContent()+ "<br>";
            stringBuffer.append(content);

            if(!StringUtil.isBlank(v.getData().getPic())){
                String pic = "<img src=\""+ v.getData().getPic()+ "\" />";
                stringBuffer.append(pic);
            }

        });
        if(stringBuffer.length() == 0){
            return;
        }
        emails.stream().forEach( email -> {
            messageService.sendMimeMessage(email.getEmail(), "每日资讯", stringBuffer.toString());
        });

    }

    public void fillIndexData(){
        String[][] arr = stockService.getDayData("sh000001",LocalDate.of(2022,1,1),null,0);
        List<DailyIndexData> list = new ArrayList<>();
        BigDecimal lastDayClose = null;
        for (int i = 0; i < arr.length; i++) {
            DailyIndexData one = new DailyIndexData();
            one.setDate(LocalDate.parse(arr[i][0]));
            one.setIndexData(new BigDecimal(arr[i][2]));
            if(lastDayClose != null){
                one.setIndexGrowth((new BigDecimal(arr[i][2]).subtract(lastDayClose).multiply(new BigDecimal(100)).divide(lastDayClose,2,RoundingMode.HALF_UP)));
            }
            lastDayClose = new BigDecimal(arr[i][2]);
            list.add(one);
        }
        iDailyIndexDataService.updateBatchById(list);
    }


    public void getNorthFunds(){
        Headers.Builder headersBuild = new Headers.Builder();
        headersBuild.add("x-app-id","SRvUdAEfVxLO5NAd");
        headersBuild.add("x-version","1.0.1");

        String str = null;
        try {
            str = okHttpComponent.getWithHeaders("https://search-open-api.jin10.com/search?page=1&page_size=10&order=1&type=flash&keyword=北向资金今日", headersBuild.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        str = str.replaceAll("金十","");
        Jin10SearchResponse<Jin10SearchData> data = JSONObject.parseObject(str, new TypeReference<Jin10SearchResponse<Jin10SearchData>>(){});
        data.getData().getList().forEach( currentContent -> {
            if(currentContent.getData() != null ){
                String content = currentContent.getData().getContent();

                if(content.contains("北向资金今日") || content.contains("北向资金全天")){
                    DailyIndexData northFundData = new DailyIndexData();
                    northFundData.setDate(currentContent.getTime().toLocalDate());
                    List<BigDecimal> numList = StringUtil.getNumStrList(content);
                    if(!numList.isEmpty()){
                        if(content.contains("净买入")){
                            northFundData.setNorthFundAmount(numList.get(0));
                        }else {
                            northFundData.setNorthFundAmount(numList.get(0).negate());
                        }
                    }
                    iDailyIndexDataService.updateDailyData(northFundData);
                }
            }
        });

    }

    public void getMainFunds(){
        Headers.Builder headersBuild = new Headers.Builder();
        headersBuild.add("x-app-id","SRvUdAEfVxLO5NAd");
        headersBuild.add("x-version","1.0.1");
        String str = null;
        try {
            str = okHttpComponent.getWithHeaders("https://search-open-api.jin10.com/search?page=1&page_size=10&order=1&type=flash&keyword=主力资金", headersBuild.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Jin10SearchResponse<Jin10SearchData> data = JSONObject.parseObject(str, new TypeReference<Jin10SearchResponse<Jin10SearchData>>(){});
        data.getData().getList().forEach( currentContent -> {
            if(currentContent.getData() != null ){
                String content = currentContent.getData().getContent();
                if(content.contains("沪深两市主力")){
                    DailyIndexData mainFundData = new DailyIndexData();
                    mainFundData.setDate(currentContent.getTime().toLocalDate());
                    List<BigDecimal> numList = StringUtil.getNumStrList(content);
                    String[] contentArr = content.split("；");
                    for (int i = 0; i < contentArr.length; i++) {
                        if(i == 0 && numList.size() >= 2){
                            if(contentArr[i].contains("入")){
                                mainFundData.setMainFundAmount(numList.get(0));
                            }else {
                                mainFundData.setMainFundAmount(numList.get(0).negate());
                            }
                            mainFundData.setMainFundProportion(numList.get(1));
                        }

                        if(i == 2 && numList.size() >= 6){
                            if(contentArr[i].contains("入")){
                                mainFundData.setSubFundAmount(numList.get(4));
                            }else {
                                mainFundData.setSubFundAmount(numList.get(4).negate());
                            }
                            mainFundData.setSubFundProporion(numList.get(5));
                        }
                    }

                    iDailyIndexDataService.updateDailyData(mainFundData);
                }

            }
        });

    }


}
