package org.albertryu.springbootsample.service;

import org.albertryu.springbootsample.common.RedisTagEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisTagServiceTest {

    @Autowired
    private RedisTagService redisTagService;


    private static final String TEST_UUID_1 = "1234";
    private static final String TEST_UUID_2 = "5678";


    @Test
    void setTag() {
        redisTagService.setTag(RedisTagEnum.MISSION_DONE, TEST_UUID_1);

        redisTagService.describe();
    }

    @Test
    void checkTag() {
        System.out.println(redisTagService.checkTag(RedisTagEnum.MISSION_DONE, TEST_UUID_1));
        System.out.println(redisTagService.checkTag(RedisTagEnum.MISSION_DONE, TEST_UUID_1));

        System.out.println(redisTagService.checkTag(RedisTagEnum.MISSION_DONE, TEST_UUID_2));
        System.out.println(redisTagService.checkTag(RedisTagEnum.MISSION_DONE, TEST_UUID_2));

        redisTagService.describe();
    }
}