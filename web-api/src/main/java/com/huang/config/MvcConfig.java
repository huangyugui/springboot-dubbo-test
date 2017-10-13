package com.huang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * User: Administrator
 * Date: 2017/10/13
 * Description:
 */

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(300000*1000L); //tomcat默认10秒
        configurer.setTaskExecutor(mvcTaskExecutor());//所借助的TaskExecutor
    }

    @Bean
    public ThreadPoolTaskExecutor mvcTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setQueueCapacity(20);
        executor.setMaxPoolSize(100);
        executor.initialize();
        return executor;

    }
}
