package com.cosmos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@EnableCaching//开启缓存服务
@SpringBootApplication
@MapperScan("com.cosmos.mapper")//指定扫描接口与映射配置文件的包名
public class TsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsPlatformApplication.class, args);
    }

}
