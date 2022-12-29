package org.albertryu.springbootsample.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisTagServiceTest {

    @Autowired
    private RedisTagService redisTagService;


    private static final String UUID_1 = "1234";
    private static final String UUID_2 = "5678";

    @Test
    void setMissionDoneTag() {
        redisTagService.setMissionDoneTag(UUID_1);
    }

    @Test
    void checkMissionDoneTag() {
        System.out.println(redisTagService.checkMissionDoneTag(UUID_1));
        System.out.println(redisTagService.checkMissionDoneTag(UUID_1));

        System.out.println(redisTagService.checkMissionDoneTag(UUID_2));
    }
}