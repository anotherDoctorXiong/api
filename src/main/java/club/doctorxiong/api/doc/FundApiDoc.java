package club.doctorxiong.api.doc;

/**
 * @Auther: 熊鑫
 * @Date: 2019/6/27 14
 * @Description: 生成api文档
 */
public class FundApiDoc {

    /**
     * @apiGroup Fund
     * @api {get} /v1/fund 获取基金基础信息
     * @apiName fund
     * @apiDescription  支持批量查询,不包含历史数据
     * @apiParam {String} code 基金代码(必填)逗号隔开支持多个查询
     * @apiParamExample {String} 请求示例
     * /v1/fund?code=202015,007339
     * @apiSuccessExample {json} 返回示例
     *{
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         {
     *             "code": "202015",
     *             "name": "南方沪深300联接基金A",
     *             "type": "指数型",
     *             "netWorth": 1.51,
     *             "expectWorth": 1.5095,
     *             "expectGrowth": "-0.31",
     *             "dayGrowth": "-0.2774",
     *             "lastWeekGrowth": "0.1828",
     *             "lastMonthGrowth": "3.0998",
     *             "lastThreeMonthsGrowth": "-1.6158",
     *             "lastSixMonthsGrowth": "1.5535",
     *             "lastYearGrowth": "8.6409",
     *             "netWorthDate": "2020-05-15",
     *             "expectWorthDate": "2020-05-15 15:00:00"
     *         },
     *         {
     *             "code": "007339",
     *             "name": "易方达沪深300ETF联接C",
     *             "netWorth": 1.3676,
     *             "expectWorth": 1.3673,
     *             "expectGrowth": "-0.30",
     *             "dayGrowth": "-0.2771",
     *             "lastWeekGrowth": "0.1828",
     *             "lastMonthGrowth": "3.0906",
     *             "lastThreeMonthsGrowth": "-1.6256",
     *             "lastSixMonthsGrowth": "1.2663",
     *             "lastYearGrowth": "6.9942",
     *             "netWorthDate": "2020-05-15",
     *             "expectWorthDate": "2020-05-15 15:00:00"
     *         }
     *     ],
     *     "meta": "202015,007339"
     * }
     * @apiSuccess (返回字段描述) {String} code 基金代码
     * @apiSuccess (返回字段描述) {String} name 基金名称
     * @apiSuccess (返回字段描述) {Number} netWorth 当前基金单位净值
     * @apiSuccess (返回字段描述) {Number} expectWorth 当前基金单位净值估算
     * @apiSuccess (返回字段描述) {String} expectGrowth 当前基金单位净值估算日涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} dayGrowth 单位净值日涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastWeekGrowth 单位净值周涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastWeekGrowth 单位净值周涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastMonthGrowth 单位净值月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastThreeMonthsGrowth 单位净值三月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastSixMonthsGrowth 单位净值六月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastYearGrowth 单位净值年涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} netWorthDate 净值更新日期,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiSuccess (返回字段描述) {String} expectWorthDate 净值估算更新日期,,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiError (错误码) 400 解析请求失败
     * @apiError (错误码) 405 无效的基金代码
     * @apiError (错误码) 500 内部网络异常
     * @apiSampleRequest https://api.doctorxiong.club/v1/fund
     */


    /**
     * @apiGroup Fund
     * @api {get} /v1/fund/detail 获取基金详情
     * @apiName getFundDetail
     * @apiDescription  通过基金代码获取基金详情,包含基金历史变化信息,货币基金的数据与其他基金不同,获取数据后应先判断基金类型,再渲染数据
     * @apiParam {String} code 基金代码(必填)
     * @apiParam {Date} startDate 开始时间,标准时间格式yyyy-MM-dd
     * @apiParam {Date} endDate 截至时间,标准时间格式yyyy-MM-dd
     * @apiParamExample {String} 请求示例
     * /v1/fund/detail?code=000001&startDate=2020-09-01
     * @apiSuccessExample {json} 返回示例(非货币基金)
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "code": "000001",
     *         "name": "华夏成长",
     *         "type": "混合型",
     *         "netWorth": 1.09,
     *         "expectWorth": 1.104,
     *         "totalWorth": 3.501,
     *         "expectGrowth": "1.29",
     *         "dayGrowth": "0.1838",
     *         "lastWeekGrowth": "0.1828",
     *         "lastMonthGrowth": "3.02",
     *         "lastThreeMonthsGrowth": "-0.55",
     *         "lastSixMonthsGrowth": "18.87",
     *         "lastYearGrowth": "3.12",
     *         "buyMin": "100",
     *         "buySourceRate": "1.5",
     *         "buyRate": "0.15",
     *         "manager": "董阳阳",
     *         "fundScale": "49.91亿(2019-03-31)",
     *         "netWorthDate": "2019-06-26",
     *         "expectWorthDate": "2019-06-27 15:00",
     *         "netWorthData":[
     *         	    [
     *                 "2001-12-18",
     *                 1,
     *                 0,
     *                 ""
     *             ],
     *             [
     *                 "2001-12-21",
     *                 1,
     *                 0,
     *                 ""
     *             ]
     *         ]
     *     },
     *     "meta": "000001"
     * }
     * @apiSuccessExample {json} 返回示例(货币基金)
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "code": "003171",
     *         "name": "信达澳银慧理财货币",
     *         "type": "货币型",
     *         "lastMonthGrowth": "0.1",
     *         "lastThreeMonthsGrowth": "0.28",
     *         "lastSixMonthsGrowth": "0.7",
     *         "lastYearGrowth": "1.98",
     *         "buyMin": 100,
     *         "buySourceRate": 0,
     *         "buyRate": 0,
     *         "manager": "孔学峰",
     *         "fundScale": "0.20亿(2019-03-31)",
     *         "millionCopiesIncomeData": [
     *             [
     *                 "2016-09-23",
     *                 0.4773
     *             ],
     *             [
     *                 "2016-09-25",
     *                 0.9546
     *             ]
     *     	    ],
     *         "millionCopiesIncomeDate": "2019-06-27 00:00",
     *         "sevenDaysYearIncome": 1.073,
     *         "sevenDaysYearIncomeDate": [
     *             [
     *                 "2016-09-23",
     *                 2.131
     *             ],
     *             [
     *                 "2016-09-25",
     *                 1.706
     *             ]
     *         ]
     *     },
     *     "meta": "003171"
     * }
     * @apiSuccess (返回字段描述) {String} code 基金代码
     * @apiSuccess (返回字段描述) {String} name 基金名称
     * @apiSuccess (返回字段描述) {String} type 基金类型
     * @apiSuccess (返回字段描述) {Number} netWorth 当前基金单位净值
     * @apiSuccess (返回字段描述) {Number} expectWorth 当前基金单位净值估算
     * @apiSuccess (返回字段描述) {Number} totalWorth 当前基金累计净值
     * @apiSuccess (返回字段描述) {String} expectGrowth 当前基金单位净值估算日涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} dayGrowth 单位净值日涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastWeekGrowth 单位净值周涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastWeekGrowth 单位净值周涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastMonthGrowth 单位净值月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastThreeMonthsGrowth 单位净值三月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastSixMonthsGrowth 单位净值六月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastYearGrowth 单位净值年涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {Number} buyMin 起购额度
     * @apiSuccess (返回字段描述) {Number} buySourceRate 原始买入费率,单位为百分比
     * @apiSuccess (返回字段描述) {Number} buyRate 当前买入费率,单位为百分比
     * @apiSuccess (返回字段描述) {String} manager 基金经理
     * @apiSuccess (返回字段描述) {String} fundScale 基金规模及日期,日期为最后一次规模变动的日期
     * @apiSuccess (返回字段描述) {String} worthDate 净值更新日期,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiSuccess (返回字段描述) {String} expectWorthDate 净值估算更新日期,,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiSuccess (返回字段描述) {String[][]} netWorthData 历史净值信息["2001-12-18" , 1 , 0 , ""]依次表示:日期; 单位净值; 净值涨幅; 每份分红.
     * @apiSuccess (返回字段描述) {Number} millionCopiesIncome 每万分收益(货币基金)
     * @apiSuccess (返回字段描述) {String[][]} millionCopiesIncomeData 历史万每分收益信息(货币基金)["2016-09-23",0.4773]依次表示:日期; 每万分收益.
     * @apiSuccess (返回字段描述) {String} millionCopiesIncomeDate 七日年化收益更新日期(货币基金)
     * @apiSuccess (返回字段描述) {Number} sevenDaysYearIncome 七日年化收益(货币基金)
     * @apiSuccess (返回字段描述) {String[][]} sevenDaysYearIncomeData 历史七日年华收益信息(货币基金)["2016-09-23",2.131]依次表示:日期; 七日年化收益.
     * @apiError (错误码) 400 解析请求失败
     * @apiError (错误码) 405 无效的基金代码
     * @apiError (错误码) 500 内部网络异常
     * @apiSampleRequest https://api.doctorxiong.club/v1/fund/detail
     */


    /**
     * @apiGroup Fund
     * @api {get} /v1/fund/detail/list 批量获取基金详情
     * @apiName getFundDetailList
     * @apiDescription  通过基金代码获取基金详情,包含基金历史变化信息,货币基金的数据与其他基金不同,获取数据后应先判断基金类型,再渲染数据
     * @apiParam {String} code 基金代码(必填,多个用逗号隔开)
     * @apiParam {Date} startDate 开始时间,标准时间格式yyyy-MM-dd
     * @apiParam {Date} endDate 截至时间,标准时间格式yyyy-MM-dd
     * @apiParamExample {String} 请求示例
     * /v1/fund/detail?code=000001,000002&startDate=2020-09-01
     * @apiSuccessExample {json} 返回示例(货币基金)
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         {
     *            "code": "003171",
     *            "name": "信达澳银慧理财货币",
     *            "type": "货币型",
     *            "lastMonthGrowth": "0.1",
     *            "lastThreeMonthsGrowth": "0.28",
     *            "lastSixMonthsGrowth": "0.7",
     *            "lastYearGrowth": "1.98",
     *            "buyMin": 100,
     *            "buySourceRate": 0,
     *            "buyRate": 0,
     *            "manager": "孔学峰",
     *            "fundScale": "0.20亿(2019-03-31)",
     *            "millionCopiesIncomeData": [
     *                [
     *                    "2016-09-23",
     *                    0.4773
     *                ],
     *                [
     *                    "2016-09-25",
     *                    0.9546
     *                ]
     *       	     ],
     *            "millionCopiesIncomeDate": "2019-06-27 00:00",
     *            "sevenDaysYearIncome": 1.073,
     *            "sevenDaysYearIncomeDate": [
     *                [
     *                    "2016-09-23",
     *                    2.131
     *                ],
     *                [
     *                    "2016-09-25",
     *                    1.706
     *                ]
     *            ]
     *         }
     *     ],
     *     "meta": "003171"
     * }
     * @apiSuccess (返回字段描述) {String} code 基金代码
     * @apiSuccess (返回字段描述) {String} name 基金名称
     * @apiSuccess (返回字段描述) {String} type 基金类型
     * @apiSuccess (返回字段描述) {Number} netWorth 当前基金单位净值
     * @apiSuccess (返回字段描述) {Number} expectWorth 当前基金单位净值估算
     * @apiSuccess (返回字段描述) {Number} totalWorth 当前基金累计净值
     * @apiSuccess (返回字段描述) {String} expectGrowth 当前基金单位净值估算日涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} dayGrowth 单位净值日涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastWeekGrowth 单位净值周涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastWeekGrowth 单位净值周涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastMonthGrowth 单位净值月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastThreeMonthsGrowth 单位净值三月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastSixMonthsGrowth 单位净值六月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastYearGrowth 单位净值年涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {Number} buyMin 起购额度
     * @apiSuccess (返回字段描述) {Number} buySourceRate 原始买入费率,单位为百分比
     * @apiSuccess (返回字段描述) {Number} buyRate 当前买入费率,单位为百分比
     * @apiSuccess (返回字段描述) {String} manager 基金经理
     * @apiSuccess (返回字段描述) {String} fundScale 基金规模及日期,日期为最后一次规模变动的日期
     * @apiSuccess (返回字段描述) {String} worthDate 净值更新日期,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiSuccess (返回字段描述) {String} expectWorthDate 净值估算更新日期,,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiSuccess (返回字段描述) {String[][]} netWorthData 历史净值信息["2001-12-18" , 1 , 0 , ""]依次表示:日期; 单位净值; 净值涨幅; 每份分红.
     * @apiSuccess (返回字段描述) {Number} millionCopiesIncome 每万分收益(货币基金)
     * @apiSuccess (返回字段描述) {String[][]} millionCopiesIncomeData 历史万每分收益信息(货币基金)["2016-09-23",0.4773]依次表示:日期; 每万分收益.
     * @apiSuccess (返回字段描述) {String} millionCopiesIncomeDate 七日年化收益更新日期(货币基金)
     * @apiSuccess (返回字段描述) {Number} sevenDaysYearIncome 七日年化收益(货币基金)
     * @apiSuccess (返回字段描述) {String[][]} sevenDaysYearIncomeData 历史七日年华收益信息(货币基金)["2016-09-23",2.131]依次表示:日期; 七日年化收益.
     * @apiError (错误码) 400 解析请求失败
     * @apiError (错误码) 405 无效的基金代码
     * @apiError (错误码) 500 内部网络异常
     * @apiSampleRequest https://api.doctorxiong.club/v1/fund/detail/list
     */







    /**
     * @apiGroup Fund
     * @api {post} /v1/fund/rank 获取基金排行
     * @apiName getFundRank
     * @apiDescription  根据条件获取基金排行(包含基本基金信息),不合法的参数会被直接忽略。
     * @apiParam {String[]={"gp","hh","zq","zs","qdii","fof"}} fundType=所有类型 基金gp(股票型),hh(混合型),zq(债券型),zs(指数型)(可以接受多个参数)
     * @apiParam {String={"r","z","1y","3y","6y","jn","1n","2n","3n"}} sort=日涨幅 r(日涨幅) z(周涨幅),1y(最近一个月涨幅),jn(今年涨幅),1n(近一年涨幅)
     * @apiParam {String[]={"80000222","80000223","80000229","80000220","80048752","80000248","80064225","80000226","80000228","80053708"}} fundCompany=所有基金公司 华夏("80000222"), 嘉实("80000223"), 易方达("80000229"),南方("80000220"), 中银("80048752"), 广发("80000248"),
     * 工银("80064225"), 博时("80000226"), 华安("80000228"),汇添富("80053708");(接受多个参数)
     * @apiParam {Number} creatTimeLimit=无限制 基金成立时间限制1:小于一年》2:小于两年.依次类推(非必需)
     * @apiParam {Number=[10,100,1001]} fundScale 基金规模单位亿,10表示10亿以下,100表示100亿以下,1001表示100亿以上(非必需)
     * @apiParam {Number={0，1}} asc=0 排序方式0:降序:1升序
     * @apiParam {Number} pageIndex=1 页码
     * @apiParam {Number} pageSize=10 每页条目数
     * @apiParamExample {json} 请求示例1
     * 获取嘉实通过上证五星认证的指数型基金,按一周的涨幅排行,获取第一页的10条数据。
     * Content-Type: application/json
     * {
     * 	"fundType": ["zs"],
     * 	"sort":"z",
     * 	"fundCompany":["80000248"],
     * 	"pageIndex":1,
     * 	"pageSize":10
     * }
     * @apiParamExample {json} 请求示例2
     * 获取多种类型,多个公司的基金,按一周的涨幅排行。
     * Content-Type: application/json
     * {
     * 	"fundType": ["zs","gp"],
     * 	"sort":"z",
     * 	"fundCompany":["80000226","80000248"],
     * }
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "pageIndex": 1,
     *         "pageSize": 2,
     *         "allPages": 3156,
     *         "rank": [
     *             {
     *                 "code": "980003",
     *                 "name": "太平洋六个月滚动持有债",
     *                 "netWorth": "1.4019",
     *                 "netWorthDate": "2020-07-22",
     *                 "fundType": "债券型",
     *                 "dayGrowth": "0.0285",
     *                 "lastWeekGrowth": "0.2503",
     *                 "lastMonthGrowth": "0.2144",
     *                 "lastThreeMonthsGrowth": "",
     *                 "lastSixMonthsGrowth": "",
     *                 "lastYearGrowth": "",
     *                 "thisYearGrowth": ""
     *             },
     *             {
     *                 "code": "970005",
     *                 "name": "安信瑞鸿中短债C",
     *                 "netWorth": "1.0229",
     *                 "netWorthDate": "2020-07-21",
     *                 "fundType": "债券型",
     *                 "dayGrowth": "0.0293",
     *                 "lastWeekGrowth": "0.1175",
     *                 "lastMonthGrowth": "-0.0293",
     *                 "lastThreeMonthsGrowth": "",
     *                 "lastSixMonthsGrowth": "",
     *                 "lastYearGrowth": "",
     *                 "thisYearGrowth": ""
     *             }
     *         ]
     *     },
     *     "meta": null
     * }
     * @apiSuccess (返回字段描述) {String} code 基金代码
     * @apiSuccess (返回字段描述) {String} name 基金名称
     * @apiSuccess (返回字段描述) {String} fundType 基金类型
     * @apiSuccess (返回字段描述) {String} netWorth 当前基金单位净值
     * @apiSuccess (返回字段描述) {String} netWorth 单位净值更新日期
     * @apiSuccess (返回字段描述) {String} dayGrowth 日涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastWeekGrowth 最近一周涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastMonthGrowth 最近一个月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastThreeMonthsGrowth 最近三个月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastSixMonthsGrowth 最近六个月涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} lastYearGrowth 最近一年涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {String} thisYearGrowth 今年的涨幅,单位为百分比
     * @apiSuccess (返回字段描述) {Number} pageIndex 页码
     * @apiSuccess (返回字段描述) {Number} pageSize 每页的条目数
     * @apiSuccess (返回字段描述) {Number} allPages 最大页数
     * @apiError (错误码) 202 没有符合条件的基金
     * @apiError (错误码) 400 解析请求失败,一般是参数错误
     * @apiError (错误码) 500 内部网络异常
     */




    /**
     * @apiGroup Fund
     * @api {get} /v1/fund/position 获取基金持仓详情
     * @apiName getFundPosition
     * @apiDescription  获取基金持仓数据
     * @apiParam {String} code 基金代码(必填)
     * @apiParamExample {String} 请求示例
     * /v1/fund/position?code=003634
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "title": "嘉实农业产业股票  2020年1季度股票投资明细",
     *         "date": "2020-03-31",
     *         "stock": "80.30%",
     *         "bond": "2.93%",
     *         "cash": "5.77%",
     *         "total": "4.84",
     *         "stockList": [
     *             [
     *                 "002311",
     *                 "海大集团",
     *                 "8.00%",
     *                 "96.35",
     *                 "3,873.36"
     *             ],
     *             [
     *                 "000998",
     *                 "隆平高科",
     *                 "7.98%",
     *                 "213.27",
     *                 "3,864.45"
     *             ],
     *         ]
     *     },
     *     "meta": 003634
     * }
     * @apiSuccess (返回字段描述) {String} title 标题信息
     * @apiSuccess (返回字段描述) {String} date 截至时间
     * @apiSuccess (返回字段描述) {String} stock 股票占比
     * @apiSuccess (返回字段描述) {String} bond 债券占比
     * @apiSuccess (返回字段描述) {String} cash 现金占比
     * @apiSuccess (返回字段描述) {String} total 总净资产(亿元)
     * @apiSuccess (返回字段描述) {String} stockList ["002311","海大集团","8.00%","96.35","3,873.36"]分别代表股票代码,名称,占比,持有股数(万股),持有金额(万元)
     * @apiError (Error 5xx) 500 内部网络异常
     * @apiSampleRequest https://api.doctorxiong.club/v1/fund/position
     */

    /**
     * @apiGroup Fund
     * @api {get} /v1/fund/all 获取所有基金
     * @apiName getAllFund
     * @apiDescription  所有获取基金
     * @apiParam {String} keyWord 支持基金代码和名称模糊查询(非必填)
     * @apiParamExample {String} 请求示例
     * /v1/fund/all
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         [
     *             "000001",
     *             "HXCZHH",
     *             "华夏成长混合",
     *             "混合型",
     *             "HUAXIACHENGZHANGHUNHE"
     *         ],
     *         [
     *             "000003",
     *             "ZHKZZZQA",
     *             "中海可转债债券A",
     *             "债券型",
     *             "ZHONGHAIKEZHUANZHAIZHAIQUANA"
     *         ]
     *     ]
     * }
     * @apiSampleRequest https://api.doctorxiong.club/v1/fund/all
     */

    /**
     * @apiGroup Token
     * @api {get} /v1/fund 无token的请求每小时限制一百次
     * @apiName token使用示例
     * @apiDescription  get请求支持两种方式,即支持header携带token,也支持url显式携带.post请求只支持header携带
     * @apiParam {String} token
     * @apiHeader {String} token UYhcC****
     * @apiParamExample {String} url显式携带
     * /v1/fund?code=202015&token=UYhcC****
     * @apiParamExample {json} header携带
     * {
     * 	 "token": "UYhcC****"
     * }
     * @apiError (错误码) 400 token无效
     */



}
