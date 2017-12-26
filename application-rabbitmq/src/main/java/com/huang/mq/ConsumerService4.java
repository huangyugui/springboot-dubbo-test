package com.huang.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:可以通过设置一下参数，来进行多线程消费：
 *  concurrency: 1  #对每个listener在初始化的时候设置的并发消费者的个数，就是在rabbitmq中会建立5个消费者， 如果对消息的顺序有要求，勿使用此属性
    max-concurrency: 10
    prefetch: 3  #每次投递给消费者的消息的个数
 * 经过测试，在concurrency =1   prefetch = 3的情况下，第一次投递过来时是按照顺序，如果被否定了之后，重新放回队列里面再投递的时候并不会按照顺序来
 *
 * 另外如果prefetch设置为>1,则一次拿去多个消息，然后就会阻塞在那里，直到某个消息消费完毕，才去取下一个消息
 *
 * Created on 2017/8/11 10:44
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
@Component
@Slf4j
public class ConsumerService4 implements ChannelAwareMessageListener{

    private ExecutorService executor = Executors.newFixedThreadPool(10);
    private AtomicInteger count = new AtomicInteger(1);

    @Override
//    @RabbitListener(queues = {"${rabbitmq.queue.exception1}"})
    @RabbitListener(queues = {"${rabbitmq.queue.exception}", "${rabbitmq.queue.exception1}"})
    public void onMessage(Message message, Channel channel) throws Exception {
        executor.execute(() -> {
            log.info("接收的消息：tagId:{}, message{}", message.getMessageProperties().getDeliveryTag(), new String(message.getBody()));
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                /**
                 * 此处的multiple为true时：将一次性ack所有小于deliveryTag的消息
                 */
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.info("tagId:{}, message:{} ack ok", message.getMessageProperties().getDeliveryTag(), new String(message.getBody()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
