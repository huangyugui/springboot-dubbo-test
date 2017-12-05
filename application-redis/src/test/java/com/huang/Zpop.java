package com.huang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Set;

/**
 * Description:
 * Created on 2017/9/29 19:08
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = RedisApplication.class)// 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration//由于是Web项目，Junit需要模拟ServletContext,因此我们需要给我们的测试类加上@WebAppConfiguration
@Slf4j
public class Zpop {

    @Autowired
    private RedisTemplate redisTemplate;

    @Before
    public void before(){
        redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    /**
     * Discription: 原子性的ZPOP
     * Created on: 2017/9/29 18:31
     * @param:
     * @return:
     * @author: <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
     */
    @Test
    public void test6(){
        redisTemplate.opsForZSet().add("zset", "v3", 1);
        redisTemplate.opsForZSet().add("zset", "v2", 2);
        redisTemplate.opsForZSet().add("zset", "v1", 3);
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.watch("zset");
        Set<String> set = redisTemplate.opsForZSet().range("zset", 0, 0);
        log.info("取出的元素为：{}, zset的元素个数为：{}", set, redisTemplate.opsForZSet().size("zset"));
        while(true){
            redisTemplate.multi();
            redisTemplate.opsForZSet().remove("zset", set.toArray());
//            //是事务中，因此只会返回null，要注意下面的写法会报空指针异常
//            Long num = redisTemplate.opsForZSet().remove("zset", "v3");
//            log.info("删除了 {} 个元素", num);
            List<Object> result = redisTemplate.exec();
            log.info("执行结果为：{}", result);
            if((Long)result.get(0) > 0){
                break;
            }
        }
//        redisTemplate.opsForZSet().range("zset", 0, 9);
//        log.info("zset的元素为：{}");
    }

}
