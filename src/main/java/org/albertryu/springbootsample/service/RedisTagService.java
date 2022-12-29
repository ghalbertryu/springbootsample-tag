package org.albertryu.springbootsample.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.albertryu.springbootsample.common.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, String> readOnlyRedisTemplate;

    private final Cache<String, Instant> tagMissionDoneCache = Caffeine.newBuilder().expireAfterAccess(3, TimeUnit.DAYS).build();


    public void setMissionDoneTag(String uuid) {
        Instant tagMissionDoneCacheInstant = tagMissionDoneCache.getIfPresent(uuid);
        if (Objects.nonNull(tagMissionDoneCacheInstant)) {
            return;
        }

        String redisKey = RedisKeyGenerator.genTagMissionDoneRedisKey(uuid);
        redisTemplate.opsForValue().set(redisKey, "", 3, TimeUnit.DAYS);
        tagMissionDoneCache.put(uuid, Instant.now());
        log.info("setMissionDoneTag. uuid={}", uuid);
    }

    public boolean checkMissionDoneTag(String uuid) {
        boolean ret;
        Instant tagMissionDoneCacheInstant = tagMissionDoneCache.getIfPresent(uuid);

        if (Objects.nonNull(tagMissionDoneCacheInstant)) {
            ret = true;
            log.info("checkMissionDoneTag from local. ret={}, uuid={}", ret, uuid);
        } else {
            ret = Boolean.TRUE.equals(readOnlyRedisTemplate.hasKey(RedisKeyGenerator.genTagMissionDoneRedisKey(uuid)));
            log.info("checkMissionDoneTag from redis. ret={}, uuid={}", ret, uuid);
            if (ret) {
                tagMissionDoneCache.put(uuid, Instant.now());
            }
        }

        return ret;
    }
}
