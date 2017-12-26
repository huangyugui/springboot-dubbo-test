package com.huang.mq.other;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//@Configuration
//@RabbitListener(queues = {"exception_queue1"})
@Slf4j
public class Listener {

    private AtomicInteger count = new AtomicInteger(1);

    /** 设置交换机类型  */  
    @Bean
    public DirectExchange defaultExchange() {
        /** 
         * DirectExchange:按照routingkey分发到指定队列 
         * TopicExchange:多关键字匹配 
         * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念 
         * HeadersExchange ：通过添加属性key-value匹配 
         */  
        return new DirectExchange("exception_exchange");
    }  
  
    @Bean
    public Queue fooQueue() {
        return QueueBuilder.durable("exception_queue")
                .withArgument("x-dead-letter-exchange", "dle")
                .withArgument("x-dead-letter-routing-key", "dlk")
                .build();
    }

    @Bean
    public Queue fooQueue1() {
        return new Queue("exception_queue1");
    }

    @Bean
    public Binding binding() {
        /** 将队列绑定到交换机 */  
        return BindingBuilder.bind(fooQueue()).to(defaultExchange()).with("exception_pushkey");
    }

    private ExecutorService executor = Executors.newFixedThreadPool(10);
   
    /**
     * 接受消息的监听，这个监听会接受消息队列FOO_QUEUE的消息
     * 针对消费者配置  
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(fooQueue1());
        container.setExposeListenerChannel(true);  
        container.setMaxConcurrentConsumers(3);
        container.setConcurrentConsumers(3);
        container.setPrefetchCount(3);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(new ChannelAwareMessageListener() {
            public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
                executor.execute(() -> {
                    log.info("接收的消息：tagId:{}, message{}", message.getMessageProperties().getDeliveryTag(), new String(message.getBody()));
                    try {
                        TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                        log.info("tagId:{}, message:{} ack ok", message.getMessageProperties().getDeliveryTag(), new String(message.getBody()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }  

        });  
        return container;  
    }  

}  