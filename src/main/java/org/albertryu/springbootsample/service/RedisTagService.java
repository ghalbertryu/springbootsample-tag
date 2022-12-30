package org.albertryu.springbootsample.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.albertryu.springbootsample.common.RedisKeyGenerator;
import org.albertryu.springbootsample.common.RedisTagEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author AlbertRyu
 * Redis 標記
 * 寫入一個有 expire time 的 redisKey
 * 主要用來擋掉一段時間內已經做過的任務
 */
@Slf4j
@Service
public class RedisTagService {

    @Autowired
    private StringRedisTemplate masterReplicaStringRedisTemplate;

    /**
     * key: redisKey
     * value: local cache time instant
     */
    private final Cache<String, Instant> tagLocalCache = Caffeine.newBuilder().expireAfterAccess(3, TimeUnit.DAYS).build();


    public void setTag(RedisTagEnum redisTagEnum, String uuid) {
        String redisKey = RedisKeyGenerator.genTagRedisKey(redisTagEnum, uuid);
        Instant tagMissionDoneCacheInstant = tagLocalCache.getIfPresent(redisKey);
        if (Objects.nonNull(tagMissionDoneCacheInstant)) {
            return;
        }

        masterReplicaStringRedisTemplate.opsForValue().set(redisKey, Instant.now().toString(), redisTagEnum.getTimeout(), redisTagEnum.getTimeoutUnit());
        tagLocalCache.put(redisKey, Instant.now());
        log.info("setMissionDoneTag. uuid={}, redisTagEnum={}", uuid, redisTagEnum);
    }

    public boolean checkTag(RedisTagEnum redisTagEnum, String uuid) {
        boolean ret;
        String redisKey = RedisKeyGenerator.genTagRedisKey(redisTagEnum, uuid);

        Instant tagMissionDoneCacheInstant = tagLocalCache.getIfPresent(redisKey);
        if (Objects.nonNull(tagMissionDoneCacheInstant)) {
            ret = true;
            log.info("checkMissionDoneTag from local. ret={}, uuid={}, redisTagEnum={}", ret, uuid, redisTagEnum);
        } else {
            ret = Boolean.TRUE.equals(masterReplicaStringRedisTemplate.hasKey(redisKey));
            log.info("checkMissionDoneTag from redis. ret={}, uuid={}, redisTagEnum={}", ret, uuid, redisTagEnum);
            if (ret) {
                tagLocalCache.put(redisKey, Instant.now());
            }
        }

        return ret;
    }

    public void describe() {
        log.info("RedisTagService describe. tagMissionDoneLocalCache={}", tagLocalCache.asMap());
    }

}
