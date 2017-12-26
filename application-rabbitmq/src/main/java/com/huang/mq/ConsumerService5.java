package com.huang.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


//@Component
@Slf4j
public class ConsumerService5 implements ChannelAwareMessageListener{

    private ExecutorService executor = Executors.newFixedThreadPool(10);
    private AtomicInteger count = new AtomicInteger(1);

    @Override
    @RabbitListener(queues = {"${rabbitmq.queue.exception1}"})
//    @RabbitListener(queues = {"${rabbitmq.queue.exception}", "${rabbitmq.queue.exception1}"})
    public void onMessage(Message message, Channel channel) throws Exception {
        executor.execute(() -> {
            log.info("接收的消息：tagId:{}, message{}", message.getMessageProperties().getDeliveryTag(), new String(message.getBody()));
            try {
                TimeUnit.SECONDS.sleep(count.getAndIncrement());
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

}
