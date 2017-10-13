package com.huang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Description:
 * Created on 2017/9/29 10:30
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 *          Copyright (c) 2017 国美金控-美借
 */
@RunWith(SpringJUnit4ClassRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = RedisApplication.class)// 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration//由于是Web项目，Junit需要模拟ServletContext,因此我们需要给我们的测试类加上@WebAppConfiguration
@Slf4j
public class EfficientTest {

    @Resource(name="getRedisTemplate")
    private ListOperations<String, Person> listOperations;

    @Resource(name="redisTemplate")
    private ListOperations<String, Person> listOperations1;

    @Test
    public void test7(){
        long start = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++){
            listOperations.leftPush("lk1", new Person("1", "zhangsan"));
            listOperations.leftPush("lk1", new Person("2", "lisi"));
            listOperations.rightPop("lk1");
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));

        start = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++){
            listOperations1.leftPush("lk1", new Person("1", "zhangsan"));
            listOperations1.leftPush("lk1", new Person("2", "lisi"));
            listOperations1.rightPop("lk1");
        }
        end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }

}
