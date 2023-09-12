package com.lxkplus.RandomInit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lxkplus.RandomInit.mapper")
@SpringBootApplication
public class RandomInitApplication {

    public static void main(String[] args) {
        SpringApplication.run(RandomInitApplication.class, args);
    }
}
