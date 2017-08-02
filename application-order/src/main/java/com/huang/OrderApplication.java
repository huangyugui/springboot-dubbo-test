package com.huang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.huang.dao")
public class OrderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(OrderApplication.class).web(false).run(args);
        com.alibaba.dubbo.container.Main.main(args);
    }

}
