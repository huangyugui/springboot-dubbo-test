package com.huang;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableAsync
public class WebApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApiApplication.class, args);
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        fastConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }

    @Bean
    public ThreadPoolTaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10);
        executor.setKeepAliveSeconds(5);
        executor.setThreadNamePrefix("thread-pool-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @EventListener
    public void event(ContextClosedEvent event){
        ThreadPoolTaskExecutor executor = executor();
        executor.shutdown();
        try {
            if (!executor.getThreadPoolExecutor().awaitTermination(30l, TimeUnit.SECONDS)) {
                executor.getThreadPoolExecutor().shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 没起到太大的作用，经过测试，感觉是上面的线程池先关闭的，然后才是下面的容器连接池关闭，因此当优雅关机之后还是可以继续
     * 接受请求，因此下面的还没执行，所以connector没有终止
     */
//	@Bean
//	public GracefulShutdown gracefulShutdown() {
//		return new GracefulShutdown();
//	}
//
//	@Bean
//	public EmbeddedServletContainerCustomizer tomcatCustomizer() {
//		return new EmbeddedServletContainerCustomizer() {
//
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
//				if (container instanceof TomcatEmbeddedServletContainerFactory) {
//					((TomcatEmbeddedServletContainerFactory) container)
//							.addConnectorCustomizers(gracefulShutdown());
//				}
//
//			}
//		};
//	}
//
//	private static class GracefulShutdown implements TomcatConnectorCustomizer,
//			ApplicationListener<ContextClosedEvent> {
//
//		private static final Logger log = LoggerFactory.getLogger(GracefulShutdown.class);
//
//		private volatile Connector connector;
//
//		@Override
//		public void customize(Connector connector) {
//			this.connector = connector;
//		}
//
//		@Override
//		public void onApplicationEvent(ContextClosedEvent event) {
//			this.connector.pause();
//			Executor executor = this.connector.getProtocolHandler().getExecutor();
//			if (executor instanceof ThreadPoolExecutor) {
//				try {
//					ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
//					threadPoolExecutor.shutdown();
//					log.info("开始关闭线程池------------------------");
//					if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
//						log.warn("Tomcat thread pool did not shut down gracefully within "
//								+ "30 seconds. Proceeding with forceful shutdown");
//					}
//				}
//				catch (InterruptedException ex) {
//					Thread.currentThread().interrupt();
//				}
//			}
//		}
//
//	}
}
