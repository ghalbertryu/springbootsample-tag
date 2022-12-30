# springbootsample-tag
SpringBootSample：將過去 SpringBoot 專案中，
曾經使用開發過的工具或是架構抽象模組化寫成 Sample Project，
方便未來複用或參考

Tag：將執行過的任務寫入一個有 expire time 的 redis key

在分散式服務當中方便其他程序確認是否要執行某項特定任務

主要用途為擋掉一段時間內已經做過的任務

* Redis 連線使用主從寫讀架構做到 Load Balance
* 會將曾經確認過已存在 Tag 之任務 key 存入 Local Cache 緩存

## Run
org.albertryu.springbootsample.Application.main

## HowToUse
1. 編輯任務類型定義：
org.albertryu.springbootsample.common.RedisTagEnum
2. 寫入 Tag：
org.albertryu.springbootsample.service.RedisTagService.setTag
3. 驗證 Tag：
org.albertryu.springbootsample.service.RedisTagService.checkTag

## UnitTest
org.albertryu.springbootsample.service.RedisTagServiceTest.setTag
org.albertryu.springbootsample.service.RedisTagServiceTest.checkTag

## Changelog
### 2022-12-30
* create project