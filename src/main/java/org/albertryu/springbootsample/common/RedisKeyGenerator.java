package org.albertryu.springbootsample.common;

public class RedisKeyGenerator {

    public static final String TAG_KEY_FORMAT = "TAG:%s:%s";


    public static String genTagRedisKey(RedisTagEnum redisTagEnum, String uuid) {
        return String.format(TAG_KEY_FORMAT, redisTagEnum.name().toUpperCase(), uuid);
    }
}
