package com.huang;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.bcel.internal.generic.LREM;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created on 2017/9/26 14:01
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 *          Copyright (c) 2017 国美金控-美借
 */
@RunWith(SpringJUnit4ClassRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = RedisApplication.class)// 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration//由于是Web项目，Junit需要模拟ServletContext,因此我们需要给我们的测试类加上@WebAppConfiguration
@Slf4j
public class StringRedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource(name="redisTemplate")
    private ListOperations<String, Person> listOperations;

    @Resource(name="redisTemplate")
    private SetOperations<String, String> setOperations;

    @Resource(name="redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Long> hashOperations1;

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
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("key1", "value1");
        Map<String, String> map = new HashMap<String, String>();
        map.put("key2", "value2");
        map.put("key3", "value3");
        valueOperations.multiSet(map);
//        valueOperations.set("key4", "value4", 6000000);//最后一个值是偏移量
        valueOperations.set("key5", "value5", 60, TimeUnit.DAYS);
        redisTemplate.delete("key4");
        String value = (String) valueOperations.getAndSet("key2", "getAndSet");
        log.info(value);
        List<String> keys = Arrays.asList("key2", "key3", "key5");
        List<String> values = valueOperations.multiGet(keys);
        log.info(JSON.toJSONString(values));
        redisTemplate.expire("key3", 6000, TimeUnit.SECONDS);
        log.info(redisTemplate.getExpire("key3") + "");
        log.info(redisTemplate.getExpire("key3", TimeUnit.HOURS) + "");
        log.info("increment + 1 = {}", valueOperations.increment("key6", 1));
        log.info("increment + 1 = {}", valueOperations.increment("key6", 1));
        log.info("increment + 1 = {}", valueOperations.increment("key6", 1));
        log.info("increment + 1.1 = {}", valueOperations.increment("key7", 1.1d));
        log.info("increment + 1.1 = {}", valueOperations.increment("key7", 1.1d));
        log.info("increment + 1.1 = {}", valueOperations.increment("key7", 1.1d));
        BoundValueOperations bvo = redisTemplate.boundValueOps("key8");
        bvo.set("7");
        log.info("increment 7 + 2 = {}", bvo.increment(2));

    }

    @Test
    public void test2() {
        //LPUSH
        listOperations.leftPush("lk1", new Person("1", "zhangsan"));
        listOperations.leftPush("lk1", new Person("2", "lisi"));
        //LPOP
        log.info(listOperations.leftPop("lk1").toString());
        //LINDEX
        log.info("从左开始第二个元素： {}", listOperations.index("lk1", 1));

        listOperations.rightPush("lk1", new Person("3", "wangwu"));
        listOperations.rightPush("lk1", new Person("6", "wangwu6"));
        listOperations.rightPush("lk1", new Person("7", "wangwu7"));
        listOperations.rightPush("lk1", new Person("8", "wangwu8"));
        listOperations.rightPush("lk1", new Person("9", "wangwu9"));
        //LSET
        listOperations.set("lk1", 1, new Person("5", "songqi"));
        //LINSERT
        listOperations.leftPush("lk1", new Person("3", "wangwu"), new Person("6", "wangwu6"));//在3之前加入4
        //LRANGE
        log.info("list: {}", listOperations.range("lk1", 0, 9));
        //LREM
        listOperations.remove("lk1", 2, new Person("6", "wangwu6"));//删除和6相同的元素，
        log.info("list: {}", listOperations.range("lk1", 0, 9));
        //LTRIM
        listOperations.trim("lk1", 0, 3);
        log.info("list: {}", listOperations.range("lk1", 0, 9));
        listOperations.trim("lk1", -2, -1);
        log.info("list: {}", listOperations.range("lk1", 0, 9));
        //BLPOP
        log.info("blpop: {}", listOperations.leftPop("lk1", 10, TimeUnit.SECONDS));
        //如果所有给定 key 都不存在或包含空列表，那么 BLPOP 命令将阻塞连接，直到等待超时，或有另一个客户端对给定 key 的任意一个执行 LPUSH 或 RPUSH 命令为止。
        log.info("blpop: {}", listOperations.leftPop("lk2", 20, TimeUnit.SECONDS));
    }

    @Test
    public void test3(){
        listOperations.leftPush("lk2", new Person("10", "10"));
    }

    @Test
    public void test4(){
        setOperations.add("sk1", "v1", "v2", "v3", "v3", "v4");
        setOperations.add("sk2", "v1", "v2", "v5", "v6", "v7");
        log.info("sk1 的元素个数：{}", setOperations.size("sk1"));
        log.info("sk1 = {}", setOperations.members("sk1"));
        log.info("sk1 和 sk2 的交集= {}", setOperations.intersect("sk1", "sk2"));
        log.info("sk1 和 sk2 的差集= {}", setOperations.difference("sk1", "sk2"));
        log.info("v2 存在在 sk1 中否 = {}", setOperations.isMember("sk1", "v2"));
        setOperations.move("sk1", "v3", "sk2");
        log.info("sk2 = {}", setOperations.members("sk2"));
        log.info("sk1 随机移除一个元素后：{}", setOperations.pop("sk1"));//移除随机一个元素
        log.info("sk1 随机获取两个元素后：{}", setOperations.distinctRandomMembers("sk1", 2));//移除获取一个元素，但是并不删除
        setOperations.remove("sk2", "v2", "v5");
        log.info("sk2 删除几个元素后：{}", setOperations.members("sk2"));
        log.info("sk1 和 sk2 的并集：{}", setOperations.unionAndStore("sk1", "sk2", "sk3"));
        //增量迭代
        Cursor<String> curosr = setOperations.scan("sk3", ScanOptions.scanOptions().count(20).build());
        while(curosr.hasNext()){
            System.out.println(curosr.next());
        }
    }

    @Test
    public void test5(){
        zSetOperations.add("zk1", "v1", 1d);
        zSetOperations.add("zk1", "v3", 2d);
        zSetOperations.add("zk1", "v6", 3d);
        zSetOperations.add("zk1", "v7", 4d);
        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        set.add(new DefaultTypedTuple("v1", (double) 1));
        set.add(new DefaultTypedTuple("v2", 2d));
        set.add(new DefaultTypedTuple<>("v3", 3d));
        set.add(new DefaultTypedTuple<>("v4", 4d));
        set.add(new DefaultTypedTuple<>("v5", 5d));
        zSetOperations.add("zk2", set);
        zSetOperations.unionAndStore("zk1", "zk2", "zk3");
        log.info("zk1 union zk2 = {}", zSetOperations.range("zk3", 0, -1));
        log.info("增加zk1.v6.score + 3 ={}", zSetOperations.incrementScore("zk1", "v6", 3));
        log.info("zk1 的元素： {}", zSetOperations.range("zk1", 0, -1));
        log.info("增加zk1中 min 和max之间成员的数量 ={}", zSetOperations.count("zk1", 2, 4));
        log.info("zk1 成员数量：{}", zSetOperations.zCard("zk1"));
        log.info("zk1.v6 的排名：{}", zSetOperations.rank("zk1", "v6"));//从0开始
        zSetOperations.removeRange("zk1", 0, 1);
        log.info("删除 zk1 里面排名 0 ~1 之间的数据：{}", zSetOperations.range("zk1", 0, -1));
        zSetOperations.removeRangeByScore("zk1", 2, 4);
        log.info("删除 zk1 里面score 在2 ~4 之间的数据：{}", zSetOperations.range("zk1", 0, -1));
        log.info("反转zk2 的元素： {}", zSetOperations.reverseRange("zk2", 0, 3));
        log.info("zk2.v5 的score值为： {}", zSetOperations.score("zk2", "v5"));

        zSetOperations.add("zk4", "c", 0);
        zSetOperations.add("zk4", "b", 0);
        zSetOperations.add("zk4", "d", 0);
        zSetOperations.add("zk4", "f", 0);
        log.info("zk4 的元素： {}", zSetOperations.range("zk4", 0, -1));
        //当有序集合的所有成员都具有相同的分值时， 有序集合的元素会根据成员的字典序（lexicographical ordering）来进行排序， 而这个命令则可以返回给定的有序集合键 key 中， 值介于 min 和 max 之间的成员。
        log.info("zk4 中 (v1, v6] = {}", zSetOperations.rangeByLex("zk4", RedisZSetCommands.Range.range().lte("d").gt("b")));
    }

    @Test
    public void test6(){
        Map<String, String> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");
        hashOperations.putAll("hk1", map);
        log.info("hk1 = {}", hashOperations.entries("hk1"));
        hashOperations.delete("hk1", "k1", "k2");
        log.info("hk1 = {}", hashOperations.get("hk1", "k3"));
        log.info("hk1 has k4 = {}", hashOperations.hasKey("hk1", "k4"));
        hashOperations.increment("hk1", "k5", 2l);
//        log.info("hk1.k5 = {}", hashOperations.get("hk1", "k5"));
        log.info("hk1.keys = {}", hashOperations.keys("hk1"));

        Map<String, Long> map1 = new HashMap<>();
        map1.put("k1", 2l);
        map1.put("k2", 3l);
        map1.put("k3", 1l);
        hashOperations1.putAll("hk2", map1);
        hashOperations1.increment("hk2", "k5", 2l);
        log.info("hk2.k5 = {}", hashOperations.getOperations().boundHashOps("hk2").get("k5"));
    }

}
