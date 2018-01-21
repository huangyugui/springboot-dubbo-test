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
 * 手动确认的方式下：
 * 如果不手动确认，也不抛出异常，消息不会自动重新推送（包括其他消费者），
 * 因为对于rabbitmq来说始终没有接收到消息消费是否成功的确认，并且Channel是在消费端有缓存的，没有断开连接
 *
 * 如果rabbitmq断开，连接后会自动重新推送（不管是网络问题还是宕机）
 * 如果消费端应用重启，消息会自动重新推送
 *
 * 如果监听消息的方法抛出异常，消息会按照listener.retry的配置进行重发，但是重发次数完了之后还抛出异常的话，
 * 消息不会重发（也不会重发到其他消费者），只有应用重启后会重新推送。因为retry是消费端内部处理的，
 * 包括异常也是内部处理，对于rabbitmq是不知道的（此场景解决方案后面有）
 *
 * spring.rabbitmq.listener.retry配置的重发是在消费端应用内处理的，不是rabbitqq重发

 可以配置MessageRecoverer对异常消息进行处理，此处理会在listener.retry次数尝试完并还是抛出异常的情况下才会调用，
 默认有两个实现：
    1、RepublishMessageRecoverer：将消息重新发送到指定队列，需手动配置，

    2、RejectAndDontRequeueRecoverer：如果不手动配置MessageRecoverer，会默认使用这个，实现仅仅是将异常打印抛出，源码如下：

 可以通过给队列（Queue）绑定死信队列，使用nack反馈给mq，会将消息转发到死信队列里面，此种方式需要自己在消费消息的方法内部将异常处理掉

    详情：http://dev.dafan.info/detail/575967?p=85
 * Created on 2017/8/11 10:44
 *
 * @author <a href="mailto: yugui_huang0305@163.com">黄渝贵</a>
 * @version 1.0
 *
 */
//@Component
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
