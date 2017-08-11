package com.huang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 */
@SpringBootApplication
public class RabbitMqApplication {

    public static void main(String[] args) {
//        new SpringApplicationBuilder().sources(RabbitMqApplication.class).web(false).run(args);
//        com.alibaba.dubbo.container.Main.main(args);
        SpringApplication.run(RabbitMqApplication.class, args);
    }

}
