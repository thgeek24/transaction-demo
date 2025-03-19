package com.thgeek.banking.transaction.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cache Config
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 20:55
 */
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("transactions");
    }
}
