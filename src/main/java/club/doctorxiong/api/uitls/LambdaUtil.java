package club.doctorxiong.api.uitls;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author XIONGXIN
 * @title: utils
 * @projectName miniprogress
 * @date 2021/2/23 19:57
 */
@Slf4j
public class LambdaUtil {


    public static <T> Consumer<T> forEachWrapper(Consumer<T> consumer) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                log.info("Consumer执行异常:"+ex.getMessage());
            }
        };
    }
    public static <T,R> Function<T, R> mapWrapper(Function<T,R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception ex) {
                log.info("Function获取返回值异常:"+ex.getMessage());
                return null;
            }
        };
    }

}
