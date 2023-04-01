package club.doctorxiong.api.fundfactory;

/**
 * @author XIONGXIN
 * @title: CallMethodEnum
 * @projectName designdemo
 * @description: 工厂入参枚举
 * @date 2021/1/7 16:16
 */
public enum FundOptionalEnum {

    FUND_NAME("fS_name"),
    FUND_CODE("fS_code"),
    FUND_SOURCE_RATE("fund_sourceRate"),
    FUND_RATE("fund_Rate"),
    FUND_MIN_BUY("fund_minsg"),
    FUND_POSITION_STOCK("stockCodesNew"),
    FUND_HB("ishb"),
    FUND_ONE_YEAR("syl_1n"),
    FUND_SIX_MONTHS("syl_6y"),
    FUND_THREE_MONTHS("syl_3y"),
    FUND_ONE_MONTHS("syl_1y"),
    /*股票仓位测算图*/
    FUND_POSITION("Data_fundSharesPositions"),
    /*单位净值走势equityReturn-净值回报unitMoney-每份派送金*/
    FUND_NET_WORTH_DATA("Data_netWorthTrend"),
    /*累计净值走势equityReturn-净值回报unitMoney-每份派送金*/
    FUND_TOTAL_NET_WORTH_DATA("Data_ACWorthTrend"),
    /*现任基金经理*/
    FUND_MANAGER("Data_currentFundManager"),
    /*规模变动mom-较上期环比*/
    FUND_SCALE("Data_fluctuationScale"),
    /*货币基金万分收益*/
    FUND_MILLION_COPIES_INCOME("Data_millionCopiesIncome"),
    /*货币基金七日年化*/
    FUND_SEVEN_DAYS_YEAR_INCOME("Data_sevenDaysYearIncome"),
    ;
    private String optionalName;


    FundOptionalEnum(String optionalName){
        this.optionalName = optionalName;
    }

    public String getOptionalName() {
        return optionalName;
    }
}
