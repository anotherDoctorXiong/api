package club.doctorxiong.api.common.dto;


import club.doctorxiong.api.common.LocalDateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import club.doctorxiong.api.uitls.StringUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;




/**
 * @Auther: 熊鑫
 * @Date: 2020/9/4 15
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO extends CacheDTO implements Serializable {
    //股票代码
    private String code;
    //股票名称
    private String name;
    //指数和股票
    private String type;
    //开盘后价格变化
    private String priceChange;
    //开盘后价格变化幅度
    private String changePercent;
    //开盘价
    private String open;
    //昨日收盘价
    private String close;
    //当前价格
    private String price;
    //当日最高价
    private String high;
    //当日最低价
    private String low;
    //成交量手
    private String volume;
    //成交量手
    private String volumeRate;
    //成交额万
    private String turnover ;
    //换手率
    private String turnoverRate;
    //市盈率
    private String PE;
    //静态市盈率
    private String SPE;
    //市净率
    private String PB;
    //总市值
    private String totalWorth;
    //流通市值
    private String circulationWorth;
    //日期
    private LocalDateTime date;
    //买一到买五
    private String[] buy;
    //卖一到卖五
    private String[] sell;

    private List<String[]> minData;

    private String[][] arrData;

    public StockDTO(String code) {
        this.code = code;
    }

    public void setStockData(String[] arr){
        if(arr.length<50){
            return;
        }
        try {
            this.type=arr[61];
            if(!"ZS".equals(this.type)){
                this.buy= Arrays.copyOfRange(arr,9,19);
                this.sell=Arrays.copyOfRange(arr,19,29);
                this.turnoverRate=arr[38];
                this.PE=arr[39];
                this.totalWorth=arr[45];
                this.circulationWorth=arr[44];
                this.PB=arr[46];
                this.volumeRate=arr[49];
                this.SPE=arr[53];
            }
            this.code= "1".equals(arr[0])?"sh"+arr[2]:"sz"+arr[2];
            this.name=arr[1];
            this.price=arr[3];
            this.close=arr[4];
            this.open=arr[5];
            this.date= LocalDateTimeFormatter.getLocalDateTimeWithStr(arr[30], LocalDateTimeFormatter.FORMAT_ONE);
        } catch (Exception e) {
            // 不需要做什么
        }
        this.priceChange=arr[31];
        this.changePercent=arr[32];
        this.high=arr[33];
        this.low=arr[34];
        this.volume=arr[36];
        this.turnover=arr[37];
        // 科创板的单位是股,暂时手动处理下
        if(this.type != null && this.type.contains("KCB")){
            this.volume = String.valueOf(Integer.valueOf(this.volume)/100 + (Integer.valueOf(this.volume)%100>50?1:0));
            for(int i = 0 ; i < this.buy.length;i++){
                if(i%2==1){
                    this.buy[i] = String.valueOf(Integer.valueOf(this.buy[i])/100 + (Integer.valueOf(this.buy[i])%100>50?1:0));
                }
            }
            for(int j = 0 ; j < this.sell.length;j++){
                if(j%2==1){
                    this.sell[j] = String.valueOf(Integer.valueOf(this.sell[j])/100 + (Integer.valueOf(this.sell[j])%100>50?1:0));
                }
            }
        }
    }


    public boolean invalidStock(){
        return StringUtil.isBlank(this.code) || StringUtil.isBlank(this.name) || StringUtil.isBlank(this.type);
    }

}
