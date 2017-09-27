package com.huang;

import com.huang.controller.OrderController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Description:
 * Created on 2017/9/27 21:11
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 *          Copyright (c) 2017 国美金控-美借
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebApiApplication.class)
@WebAppConfiguration // 使用@WebIntegrationTest注解需要将@WebAppConfiguration注释掉
@Slf4j
public class WebTest {

    @Autowired
    private OrderController controller;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test() throws Exception {
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get("/order/query/11")
                    .accept(MediaType.APPLICATION_JSON)).andReturn();
        int code = result.getResponse().getStatus();
        String body = result.getResponse().getContentAsString();
        log.info("status: {}, body: {}", code, body);
    }

}
