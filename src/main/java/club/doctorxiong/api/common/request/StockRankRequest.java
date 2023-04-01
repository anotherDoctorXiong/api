package club.doctorxiong.api.common.request;

import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: 熊鑫
 * @Date: 2020/7/17 10
 * @Description: 板块的股票排行  page=1&num=15&sort=settlement&asc=0&node=sh_b&symbol=
 */
@ToString
public class StockRankRequest implements Serializable {
    /**
     * 沪深A a  沪A ash  深A  asz  沪B深B
     */
    private String node;
    private String industryCode;
    private Integer pageIndex;
    private Integer pageSize;
    /**
     * 最新价:last  涨幅:chr  涨额:ch  买入价:buy1  卖出价:sell1 成交量:volume  成交额:turnover
     * 换手率:trunr 开盘:open收盘:close最高价:high最低价:low
     */
    private String sort;
    /**
     * 排序方式0:降序  1:升序  -1:不排序
     */
    private Integer asc;

    //默认沪深A股交易额
    public StockRankRequest() {
        this.node="a";
        this.pageIndex = 1;
        this.pageSize = 10;
        this.sort = "turnover";
        this.asc = -1;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        switch (this.sort){
            case "price":
                return "trade";
            case "priceChange":
                return "pricechange";
            case "changePercent":
                return "changepercent";
            case "turnover":
                return "amount";
            default:
                return sort;
        }
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getAsc() {
        return asc;
    }

    public void setAsc(Integer asc) {
        this.asc = asc;
    }

    public String getNode() {
        if(this.getIndustryCode() != null && !"".equals(this.getIndustryCode())){
            return industryCode;
        }
        switch (this.node){
            case "a":
                return "hs_a";
            case "b":
                return "hs_b";
            case "ash":
                return "sh_a";
            case "bsh":
                return "sh_b";
            case "asz":
                return "sz_a";
            case "bsz":
                return "sz_b";
            default:
                return node;
        }
    }

    public void setNode(String node) {
        this.node = node;
    }
}
