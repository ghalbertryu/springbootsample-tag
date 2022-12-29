package org.albertryu.springbootsample.common;

import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 在此定義 RedisTag 類型
 * 要指定對應類型 Tag timeout 資料存活時間
 */
@AllArgsConstructor
public enum RedisTagEnum {
    MISSION_DONE(3, TimeUnit.DAYS),
    EVENT_RECEIVED(7, TimeUnit.DAYS),
    MESSAGE_SEND(1, TimeUnit.DAYS),
    ;


    private long timeout;
    private TimeUnit timeoutUnit;

}
