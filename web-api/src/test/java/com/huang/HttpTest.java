package com.huang;

import com.huang.common.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created on 2017/10/13 17:44
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 *          Copyright (c) 2017 国美金控-美借
 */
@Slf4j
public class HttpTest {

    public static void main(String[] args){
        log.info("开始了");
        for(int i = 0; i < 100; i++){
            new Thread(() -> {
                try {
                    int radom = new Random().nextInt(1000);
                    log.info("" + radom);
                    TimeUnit.MILLISECONDS.sleep(radom);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpClientUtil.httpGetRequest("http://localhost:8090/mj/order/test");
            }).start();
        }
        log.info("结束了");
    }
}
