package com.hsbc.trade.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Import({DataSourceConfig.class, RedisConfig.class})
public class InfraConfig {
    @Value("${thread.pool.core.size}")
    private int corePoolSize;
    @Value("${thread.pool.queue.capacity}")
    private int poolCapacity;

    @Value("${thread.pool.keep.alive.time}")
    private int keepAliveTime;

    @Value("${thread.pool.queue.max.size}")
    private int maxPoolSize;
    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize,
                maxPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<>(poolCapacity)) {
        };
        return poolExecutor;
    }
}
