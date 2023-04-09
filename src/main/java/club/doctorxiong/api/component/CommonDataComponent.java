package club.doctorxiong.api.component;


import club.doctorxiong.api.common.dto.ConvertBondDTO;
import club.doctorxiong.api.service.FundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: 熊鑫
 * @Date: 2020/8/25 17
 * @Description: 一些通用的全局数据
 *
 */
@Component
@Slf4j
public class CommonDataComponent {
    @Autowired
    private StockComponent stockComponent;
    @Autowired
    private FundService fundService;


    public static HashSet<String> allStockCode=new HashSet<>();
    public static Map<String,String> fundCodeAndTypeMap = new HashMap<>();

    public static Map<String,String> allStockMap = new ConcurrentHashMap<>();

    public static Map<String,String> allIndexMap = new ConcurrentHashMap<>();

    public static List<ConvertBondDTO> convertBondList = new LinkedList<>();

    private static final int DEFAULT_SIZE = 2 << 24;
    private static final int[] SEEDS = new int[] { 5, 7, 11, 13, 31, 37, 61 };
    private BitSet bits = new BitSet(DEFAULT_SIZE);
    private SimpleHash[] func = new SimpleHash[SEEDS.length];

    /*@Override
    public void run(String... args) {
        //把股票代码,指数代码,基金代码全塞进去

        List<String[]> allStock=stockComponent.getAllStockOrIndex(true);
        List<String[]> allIndex=stockComponent.getAllStockOrIndex(false);
        List<String[]> allFund=fundService.getAllFund(null);

        allFund.forEach((fund)->{
            fundCodeAndTypeMap.put(fund[0],fund[3]);
        });

        allStock.forEach((stock)->{
            allStockCode.add(stock[0]);
            allStockMap.put(stock[0],stock[1]);
        });
        allIndex.forEach((index)->{
            allStockCode.add(index[0]);
            allIndexMap.put(index[0],index[1]);
        });

        refreshConvertBondList();
    }

    *//**
     * 刷新可转债列表
     *//*
    public void refreshConvertBondList(){
        convertBondList = stockComponent.getConvertBondList();
    }

    public void refreshStock(){
        log.info("refreshStock 刷新代码");
        List<String[]> allStock=stockComponent.getAllStockOrIndex(true);
        List<String[]> allIndex=stockComponent.getAllStockOrIndex(false);

        allStock.forEach((stock)->{
            allStockCode.add(stock[0]);
            if(!allStockMap.containsKey(stock[0])){
                allStockMap.put(stock[0],stock[1]);
            }
        });
        allIndex.forEach((index)->{
            allStockCode.add(index[0]);
            if(!allIndexMap.containsKey(index[0])){
                allIndexMap.put(index[0],index[1]);
            }
        });
    }*/



    public CommonDataComponent() {
        for (int i = 0; i < SEEDS.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, SEEDS[i]);
        }
    }

    public void add(String value) {
        for (SimpleHash f : func) {
            bits.set(f.hash(value), true);
        }
    }

    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }

    public static class SimpleHash {
        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }
}
