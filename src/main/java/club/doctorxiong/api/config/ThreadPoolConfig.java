package club.doctorxiong.api.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author gognqiangwei
 * @description 批量接单的线程池
 * @date 2022/5/10
 */
@Data
@Configuration
@Slf4j
public class ThreadPoolConfig {

    private int corePoolSize = 5;

    private int maxPoolSize = 10;

    private int keepAliveSeconds = 5;

    private int queueCapacity = 100;


    /**
     * 线程池,启用了链路追踪的
     *
     * @return
     */
    @Bean
    public ThreadPoolExecutor ThreadPoolExecutor() {
        ThreadFactory threadFactory = new TaskThreadFactory("ThreadPoolExecutor start",true,1);
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueCapacity),
                threadFactory, new ThreadPoolExecutor.AbortPolicy()) {
           /* @Override
            public void execute(Runnable task) {
                super.execute(TraceIdUtil.wrap(task, MDC.getCopyOfContextMap()));
            }

            @Override
            public <T> Future<T> submit(Runnable task, T result) {
                return super.submit(TraceIdUtil.wrap(task, MDC.getCopyOfContextMap()), result);
            }

            @Override
            public <T> Future<T> submit(Callable<T> task) {
                return super.submit(TraceIdUtil.wrap(task, MDC.getCopyOfContextMap()));
            }

            @Override
            public Future<?> submit(Runnable task) {
                return super.submit(TraceIdUtil.wrap(task, MDC.getCopyOfContextMap()));
            }*/

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                if (t == null && r instanceof Future<?>) {
                    Future<?> future = (Future<?>) r;
                    if (future.isDone()) {
                        try {
                            future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            log.error("ThreadPoolExecutor ThreadPoolExecuteError", e);
                        }
                    }
                }
                if (t != null) {
                    log.error("ThreadPoolExecutor execute error, message: {%s}, {%s}", t.getMessage(), t);
                }
            }
        };
    }


}
