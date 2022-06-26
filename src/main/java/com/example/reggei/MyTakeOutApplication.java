package com.example.reggei;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// @Slf4j 可以使用 log 日志系统
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class MyTakeOutApplication {

    public static void main(String[] args) {

        SpringApplication.run(MyTakeOutApplication.class, args);
        log.info("项目启动成功...");
    }

}
