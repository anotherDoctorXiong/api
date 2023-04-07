package club.doctorxiong.api.common.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import org.apache.tomcat.util.buf.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author : 熊鑫
 * @ClassName : FundType
 * @description : 通过筛选获取基金list,变量为基金分类及排行条件
 * @date : 2019/6/4 14:42
 */
@ToString
public class FundRankRequest {

    /**
     * 基金类型(股票gp指数zf混合hh债券zqDQIIdqiiFOFfof)
     * */
    @JsonProperty("fundType")
    private List<String> ft;
    /**
     * 基金增长率(日r周z月y年n)3月增长率3y
     * */
    @JsonProperty("sort")
    private String sc;
    /**
     * 基金公司(嘉实易方达等)
     */
    private List<String> cp;
    /**
     * 成立年限单位年
     * */
    @JsonProperty("creatTimeLimit")
    private int nx;
    /**
     * 基金规模单位亿其中大于100亿为1001
     */
    @JsonProperty("fundScale")
    private int se;
    /**
     * 排序方式desc降序asc升序
     * */
    @JsonProperty("asc")
    private String st;
    /**
     * index起始位置
     * */
    @JsonProperty("pageIndex")
    private int pi;
    /**
     * pageSize
     * */
    @JsonProperty("pageSize")
    private int pn;

    public FundRankRequest() {
        this.st = "desc";
        this.pi = 1;
        this.pn = 10;
        this.sc = "r";
    }


    /**
     * @param
     * @name: getCp
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/23 11:57
     * @description: 获取的公司列表直接转为http://fund.eastmoney.com/data/FundGuideapi.aspx所接受的参数格式
     * 所接受的参数类型参考FundCompany
     */
    public String getCp() {
        return StringUtils.join(this.cp, ',');
    }

    /**
     * @param
     * @name: getFt
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/23 12:02
     * @description: 获取的基金种类列表直接转为http://fund.eastmoney.com/data/FundGuideapi.aspx所接受的参数格式
     * 所接受的参数类型参考FundType
     */

    public String getFt() {
        return StringUtils.join(this.ft, ',');
    }

    public void setFt(List<String> ft) {
        this.ft = ft;
    }

    public String getSc() {
        switch (this.sc){
            case "r":
                return "rzdf";
            case "z":
                return "zzf";
            default:
                return this.sc+"zf";
        }

    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public void setCp(List<String> cp) {
        this.cp = cp;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getSe() {
        return se;
    }

    public void setSe(int se) {
        this.se = se;
    }

    public String getSt() {
        return st;
    }

    public void setSt(int st) {
        if (st == 0) {
            this.st = "desc";
        } else if(st == 1){
            this.st = "asc";
        }else {
            this.st = null;
        }

    }

    public int getPi() {
        return pi;
    }

    public void setPi(int pi) {
        this.pi = pi;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    /**
     *
     */
    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }
}
