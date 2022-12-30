package org.albertryu.springbootsample.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.List;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

@Slf4j
@Configuration
public class RedisConfig {

    /**
     * 使用 RedisStaticMasterReplicaConfiguration 來建立 RedisConnectionFactory
     * 讓 spring 自動建立的 redisTemplate 使用 Write to Master, Read from Replica 的策略來存取 redis
     * <a href="https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:write-to-master-read-from-replica">
     *     write-to-master-read-from-replica</a>
     */
    @Bean("masterReplicaRedisConnectionFactory")
    public LettuceConnectionFactory masterReplicaRedisConnectionFactory(MasterReplicaRedisInstance masterReplicaRedisInstance) {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(REPLICA_PREFERRED)
                .build();

        MasterReplicaRedisInstance.RedisInstance master = masterReplicaRedisInstance.getMaster();
        List<MasterReplicaRedisInstance.RedisInstance> slaves = masterReplicaRedisInstance.getSlaves();
        RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(master.getHost(), master.getPort());
        slaves.forEach(slave -> staticMasterReplicaConfiguration.addNode(slave.getHost(), slave.getPort()));
        staticMasterReplicaConfiguration.setDatabase(masterReplicaRedisInstance.getDatabase());
        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
    }

    @Bean("masterReplicaRedisInstance")
    @ConfigurationProperties(prefix = "springbootsample.redis")
    public MasterReplicaRedisInstance masterReplicaRedisInstance() {
        return new MasterReplicaRedisInstance();
    }

    @Data
    public static class MasterReplicaRedisInstance {

        private int database = 0;
        private RedisInstance master;
        private List<RedisInstance> slaves;

        @Data
        public static class RedisInstance {
            private String host;
            private int port;
        }
    }

}
