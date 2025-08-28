package ryzendee.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ryzendee.app.config.props.CacheProps;

import static ryzendee.app.constants.CacheManagerNameConstants.DEAL_CACHE_MANAGER;
import static ryzendee.app.constants.CacheManagerNameConstants.DEAL_METADATA_CACHE_MANAGER;

@EnableCaching
@EnableConfigurationProperties(CacheProps.class)
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final CacheProps cacheProps;

    @Primary
    @Bean(DEAL_CACHE_MANAGER)
    public RedisCacheManager dealCacheManager(RedisConnectionFactory redisConnectionFactory,
                                              RedisSerializationContext.SerializationPair<Object> jsonSerializer) {
        RedisCacheConfiguration dealCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(cacheProps.getDeals().getTtl())
                .serializeValuesWith(jsonSerializer);

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(dealCacheConfig)
                .build();
    }

    @Bean(DEAL_METADATA_CACHE_MANAGER)
    public RedisCacheManager dealMetadataCacheManager(RedisConnectionFactory redisConnectionFactory,
                                                      RedisSerializationContext.SerializationPair<Object> jsonSerializer) {
        RedisCacheConfiguration dealMetadataCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(cacheProps.getDealMetadata().getTtl())
                .serializeValuesWith(jsonSerializer);

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(dealMetadataCacheConfig)
                .build();
    }

    @Bean
    public RedisSerializationContext.SerializationPair<Object> jsonSerializer(ObjectMapper objectMapper) {
        return RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
    }
}
