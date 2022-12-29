package org.albertryu.springbootsample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisTemplateConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    @Value("${spring.data.redis.slave.host}")
    private String slaveHost;

    @Value("${spring.data.redis.slave.port}")
    private int slavePort;

    @Value("${spring.redis.database:1}")
    private int database;


    @Primary
    @Bean("redisTemplate")
    public RedisTemplate<String, String> redisTemplate() {
        return buildStringStringRedisTemplate(masterConnectionFactory());
    }

    private LettuceConnectionFactory masterConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);

        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        factory.afterPropertiesSet();
        factory.resetConnection();
        return factory;
    }

    private RedisTemplate<String, String> buildStringStringRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.setStringSerializer(stringSerializer);
        return template;
    }

    @Bean("readOnlyRedisTemplate")
    public RedisTemplate<String, String> readOnlyRedisTemplate() {
        return buildStringStringRedisTemplate(secondConnectionFactory());
    }

    private LettuceConnectionFactory secondConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(slaveHost);
        redisStandaloneConfiguration.setPort(slavePort);
        redisStandaloneConfiguration.setDatabase(database);

        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        factory.afterPropertiesSet();
        factory.resetConnection();
        return factory;
    }
}
