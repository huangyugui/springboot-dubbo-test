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

/**
 * Description:可以通过设置一下参数，来进行多线程消费：
 *  concurrency: 1  #对每个listener在初始化的时候设置的并发消费者的个数，就是在rabbitmq中会建立5个消费者， 如果对消息的顺序有要求，勿使用此属性
    max-concurrency: 10
    prefetch: 3  #每次投递给消费者的消息的个数
 * 经过测试，在concurrency =1   prefetch = 3的情况下，第一次投递过来时是按照顺序，如果被否定了之后，重新放回队列里面再投递的时候并不会按照顺序来
 *
 * Created on 2017/8/11 10:44
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
//@Component
@Slf4j
public class ConsumerService3 implements ChannelAwareMessageListener{

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    @Override
//    @RabbitListener(queues = {"${rabbitmq.queue.exception}"})
    @RabbitListener(queues = {"${rabbitmq.queue.exception}", "${rabbitmq.queue.exception1}"})
    public void onMessage(Message message, Channel channel) throws Exception {
        executor.execute(() -> {
            log.info("接收的消息：{}", new String(message.getBody()));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);//手动否认消息,消息会被无限次重新接收,直到确认消息
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
