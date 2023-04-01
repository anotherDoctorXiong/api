package club.doctorxiong.api.uitls;




import club.doctorxiong.api.common.request.StockRankRequest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Auther: 熊鑫
 * @Date: 2019/6/9 17
 * @Description: 动态合成各种所需的url
 */
public class UrlUtil {


    /**
     * @param fund_code
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/9 17:22
     * @Description: 获取基金最新的估算信息的url
     * 实例：http://fundgz.1234567.com.cn/js/519933.js?rt=1560071822744
     * rt是unix时间戳
     */
    public static String getFundExportUrl(String fund_code) {
        return "http://fundgz.1234567.com.cn/js/" + fund_code + ".js";
    }

    /**
     * @param fund_code
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/9 17:32
     * @Description: 获取基金详细信息的url请求
     * 实例：http://fund.eastmoney.com/pingzhongdata/519933.js?v=20190609171217
     * 备用url：http://finance.sina.com.cn/fund/api/xh5Fund/nav/006210.js
     * v时间字符串
     */
    public static String getFundDetailUrl(String fund_code) {
        return "http://fund.eastmoney.com/pingzhongdata/" + fund_code + ".js";
    }

    /**
     * @param
     * @name: getFundRankUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/26 11:10
     * @description: 基金排行的url
     */
    public static String getFundRankBaseUrl() {
        // 旧的链接,收盘时数据会失效
        // "http://fund.eastmoney.com/data/FundGuideapi.aspx?dt=0&";

        return "http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&dx=1&";
    }

    /**
     * @param
     * @name: getAllFundUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/7/23 20:37
     * @description: 获取所有基金
     */
    public static String getAllFundUrl() {
        return "http://fund.eastmoney.com/js/fundcode_search.js";
    }


    /**
     * @param
     * @name: getStockRankBaseUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/7/18 10:26
     * @description: 股票排行的数据来源
     * http://stock.gtimg.cn/data/view/rank.php?t=rankash/buy1&p=1&o=0&l=40&v=list_data
     */
    public static String getStockRankBaseUrl() {
        return "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?";
    }


    /**
     * @param
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/6/29 10:49
     * @description: 获取已经排序的板块代码list
     */
    public static String getIndustryRankUrl(Integer pageIndex, Integer pageSize) {
        return "http://stock.gtimg.cn/data/view/bdrank.php?&t=01/averatio&p=" + pageIndex + "&o=0&l=" + pageSize + "&v=list_data";
    }

    /**
     * @param str
     * @name: getIndustryRankDetailUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/7/16 17:12
     * @description: 返回各板块的详细信息, 如涨幅, 名称, 代码, 成交额
     */
    public static String getIndustryCodeUrl(String str) {
        return "http://qt.gtimg.cn/q=" + str;
    }

    public static String getSWUrl() {
        return "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodes";
    }

    /**
     * @name: getIndustryDetailUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/7/16 17:28
     * @description: 获得板块的股票代码
     * http://stock.gtimg.cn/data/view/rank.php?t=rankash/buy1&p=1&o=0&l=40&v=list_data
     * http://stock.gtimg.cn/data/index.php?appn=rank&t=ranka/chr&p=1&o=0&l=40&v=list_data
     * <p>
     * http://stock.gtimg.cn/data/index.php?appn=rank&t=pt021055/buy1&p=1&o=0&l=40&v=list_data
     */

    public static String getSinaStockRankUrl(StockRankRequest request) {
        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=" + request.getPageIndex() + "&num=" +
                request.getPageSize() + "&sort=" + request.getSort() + "&asc=" + request.getAsc() + "&node=" + request.getNode() + "&symbol=";
        return url;
    }

    public static String getSinaStockCountUrl(String node) {
        return "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeStockCount?node=" + node;
    }


    /**
     * @param request
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/9 17:07
     * @Description: 通过反射为当前对象构建get请求的url
     */
    public static String creatGetUrlWithParams(String baseUrl, Object request) {
        StringBuffer newuri = new StringBuffer(baseUrl);
        Field[] field = request.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String fieldName = field[i].getName();//字段名称
            //有些类添加了日志注解,跳过log属性
            if ("log".equals(fieldName)) {
                continue;
            }
            String methName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Object value = null;
            try {
                Method m = request.getClass().getMethod("get" + methName);
                value = m.invoke(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (value instanceof String) {
                if (value != null) {
                    newuri.append(fieldName + "=" + value + "&");
                }
            } else if (value instanceof Integer) {
                if (Integer.valueOf(value.toString()) != 0) {
                    newuri.append(fieldName + "=" + value + "&");
                }
            }

        }
        return newuri.toString();
    }


    public static String getFuturesMenu() {
        return "http://quote.eastmoney.com/api/PageConfigs/sidemenus.json";
    }

    /**
     * @param code
     * @name: getFundPostionHTMLUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/5/26 14:51
     * @description: 基金持仓股票
     */
    public static String getHTMLUrl(String code) {
        return "http://fundf10.eastmoney.com/FundArchivesDatas.aspx?type=jjcc&code=" + code + "&topline=20";
    }

    /**
     * @param code
     * @name: getHTMLUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/5/26 14:51
     * @description: 基金持仓配置
     */
    public static String getHTMLUrl1(String code) {
        return "http://fundf10.eastmoney.com/zcpz_" + code + ".html";
    }

    /**
     * @param
     * @name: getHTMLUrl2
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/7/23 15:38
     * @description: 获取基金公司代码的html
     */
    public static String getHTMLUrl2() {
        return "http://fund.eastmoney.com/daogou/";
    }

    /**
     * @param
     * @name: getStockDate
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/23 21:39
     * @description: 某年的日K
     */
    public static String getStockDetailUrl(String stockCode, int year) {
        return "http://data.gtimg.cn/flashdata/hushen/daily/" + year + "/" + stockCode + ".js";
    }

    /**
     * @name: getStockShowUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/25 9:22
     * @description: 可以批量处理
     */
    public static String getStockBaseInfoUrl(String stockCodeStr) {
        //return "http://hq.sinajs.cn/list="+stockCode;
        return "http://qt.gtimg.cn/q=" + stockCodeStr;
    }

    /**
     * @param stockCode
     * @name: getStockKLineUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/9/2 14:53
     * @description: 获取startDate后的K线数据
     */
    public static String getStockKLineUrl(String stockCode) {
        //return "http://hq.sinajs.cn/list="+stockCode;
        //return "http://qt.gtimg.cn/q="+stockCodeStr;
        return "http://web.ifzq.gtimg.cn/appstock/app/kline/kline?param=" + stockCode + ",day,,,2000";
    }

    public static String getStockDataUrl(String stockCode) {

        return "http://web.ifzq.gtimg.cn/appstock/app/minute/query?_var=min_data_" + stockCode + "&code=" + stockCode;
    }

    /**
     * @param stockCode
     * @name: getStockKLineQFQUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/9/2 14:54
     * @description: 获取前复权的K线数据
     */
    public static String getStockKLineQFQUrl(String stockCode) {
        //return "http://hq.sinajs.cn/list="+stockCode;
        //return "http://qt.gtimg.cn/q="+stockCodeStr;
        return "http://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param=" + stockCode + ",day,,,2000,qfq";
    }

    /**
     * @param stockCode
     * @name: getStockKLineHFQUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/9/2 15:03
     * @description: 获取后复权K线数据
     */
    public static String getStockKLineHFQUrl(String stockCode) {
        //return "http://hq.sinajs.cn/list="+stockCode;
        //return "http://qt.gtimg.cn/q="+stockCodeStr;
        return "http://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param=" + stockCode + ",day,,,2000,hfq";
    }

    /**
     * @param
     * @name: getStockRank
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/23 22:14
     * @description: 股票排行
     */
    public static String getStockRank() {
        return "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData";
    }

    /**
     * @param stockCode
     * @name: getDayMinutelyDataUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/25 15:32
     * @description:
     */
    public static String getDayMinutelyDataUrl(String stockCode) {
        return "http://data.gtimg.cn/flashdata/hushen/minute/" + stockCode + ".js";
    }

    /**
     * @param
     * @name: getAllStock
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/8/25 15:52
     * @description: 获取所有股票
     */
    public static String getAllStockUrl() {
        return "http://api.jinshuyuan.net/get_stk_dic";
    }


    /**
     * @param
     * @name: getAllStockUrl
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/8/25 22:09
     * @description: 获取所有指数
     */
    public static String getAllIndexUrl() {
        return "http://api.jinshuyuan.net/get_idx_dic";
    }


    public static String getAllInnerFund() {
        return "https://www.jisilu.cn/data/etf/etf_list/?page=1";
    }

    public static String getJin10Data(LocalDateTime indexTime) {
        return "https://classify-ws.jin10.com:5142/flash?channel=-8200&vip=1&max_time=" + indexTime + "&classify=[54]";
    }

    /**
     * 可转债列表
     *
     * @return java.lang.String
     */
    public static String getConvertBondRankUrl(Integer pageIndex) {
        return "https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns=PUBLIC_START_DATE&sortTypes=-1&pageSize=500&pageNumber=" + pageIndex + "&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteType=0" +
                "&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE,f235~10~SECURITY_CODE~TRANSFER_PRICE,f236~10~SECURITY_CODE~TRANSFER_VALUE,f2~10~SECURITY_CODE~CURRENT_BOND_PRICE,f237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO";
    }
}

