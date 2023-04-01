package club.doctorxiong.api.doc;

/**
 * @Auther: 熊鑫
 * @Date: 2019/6/28 16
 * @Description:
 */
public class StockApiDoc {

    /**
     * @apiGroup Stock
     * @api {get} /v1/stock 获取股票基础信息
     * @apiName getStock
     * @apiDescription 不包含历史K线信息和分时数据,支持批量查询, 获取数据后先判断类型type,
     * 若是ZS(指数)则没有买卖信息,部分指数代码可以通过/stock/board接口获取,也可以去网络上查询.GP类型可以细分为GP-A即A股,GP-B为B股,GP-A-KCB为科创板A股.
     * @apiParam {String} code 股票标识代码,例如000001(必填)支持两种格式.直接使用股票代码（如000001）,添加交易所前缀（如sh000001）可以精准返回指数和股票
     * 例如默认000001返回的是平安银行,sh000001则返回上证指数
     * @apiParamExample {String} 请求示例:
     * /v1/stock?code=sz000001
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         {
     *             "code": "sz000001",
     *             "name": "平安银行",
     *             "type": "GP",
     *             "priceChange": "-0.01",
     *             "changePercent": "-0.07",
     *             "open": "14.17",
     *             "close": "14.15",
     *             "price": "14.14",
     *             "high": "14.28",
     *             "low": "13.95",
     *             "volume": "1291347",
     *             "turnover": "182104",
     *             "turnoverRate": "0.67",
     *             "totalWorth": "2744.00",
     *             "circulationWorth": "2743.97",
     *             "date": "2020-07-17 16:14:03",
     *             "buy": [
     *                 "14.14",
     *                 "746",
     *                 "14.13",
     *                 "3506",
     *                 "14.12",
     *                 "631",
     *                 "14.11",
     *                 "927",
     *                 "14.10",
     *                 "1112"
     *             ],
     *             "sell": [
     *                 "14.15",
     *                 "1963",
     *                 "14.16",
     *                 "4710",
     *                 "14.17",
     *                 "1545",
     *                 "14.18",
     *                 "3060",
     *                 "14.19",
     *                 "1837"
     *             ],
     *             "pb": "0.99",
     *             "spe": "9.73",
     *             "pe": "9.37"
     *         }
     *     ],
     *     "meta": null
     * }
     * @apiSuccess (返回字段描述) {String} code 股票代码
     * @apiSuccess (返回字段描述) {String} name 股票名称
     * @apiSuccess (返回字段描述) {String} type GP(股票),ZS(指数)
     * @apiSuccess (返回字段描述) {String} open 今日开盘价
     * @apiSuccess (返回字段描述) {String} close 昨日收盘价
     * @apiSuccess (返回字段描述) {String} price 实时价格
     * @apiSuccess (返回字段描述) {String} priceChange 开盘后价格变化
     * @apiSuccess (返回字段描述) {String} changePercent 价格变化,单位为百分比
     * @apiSuccess (返回字段描述) {String} high 开盘截至目前最高价
     * @apiSuccess (返回字段描述) {String} low 开盘截至目前最低价
     * @apiSuccess (返回字段描述) {String} volume 成交量 单位手
     * @apiSuccess (返回字段描述) {String} turnover 成交额 单位万
     * @apiSuccess (返回字段描述) {String} turnoverRate 换手率 单位百分比
     * @apiSuccess (返回字段描述) {String} totalWorth 总市值 单位亿
     * @apiSuccess (返回字段描述) {String} circulationWorth 流通市值 单位亿
     * @apiSuccess (返回字段描述) {String} date 数据更新日期,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiSuccess (返回字段描述) {String[]} buy 买一至买五["14.14","746","14.13","3506","14.12","631","14.11","927","14.10","1112"]
     * 依次表示:买一价格; 买一交易量(手); 买二价格; 买二交易量...
     * @apiSuccess (返回字段描述) {String[]} sell 卖一至卖五["14.14","746","14.13","3506","14.12","631","14.11","927","14.10","1112"]
     * 依次表示:卖一价格; 卖一交易量(手); 卖二价格; 卖二交易量...
     * @apiSuccess (返回字段描述) {String} pb 市净率
     * @apiSuccess (返回字段描述) {String} spe 静态市盈率
     * @apiSuccess (返回字段描述) {String} pe 市盈率
     * @apiError (错误码) 406 无效的股票代码
     * @apiError (错误码) 400 解析请求失败
     * @apiError (错误码) 500 内部网络异常
     * @apiSampleRequest https://api.doctorxiong.club/v1/stock
     */

    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/min 分时数据
     * @apiName getStockMin
     * @apiDescription  获取当前交易日的分时数据和基本数据信息
     * @apiParam {String} code 股票标识代码,例如000001(必填)支持两种格式.直接使用股票代码（如000001）,添加交易所前缀（如sh000001）可以精准返回指数和股票
     * @apiParamExample {json} 请求示例
     * /v1/stock/min?code=000001
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "code": "sz000001",
     *         "name": "平安银行",
     *         "type": "GP-A",
     *         "priceChange": "-0.02",
     *         "changePercent": "-0.13",
     *         "open": "14.88",
     *         "close": "14.96",
     *         "price": "14.94",
     *         "high": "15.24",
     *         "low": "14.83",
     *         "volume": "1031377",
     *         "turnover": "155197",
     *         "turnoverRate": "0.53",
     *         "totalWorth": "2899.24",
     *         "circulationWorth": "2899.22",
     *         "date": "2020-09-07 16:15:03",
     *         "buy": [
     *             "14.93",
     *             "384",
     *             "14.92",
     *             "470",
     *             "14.91",
     *             "1278",
     *             "14.90",
     *             "3599",
     *             "14.89",
     *             "973"
     *         ],
     *         "sell": [
     *             "14.94",
     *             "9604",
     *             "14.95",
     *             "394",
     *             "14.96",
     *             "699",
     *             "14.97",
     *             "1233",
     *             "14.98",
     *             "927"
     *         ],
     *         "minData": [
     *             [
     *                 "0930",
     *                 "14.88",
     *                 "15533"
     *             ],
     *             [
     *                 "0931",
     *                 "14.87",
     *                 "19857"
     *             ],
     *             [
     *                 "0932",
     *                 "14.88",
     *                 "23811"
     *             ],
     *             [
     *                 "0933",
     *                 "14.88",
     *                 "32929"
     *             ],
     *             [
     *                 "0934",
     *                 "14.94",
     *                 "36779"
     *             ]
     *         ],
     *         "spe": "10.28",
     *         "pe": "10.95",
     *         "pb": "1.03"
     *     },
     *     "meta": null
     * }
     * @apiSuccess (返回字段描述) {String[][]} minData 开盘当日分时图[0930,10.21,86]依次表示:日期; 单位净值; 股价; 成交量.
     * @apiSuccess (返回字段描述) {String} data 同时返回股票基本信息,参数参考股票基本信息接口
     * @apiError (错误码) 400 解析请求失败,一般是参数错误
     * @apiError (错误码) 500 内部网络异常
     */




    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/kline/day 日K数据
     * @apiName getKLineOfDay
     * @apiDescription  返回对应时间内的日K数据,包含前复权和后复权
     * @apiParam {String} code 股票代码(必填)
     * @apiParam {Date} startDate 开始时间,标准时间格式yyyy-MM-dd
     * @apiParam {Date} endDate 截至时间,标准时间格式yyyy-MM-dd
     * @apiParam {int} type=0 0不复权,1前复权,2后复权
     * @apiParamExample {String} 请求示例:
     * /v1/stock/kline/day?code=000001&startDate=2020-09-01&type=1
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         [
     *             "2020-09-01",
     *             "14.960",
     *             "15.140",
     *             "15.230",
     *             "14.880",
     *             "813643.000"
     *         ],
     *         [
     *             "2020-09-02",
     *             "15.010",
     *             "15.320",
     *             "15.530",
     *             "15.010",
     *             "1679383.000"
     *         ],
     *         [
     *             "2020-09-03",
     *             "15.320",
     *             "14.900",
     *             "15.330",
     *             "14.840",
     *             "1279842.000"
     *         ],
     *         [
     *             "2020-09-04",
     *             "14.730",
     *             "14.960",
     *             "15.060",
     *             "14.600",
     *             "909890.000"
     *         ],
     *         [
     *             "2020-09-07",
     *             "14.880",
     *             "14.940",
     *             "15.240",
     *             "14.830",
     *             "1031377.000"
     *         ]
     *     ],
     *     "meta": "sz000001"
     * }
     * @apiSuccess (返回字段描述) {String[][]} data 历史股价信息依次表示:日期;开盘;收盘;最高;最低;成交量(手)
     * @apiError (错误码) 400 解析请求失败,一般是参数错误
     * @apiError (错误码) 500 内部网络异常
     */

    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/kline/week 周K数据
     * @apiName getKLineOfWeek
     * @apiDescription  返回对应时间内的周K数据,包含前复权和后复权
     * @apiParam {String} code 股票代码(必填)
     * @apiParam {Date} startDate 开始时间,标准时间格式yyyy-MM-dd
     * @apiParam {Date} endDate 截至时间,标准时间格式yyyy-MM-dd
     * @apiParam {int} type=0 0不复权,1前复权,2后复权
     * @apiParamExample {String} 请求示例:
     * /v1/stock/kline/week?code=000001&startDate=2020-09-01&type=1
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         [
     *             "2020-09-04",
     *             "15.300",
     *             "14.960",
     *             "15.680",
     *             "14.600",
     *             "6479888.000"
     *         ],
     *         [
     *             "2020-09-07",
     *             "14.88",
     *             "14.94",
     *             "15.24",
     *             "14.83",
     *             "1031377"
     *         ]
     *     ],
     *     "meta": "sz000001"
     * }
     * @apiSuccess (返回字段描述) {String[][]} data 历史股价信息依次表示:日期;开盘;收盘;最高;最低;成交量(手)
     * @apiError (错误码) 400 解析请求失败,一般是参数错误
     * @apiError (错误码) 500 内部网络异常
     */

    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/kline/month 月K数据
     * @apiName getKLineOfMonth
     * @apiDescription  返回对应时间内的月K数据,包含前复权和后复权
     * @apiParam {String} code 股票代码(必填)
     * @apiParam {Date} startDate 开始时间,标准时间格式yyyy-MM-dd
     * @apiParam {Date} endDate 截至时间,标准时间格式yyyy-MM-dd
     * @apiParam {int} type=0 0不复权,1前复权,2后复权
     * @apiParamExample {String} 请求示例:
     * /v1/stock/kline/month?code=000001&startDate=2020-05-01&type=1
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         [
     *             "2020-05-29",
     *             "13.760",
     *             "13.000",
     *             "14.130",
     *             "12.760",
     *             "12164619.000"
     *         ],
     *         [
     *             "2020-06-30",
     *             "13.100",
     *             "12.800",
     *             "13.880",
     *             "12.520",
     *             "18273192.000"
     *         ],
     *         [
     *             "2020-07-31",
     *             "12.790",
     *             "13.340",
     *             "16.630",
     *             "12.740",
     *             "48072733.000"
     *         ],
     *         [
     *             "2020-08-31",
     *             "13.470",
     *             "15.080",
     *             "15.680",
     *             "13.430",
     *             "31244079.000"
     *         ],
     *         [
     *             "2020-09-07",
     *             "14.960",
     *             "14.94",
     *             "15.530",
     *             "14.600",
     *             "5714135.000"
     *         ]
     *     ],
     *     "meta": "sz000001"
     * }
     * @apiSuccess (返回字段描述) {String[][]} data 历史股价信息依次表示:日期;开盘;收盘;最高;最低;成交量(手)
     * @apiError (错误码) 400 解析请求失败,一般是参数错误
     * @apiError (错误码) 500 内部网络异常
     */





    /**
     * @apiGroup Stock
     * @api {post} /v1/stock/rank 获取股票排行
     * @apiName getStockRank
     * @apiDescription 根据条件获取股票排行(包含基本股票信息)
     * @apiParam {String={"a","b","ash","asz","bsh","bsz"}} node="a" a(沪深A股) ash(沪市A股),asz(深市A股),b同理
     * @apiParam {String} industryCode 板块代码,通过接口/stock/industry/rank获得
     * @apiParam {String={"price","priceChange","pricePercent","buy","sell","open","close","high","low","volume","turnover"}} sort="turnover"
     * 默认最近的工作日的数据进行排序price(交易价格),priceChange(涨跌额),pricePercent(涨跌幅),
     * 买入价格(buy), 卖出价格(sell), 开盘价格(open), 收盘价格(close), 最高价(high), 最低价(low), 成交量(volume), 成交额(turnover)
     * @apiParam {Number={0,1,-1}} asc=0 排序方式 0(降序) 1(升序) -1(默认序列)
     * @apiParam {Number} pageIndex=1 页码
     * @apiParam {Number} pageSize=10 每页显示的数量
     * @apiParamExample {json} 请求示例1:
     * 获取按成交额排序的股票列表
     * Content-Type: application/json
     * {
     * 	"sort":"turnover",
     * 	"node":"a"
     * }
     * @apiParamExample {json} 请求示例2:
     * 获取某个板块按成交额排序股票列表
     * Content-Type: application/json
     * {
     * 	"industryCode":"****",
     * 	"sort":"turnover"
     * }
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "pageIndex": 1,
     *         "pageSize": 1,
     *         "allPages": 280,
     *         "rank": [
     *             {
     *                 "code": "sh600519",
     *                 "name": "贵州茅台",
     *                 "type": "GP",
     *                 "priceChange": "34.05",
     *                 "changePercent": "2.11",
     *                 "open": "1620.00",
     *                 "close": "1614.00",
     *                 "price": "1648.05",
     *                 "high": "1672.59",
     *                 "low": "1620.00",
     *                 "volume": "65902",
     *                 "turnover": "1086342",
     *                 "turnoverRate": "0.52",
     *                 "totalWorth": "20702.77",
     *                 "circulationWorth": "20702.77",
     *                 "date": "2020-07-17 15:40:45",
     *                 "buy": [],
     *                 "sell": [],
     *                 "spe": "50.24",
     *                 "pb": "16.21",
     *                 "pe": "48.06"
     *             }
     *         ]
     *     },
     *     "meta": null
     * }
     * @apiSuccess (返回字段描述) {Number} pageIndex 页码
     * @apiSuccess (返回字段描述) {Number} pageSize 页大小
     * @apiSuccess (返回字段描述) {Number} allPages 总页数
     * @apiSuccess (返回字段描述) {String} code 股票代码
     * @apiSuccess (返回字段描述) {String} name 股票名称
     * @apiSuccess (返回字段描述) {String} type GP(股票),ZS(指数)
     * @apiSuccess (返回字段描述) {String} open 今日开盘价
     * @apiSuccess (返回字段描述) {String} close 昨日收盘价
     * @apiSuccess (返回字段描述) {String} price 实时价格
     * @apiSuccess (返回字段描述) {String} priceChange 开盘后价格变化
     * @apiSuccess (返回字段描述) {String} changePercent 价格变化,单位为百分比
     * @apiSuccess (返回字段描述) {String} high 开盘截至目前最高价
     * @apiSuccess (返回字段描述) {String} low 开盘截至目前最低价
     * @apiSuccess (返回字段描述) {String} volume 成交量 单位手
     * @apiSuccess (返回字段描述) {String} turnover 成交额 单位万
     * @apiSuccess (返回字段描述) {String} turnoverRate 换手率 单位百分比
     * @apiSuccess (返回字段描述) {String} totalWorth 总市值 单位亿
     * @apiSuccess (返回字段描述) {String} circulationWorth 流通市值 单位亿
     * @apiSuccess (返回字段描述) {String} date 数据更新日期,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiSuccess (返回字段描述) {String} pb 市净率
     * @apiSuccess (返回字段描述) {String} spe 静态市盈率
     * @apiSuccess (返回字段描述) {String} pe 市盈率
     * @apiSuccess (返回字段描述) {String[]} buy 买一至买五["14.14","746","14.13","3506","14.12","631","14.11","927","14.10","1112"]
     * 依次表示:买一价格; 买一交易量(手); 买二价格; 买二交易量...
     * @apiSuccess (返回字段描述) {String[]} sell 卖一至卖五["14.14","746","14.13","3506","14.12","631","14.11","927","14.10","1112"]
     * 依次表示:卖一价格; 卖一交易量(手); 卖二价格; 卖二交易量...
     * @apiError 400 解析请求失败
     * @apiError (Error 5xx) 500 内部网络异常
     */


    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/hot 获取热门股票
     * @apiName getHotStock
     * @apiDescription 获取热门股票
     * @apiParamExample {String} 请求示例:
     * /v1/stock/hot
     * @apiSuccessExample {json} 返回示例
     *{
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *          {
     *                 "code": "sh600519",
     *                 "name": "贵州茅台",
     *                 "type": "GP",
     *                 "priceChange": "34.05",
     *                 "changePercent": "2.11",
     *                 "open": "1620.00",
     *                 "close": "1614.00",
     *                 "price": "1648.05",
     *                 "high": "1672.59",
     *                 "low": "1620.00",
     *                 "volume": "65902",
     *                 "turnover": "1086342",
     *                 "turnoverRate": "0.52",
     *                 "totalWorth": "20702.77",
     *                 "circulationWorth": "20702.77",
     *                 "date": "2020-07-17 15:40:45",
     *                 "buy": [],
     *                 "sell": [],
     *                 "spe": "50.24",
     *                 "pb": "16.21",
     *                 "pe": "48.06"
     *           }
     *     ]
     *     "meta": null
     * }
     * @apiSuccess (返回字段描述) {String} code 股票代码
     * @apiSuccess (返回字段描述) {String} name 股票名称
     * @apiSuccess (返回字段描述) {String} type GP(股票),ZS(指数)
     * @apiSuccess (返回字段描述) {String} open 今日开盘价
     * @apiSuccess (返回字段描述) {String} close 昨日收盘价
     * @apiSuccess (返回字段描述) {String} price 实时价格
     * @apiSuccess (返回字段描述) {String} priceChange 开盘后价格变化
     * @apiSuccess (返回字段描述) {String} changePercent 价格变化,单位为百分比
     * @apiSuccess (返回字段描述) {String} high 开盘截至目前最高价
     * @apiSuccess (返回字段描述) {String} low 开盘截至目前最低价
     * @apiSuccess (返回字段描述) {String} volume 成交量 单位手
     * @apiSuccess (返回字段描述) {String} turnover 成交额 单位万
     * @apiSuccess (返回字段描述) {String} turnoverRate 换手率 单位百分比
     * @apiSuccess (返回字段描述) {String} totalWorth 总市值 单位亿
     * @apiSuccess (返回字段描述) {String} circulationWorth 流通市值 单位亿
     * @apiSuccess (返回字段描述) {String} date 数据更新日期,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     * @apiSuccess (返回字段描述) {String} pb 市净率
     * @apiSuccess (返回字段描述) {String} spe 静态市盈率
     * @apiSuccess (返回字段描述) {String} pe 市盈率
     * @apiSuccess (返回字段描述) {String[]} buy 买一至买五["14.14","746","14.13","3506","14.12","631","14.11","927","14.10","1112"]
     * 依次表示:买一价格; 买一交易量(手); 买二价格; 买二交易量...
     * @apiSuccess (返回字段描述) {String[]} sell 卖一至卖五["14.14","746","14.13","3506","14.12","631","14.11","927","14.10","1112"]
     * 依次表示:卖一价格; 卖一交易量(手); 卖二价格; 卖二交易量...
     * @apiError (Error 5xx) 500 内部网络异常
     * @apiSampleRequest https://api.doctorxiong.club/v1/stock/hot
     */



    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/industry/rank 获取行业板块
     * @apiName getIndustry
     * @apiDescription 获取全部行业板块(包含基本板块信息,已按业绩排序)
     * @apiParamExample {String} 请求示例:
     * /v1/stock/industry/rank
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *          {
     *             "industryCode": "012047",
     *             "name": "电子元器件",
     *             "averagePrice": "14444.30",
     *             "priceChange": "-164.20",
     *             "changePercent": "-1.12",
     *             "volume": "49813900.00",
     *             "turnover": "10326700.00",
     *             "stockNum": "166",
     *             "stockCode": "sz300852"
     *         },
     *         {
     *             "industryCode": "012063",
     *             "name": "软件服务",
     *             "averagePrice": "26183.80",
     *             "priceChange": "-299.40",
     *             "changePercent": "-1.13",
     *             "volume": "31099100.00",
     *             "turnover": "6693430.00",
     *             "stockNum": "197",
     *             "stockCode": "sh688579"
     *         },
     *         {
     *             "industryCode": "012071",
     *             "name": "保险",
     *             "averagePrice": "4985.61",
     *             "priceChange": "-61.07",
     *             "changePercent": "-1.21",
     *             "volume": "4137720.00",
     *             "turnover": "1277230.00",
     *             "stockNum": "7",
     *             "stockCode": "sh601318"
     *         },
     *         {
     *             "industryCode": "012006",
     *             "name": "钢铁",
     *             "averagePrice": "2656.65",
     *             "priceChange": "-37.84",
     *             "changePercent": "-1.40",
     *             "volume": "14541900.00",
     *             "turnover": "551175.00",
     *             "stockNum": "34",
     *             "stockCode": "sh600581"
     *         },
     * }
     * @apiSuccess (返回字段描述) {String} industryCode 行业板块代码
     * @apiSuccess (返回字段描述) {String} name 板块名称
     * @apiSuccess (返回字段描述) {String} averagePrice 平均价
     * @apiSuccess (返回字段描述) {String} priceChange 涨跌额
     * @apiSuccess (返回字段描述) {String} changePercent 涨跌幅度
     * @apiSuccess (返回字段描述) {String} volume 成交量(手)
     * @apiSuccess (返回字段描述) {String} turnover 成交额(万)
     * @apiSuccess (返回字段描述) {String} stockNum 该行业股票数
     * @apiSuccess (返回字段描述) {String} stockCode 领涨股
     * @apiError 400 解析请求失败
     * @apiError (Error 5xx) 500 内部网络异常
     */

    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/all 获取全部股票
     * @apiName getAllStock
     * @apiDescription 获取全部股票(包含已退市)
     * @apiParam {String} keyWord 支持代码和名称模糊查询(非必填)
     * @apiParamExample {String} 请求示例:
     * /v1/stock/all
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         [
     *             "sh600000",
     *             "浦发银行"
     *         ],
     *         [
     *             "sh600001",
     *             "邯郸钢铁"
     *         ],
     *         [
     *             "sh600002",
     *             "齐鲁石化"
     *         ],
     *         [
     *             "sh600003",
     *             "st东北高"
     *         ],
     *         [
     *             "sh600004",
     *             "白云机场"
     *         ]
     *     ]
     * }
     * @apiError 400 解析请求失败
     * @apiError (Error 5xx) 500 内部网络异常
     */

    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/index/all 获取全部指数
     * @apiName getAllIndex
     * @apiDescription 获取全部指数
     * @apiParamExample {String} 请求示例:
     * /v1/stock/index/all
     * @apiSuccessExample {json} 返回示例
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *         [
     *             "sh000001",
     *             "上证指数"
     *         ],
     *         [
     *             "sh000002",
     *             "上证a股"
     *         ],
     *         [
     *             "sh000003",
     *             "上证b指"
     *         ],
     *         [
     *             "sh000004",
     *             "工业指数"
     *         ]
     *     ]
     * }
     * @apiError 400 解析请求失败
     * @apiError (Error 5xx) 500 内部网络异常
     */

    /**
     * @apiGroup Stock
     * @api {post} /v1/stock/convertBond 分页获取可转债列表
     * @apiName getConvertBondList
     * @apiDescription 分页获取可转债(按申购时间排序)
     * @apiParam {Number} pageIndex=1 页码
     * @apiParam {Number} pageSize=10 每页显示的数量
     * @apiParamExample {json} 请求示例1:
     * 获取按成交额排序的股票列表
     * Content-Type: application/json
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "pageIndex": 1,
     *         "pageSize": 2,
     *         "totalRecord": 715,
     *         "rank": [
     *             {
     *                 "convertBondCode": "127070",
     *                 "convertBondName": "大中转债",
     *                 "valueDate": "2022-08-17 00:00:00",
     *                 "securityStartDate": "2022-08-16 00:00:00",
     *                 "subCode": "071203",
     *                 "stockCode": "001203",
     *                 "stockName": "大中矿业",
     *                 "stockPrice": "11.25",
     *                 "transferPrice": "11.36",
     *                 "transferValue": "99.0317",
     *                 "convertBondPrice": "-",
     *                 "firstPerPlacing": "1.0079",
     *                 "actualIssueScale": "15.2",
     *                 "bondStartDate": "2022-08-19 00:00:00",
     *                 "subMax": 1000
     *             },
     *             {
     *                 "convertBondCode": "123156",
     *                 "convertBondName": "博汇转债",
     *                 "valueDate": "2022-08-16 00:00:00",
     *                 "securityStartDate": "2022-08-15 00:00:00",
     *                 "subCode": "370839",
     *                 "stockCode": "300839",
     *                 "stockName": "博汇股份",
     *                 "stockPrice": "15.24",
     *                 "transferPrice": "15.05",
     *                 "transferValue": "101.2625",
     *                 "convertBondPrice": "-",
     *                 "firstPerPlacing": "2.2587",
     *                 "actualIssueScale": "3.97",
     *                 "bondStartDate": "2022-08-18 00:00:00",
     *                 "subMax": 1000
     *             }
     *         ],
     *         "pageNum": 1,
     *         "allPages": 358
     *     }
     * }
     * @apiSuccess (返回字段描述) {Number} pageIndex 页码
     * @apiSuccess (返回字段描述) {Number} pageSize 页大小
     * @apiSuccess (返回字段描述) {Number} allPages 总页数
     * @apiSuccess (返回字段描述) {String} convertBondCode 债券代码(可直接通过股票接口查询)
     * @apiSuccess (返回字段描述) {String} convertBondName 可转债名称
     * @apiSuccess (返回字段描述) {String} valueDate 申购日期
     * @apiSuccess (返回字段描述) {String} securityStartDate 股权登记日
     * @apiSuccess (返回字段描述) {String} bondStartDate 中签发布日
     * @apiSuccess (返回字段描述) {String} bondGainRate 中签率(百分比)
     * @apiSuccess (返回字段描述) {String} subCode 申购代码
     * @apiSuccess (返回字段描述) {String} subMax 最大申购手数
     * @apiSuccess (返回字段描述) {String} stockCode 正股代码
     * @apiSuccess (返回字段描述) {String} stockName 正股简称
     * @apiSuccess (返回字段描述) {String} stockPrice 正股价
     * @apiSuccess (返回字段描述) {String} transferPrice 转股价
     * @apiSuccess (返回字段描述) {String} transferValue 转股价值
     * @apiSuccess (返回字段描述) {String} convertBondPrice 债现价
     * @apiSuccess (返回字段描述) {String} transferPremiumPatio 转股溢价(百分比)
     * @apiSuccess (返回字段描述) {String} firstPerPlacing 每股配售额
     * @apiSuccess (返回字段描述) {String} actualIssueScale 发行规模 单位亿
     * @apiError 400 解析请求失败
     * @apiError (Error 5xx) 500 内部网络异常
     */


    /**
     * @apiGroup Stock
     * @api {get} /v1/stock/convertBond 获取全部可转债列表
     * @apiName getConvertBondList
     * @apiDescription 全部可转债(按申购时间排序)
     * @apiParamExample {json} 请求示例1:
     * 获取按成交额排序的股票列表
     * Content-Type: application/json
     * {
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": [
     *               {
     *                   "convertBondCode": "127070",
     *                   "convertBondName": "大中转债",
     *                   "valueDate": "2022-08-17 00:00:00",
     *                   "securityStartDate": "2022-08-16 00:00:00",
     *                   "subCode": "071203",
     *                   "stockCode": "001203",
     *                   "stockName": "大中矿业",
     *                   "stockPrice": "11.25",
     *                   "transferPrice": "11.36",
     *                   "transferValue": "99.0317",
     *                   "convertBondPrice": "-",
     *                   "firstPerPlacing": "1.0079",
     *                   "actualIssueScale": "15.2",
     *                   "bondStartDate": "2022-08-19 00:00:00",
     *                   "subMax": 1000
     *               },
     *               {
     *                   "convertBondCode": "123156",
     *                   "convertBondName": "博汇转债",
     *                   "valueDate": "2022-08-16 00:00:00",
     *                   "securityStartDate": "2022-08-15 00:00:00",
     *                   "subCode": "370839",
     *                   "stockCode": "300839",
     *                   "stockName": "博汇股份",
     *                   "stockPrice": "15.24",
     *                   "transferPrice": "15.05",
     *                   "transferValue": "101.2625",
     *                   "convertBondPrice": "-",
     *                   "firstPerPlacing": "2.2587",
     *                   "actualIssueScale": "3.97",
     *                   "bondStartDate": "2022-08-18 00:00:00",
     *                   "subMax": 1000
     *               }
     *           ]
     * }
     * @apiSuccess (返回字段描述) {String} convertBondCode 债券代码(可直接通过股票接口查询)
     * @apiSuccess (返回字段描述) {String} convertBondName 可转债名称
     * @apiSuccess (返回字段描述) {String} valueDate 申购日期
     * @apiSuccess (返回字段描述) {String} securityStartDate 股权登记日
     * @apiSuccess (返回字段描述) {String} bondStartDate 中签发布日
     * @apiSuccess (返回字段描述) {String} bondGainRate 中签率(百分比)
     * @apiSuccess (返回字段描述) {String} subCode 申购代码
     * @apiSuccess (返回字段描述) {String} subMax 最大申购手数
     * @apiSuccess (返回字段描述) {String} stockCode 正股代码
     * @apiSuccess (返回字段描述) {String} stockName 正股简称
     * @apiSuccess (返回字段描述) {String} stockPrice 正股价
     * @apiSuccess (返回字段描述) {String} transferPrice 转股价
     * @apiSuccess (返回字段描述) {String} transferValue 转股价值
     * @apiSuccess (返回字段描述) {String} convertBondPrice 债现价
     * @apiSuccess (返回字段描述) {String} transferPremiumPatio 转股溢价(百分比)
     * @apiSuccess (返回字段描述) {String} firstPerPlacing 每股配售额
     * @apiSuccess (返回字段描述) {String} actualIssueScale 发行规模 单位亿
     * @apiError 400 解析请求失败
     * @apiError (Error 5xx) 500 内部网络异常
     */
}
