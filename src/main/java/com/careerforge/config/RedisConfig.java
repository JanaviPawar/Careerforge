// src/main/java/com/careerforge/config/RedisConfig.java
package com.careerforge.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {
    /*
     * We configure DIFFERENT TTLs for different data types:
     *   userProfile   → 10 min (changes sometimes)
     *   companies     → 10 min (relatively stable)
     *   dreamCompanies→ 60 min (rarely changes)
     *   dashboard     →  5 min (aggregated, changes frequently)
     *   codingProgress→ 10 min
     *
     * Without explicit TTL, stale data lives in Redis forever.
     * Proper TTL = data freshness guarantee.
     */

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // JSON serialization — human-readable in Redis CLI
        Jackson2JsonRedisSerializer<Object> jsonSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonSerializer);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultCfg = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> configs = new HashMap<>();
        configs.put("userProfile",    defaultCfg.entryTtl(Duration.ofMinutes(10)));
        configs.put("companies",      defaultCfg.entryTtl(Duration.ofMinutes(10)));
        configs.put("dreamCompanies", defaultCfg.entryTtl(Duration.ofMinutes(60)));
        configs.put("dashboard",      defaultCfg.entryTtl(Duration.ofMinutes(5)));
        configs.put("codingProgress", defaultCfg.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultCfg)
                .withInitialCacheConfigurations(configs)
                .build();
    }
}
