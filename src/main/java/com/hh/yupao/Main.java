package com.hh.yupao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 黄昊
 * @version 1.0
 **/
@MapperScan("com.hh.yupao.mapper")
@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = new SpringApplication().run(Main.class, args);
    }
}