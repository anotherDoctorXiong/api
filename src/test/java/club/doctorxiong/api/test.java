package club.doctorxiong.api;

import club.doctorxiong.api.common.dto.FundDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author XiongXin
 * @date 2023/4/2
 * @menu
 */
public class test {

    public static void main(String[] args) {
        String key = "000001";
        Cache<String, FundDTO> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();

        // 查找一个缓存元素， 没有查找到的时候返回null
        FundDTO graph = cache.getIfPresent(key);
        // 查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
        graph = cache.get(key, k -> {
            FundDTO one = new FundDTO();
            one.setCode(k);
            return one;
        });
        // 添加或者更新一个缓存元素
        cache.put(key, graph);

        Cache<String, FundDTO> cache2 = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
        System.out.println(cache2.getIfPresent(key));
    }

}
