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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created on 2017/9/29 11:14
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = RedisApplication.class)// 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration//由于是Web项目，Junit需要模拟ServletContext,因此我们需要给我们的测试类加上@WebAppConfiguration
@Slf4j
public class TransactionTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Before
    public void before(){
        redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    @Test
    public void test1(){
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        redisTemplate.opsForValue().set("sk1", "v1");
        redisTemplate.opsForValue().set("sk2", "v2");
        log.info("multi 之后，执行命令返回的结果：{}", redisTemplate.opsForValue().get("sk1"));
        log.info("事务结束之后返回的结果：{}", redisTemplate.exec());
    }

    @Test
    public void test2(){
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        redisTemplate.opsForValue().set("sk1", "v1");
        redisTemplate.opsForValue().set("sk2", "v2");
        Integer.valueOf("aaa");
        log.info("multi 之后，执行命令返回的结果：{}", redisTemplate.opsForValue().get("sk1"));
        log.info("事务结束之后返回的结果：{}", redisTemplate.exec());
    }

    /**
     * Discription: 命令在exec调用之后失败，则继续执行后续的命令，并不会停下来
     * Created on: 2017/9/29 15:52
     * @param:
     * @return:
     * @author: <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
     */
    @Test
    public void test3(){
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        redisTemplate.opsForValue().set("sk1", "v1");
        redisTemplate.opsForValue().set("sk2", "v2");
        redisTemplate.opsForValue().increment("sk2", 1);
        redisTemplate.opsForValue().set("sk3", "v3");
        log.info("multi 之后，执行命令返回的结果：{}", redisTemplate.opsForValue().get("sk1"));
        log.info("事务结束之后返回的结果：{}", redisTemplate.exec());
    }

    /**
     * Discription: 如果watch一个key，这个key的值在事务的过程中被被人改了，那么事务就会失败
     *          程序需要做的， 就是不断重试这个操作， 直到没有发生碰撞为止。
     * Created on: 2017/9/29 17:41
     * @param:
     * @return:
     * @author: <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
     */
    @Test
    public void test4(){
        redisTemplate.opsForValue().set("sk1", "ttttt");
        log.info("sk1 = {}", redisTemplate.opsForValue().get("sk1"));
        //redisTemplate.setEnableTransactionSupport(true); 一定要在watch之前
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.watch("sk1");
        while(true){
            redisTemplate.multi();
            redisTemplate.opsForValue().set("sk1", "v1");
            redisTemplate.opsForValue().set("sk2", "v2");
            redisTemplate.opsForValue().set("sk3", "v3");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("multi 之后，执行命令返回的结果：{}", redisTemplate.opsForValue().get("sk1"));
            List<Object> list = redisTemplate.exec();
            log.info("事务结束之后返回的结果：{}", list);
            if(list != null && list.size() != 0){
                break;
            }
        }
    }

    @Test
    public void test5(){
        redisTemplate.opsForValue().set("sk1", "ttttt");
        log.info("sk1 = {}", redisTemplate.opsForValue().get("sk1"));
        //redisTemplate.setEnableTransactionSupport(true); 一定要在watch之前
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.watch("sk1");
        if(redisTemplate.opsForValue().get("sk1") != null){
            redisTemplate.multi();
            redisTemplate.opsForValue().set("sk1", "v1");
            redisTemplate.opsForValue().set("sk2", "v2");
            redisTemplate.opsForValue().set("sk3", "v3");
            log.info("multi 之后，执行命令返回的结果：{}", redisTemplate.opsForValue().get("sk1"));
            List<Object> list = redisTemplate.exec();
            log.info("事务结束之后返回的结果：{}", list);
        }else{
            redisTemplate.unwatch();
        }
    }

}
