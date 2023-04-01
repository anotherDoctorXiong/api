package club.doctorxiong.api.common;




import club.doctorxiong.api.common.request.FundRankRequest;
import club.doctorxiong.api.common.request.StockRankRequest;

import java.time.LocalDate;

/**
 * redis key统一管理，防止冲突
 * @author XiongXin
 * @date 2021/12/6
 * @menu
 */
public class RedisKeyConstants {


    /**
     * token限制
     */
    public final static String USER_TOKEN = "token:%s";

    /**
     * ip限制
     */
    public final static String IP_ADDRESS = "token:%s";

    /**
     * 总访问次数
     */
    public final static String TOTAL_VISIT_TIMES = "total_visit_times";

    public static String getUserTokenRedisKey(String token){
        return String.format(USER_TOKEN,token);
    }

    public static String getIpLimitRedisKey(String ip){
        return String.format(IP_ADDRESS,ip);
    }


    /**
     * 节假日判断缓存
     */
    public final static String HOLIDAY_CACHE_KEY = "holiday_cache:%s";

    /**
     * 基金估算缓存
     */
    public final static String FUND_EXPECT_KEY = "fund_expect:%s";

    /**
     * 基金详情缓存
     */
    public final static String FUND_DETAIL_KEY = "fund_detail:%s";

    /**
     * 基金仓位缓存
     */
    public final static String FUND_POSITION_KEY = "fund_position:%s";

    /**
     * 全部基金
     */
    public final static String ALL_FUND = "all_fund";

    /**
     * 排行榜缓存
     */
    public final static String RANK_KEY = "rank:%s";


    /**
     * 板块排行
     */
    public final static String INDUSTRY_RANK = "industry_rank";

    /**
     * 金十数据
     */
    public final static String JIN_SHI_PUSH_DATA_SET = "jin_shi_push_data";



    /**
     * 日期的偏移量,用于刷新内存的数据,部分场景替代redis过期
     */
    public static LocalDate ALL_STOCK_DATE_OFF_SET = LocalDate.now();

    /**
     * 日期的偏移量,用于刷新内存的数据,部分场景替代redis过期
     */
    public static LocalDate CONVERT_BOND_DATE_OFF_SET = LocalDate.now();




    public static String getHolidayCacheKey(LocalDate date){
        return String.format(HOLIDAY_CACHE_KEY,date.toString());
    }

    public static String getFundExpectKey(String fundCode){
        return String.format(FUND_EXPECT_KEY,fundCode);
    }

    public static String getFundDetailKey(String fundCode){
        return String.format(FUND_DETAIL_KEY,fundCode);
    }

    public static String getFundPositionKey(String fundCode){
        return String.format(FUND_POSITION_KEY,fundCode);
    }

    public static String getFundRankKey(FundRankRequest request){
        return String.format(RANK_KEY,request.toString());
    }

    public static String getStockRankKey(StockRankRequest request){
        return String.format(RANK_KEY,request.toString());
    }


}
