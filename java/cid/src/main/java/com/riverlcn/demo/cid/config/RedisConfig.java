package com.riverlcn.demo.cid.config;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis 配置类
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1)); // 设置缓存有效期一小时
        return RedisCacheManager
            .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<String, JsonNode> jsonNodeRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, JsonNode> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setDefaultSerializer(new JsonNode2JsonRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JsonNode2JsonRedisSerializer());
        template.setValueSerializer(new JsonNode2JsonRedisSerializer());

        return template;
    }

    /**
     * 扩展 {@link GenericJackson2JsonRedisSerializer} 的反序列化方法，支持解析 {@link JsonNode} 的对象.
     */
    static class JsonNode2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer {

        /**
         * 序列化成 JsonNode 对象.
         *
         * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
         */
        @Override
        public JsonNode deserialize(byte[] source) throws SerializationException {
            return super.deserialize(source, JsonNode.class);
        }
    }

}
