package org.albertryu.springbootsample.common;

public class RedisKeyGenerator {

    public static final String TAG_MISSION_DONE_KEY = "Tag:MissionDone:%s";


    public static String genTagMissionDoneRedisKey(String uuid) {
        return String.format(TAG_MISSION_DONE_KEY, uuid);
    }
}
