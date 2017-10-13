package com.huang;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 * Created on 2017/9/26 13:51
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 *          Copyright (c) 2017 国美金控-美借
 */
@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }
}
