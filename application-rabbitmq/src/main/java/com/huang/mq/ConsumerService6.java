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
 * Discription: 优先级队列测试，会按照优先级的方式接收，优先级是在MQ队列里面消息瞬时的优先级来判定的，也就是说会出现下面的情况：
 *message{"name":"lisi0","priority":"18","key":"key0"}
 message{"name":"lisi3","priority":"6","key":"key3"}
 message{"name":"lisi6","priority":"12","key":"key6"}
 message{"name":"lisi2","priority":"3","key":"key2"}
 message{"name":"lisi5","priority":"4","key":"key5"}
 message{"name":"lisi4","priority":"18","key":"key4"}
 message{"name":"lisi8","priority":"13","key":"key8"}
 message{"name":"lisi1","priority":"19","key":"key1"}
 message{"name":"lisi7","priority":"1","key":"key7"}
 message{"name":"lisi24","priority":"19","key":"key24"}
 message{"name":"lisi35","priority":"19","key":"key35"}
 message{"name":"lisi17","priority":"17","key":"key17"}
 message{"name":"lisi21","priority":"17","key":"key21"}
 message{"name":"lisi23","priority":"17","key":"key23"}
 message{"name":"lisi56","priority":"19","key":"key56"}
 message{"name":"lisi32","priority":"17","key":"key32"}
 message{"name":"lisi10","priority":"16","key":"key10"}
 message{"name":"lisi39","priority":"16","key":"key39"}
 message{"name":"lisi34","priority":"16","key":"key34"}
 message{"name":"lisi42","priority":"16","key":"key42"}
 message{"name":"lisi44","priority":"16","key":"key44"}
 message{"name":"lisi45","priority":"16","key":"key45"}
 message{"name":"lisi27","priority":"15","key":"key27"}
 message{"name":"lisi47","priority":"15","key":"key47"}
 message{"name":"lisi49","priority":"15","key":"key49"}
 message{"name":"lisi52","priority":"15","key":"key52"}
 message{"name":"lisi30","priority":"14","key":"key30"}
 essage{"name":"lisi12","priority":"13","key":"key12"}
 essage{"name":"lisi16","priority":"13","key":"key16"}
 essage{"name":"lisi38","priority":"13","key":"key38"}
 message{"name":"lisi41","priority":"13","key":"key41"}
 message{"name":"lisi25","priority":"12","key":"key25"}
 essage{"name":"lisi46","priority":"10","key":"key46"}
 message{"name":"lisi53","priority":"10","key":"key53"}
 message{"name":"lisi54","priority":"10","key":"key54"}
 message{"name":"lisi58","priority":"10","key":"key58"}
 message{"name":"lisi14","priority":"8","key":"key14"}
 message{"name":"lisi11","priority":"8","key":"key11"}
 essage{"name":"lisi18","priority":"7","key":"key18"}
 message{"name":"lisi26","priority":"7","key":"key26"}
 message{"name":"lisi28","priority":"6","key":"key28"}
 message{"name":"lisi43","priority":"6","key":"key43"}
 message{"name":"lisi19","priority":"5","key":"key19"}
 message{"name":"lisi31","priority":"5","key":"key31"}
 essage{"name":"lisi40","priority":"5","key":"key40"}
 message{"name":"lisi48","priority":"5","key":"key48"}
 essage{"name":"lisi9","priority":"4","key":"key9"}
 message{"name":"lisi20","priority":"4","key":"key20"}
 message{"name":"lisi22","priority":"4","key":"key22"}
 message{"name":"lisi51","priority":"4","key":"key51"}
 essage{"name":"lisi57","priority":"4","key":"key57"}
 message{"name":"lisi15","priority":"3","key":"key15"}
 message{"name":"lisi29","priority":"3","key":"key29"}
 message{"name":"lisi33","priority":"3","key":"key33"}
 message{"name":"lisi59","priority":"3","key":"key59"}
 message{"name":"lisi37","priority":"2","key":"key37"}
 message{"name":"lisi50","priority":"2","key":"key50"}
 essage{"name":"lisi36","priority":"1","key":"key36"}
 message{"name":"lisi55","priority":"0","key":"key55"}
 message{"name":"lisi13","priority":"0","key":"key13"}

 * Created on: 2018/1/10 11:24
 * @param:
 * @return:
 * @author: <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 */
@Component
@Slf4j
public class ConsumerService6 implements ChannelAwareMessageListener{

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    @RabbitListener(queues = {"priority_queue"})
    public void onMessage(Message message, Channel channel) throws Exception {
        executor.execute(() -> {
            log.info("接收的消息：tagId:{}, message{}", message.getMessageProperties().getDeliveryTag(), new String(message.getBody()));
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                log.info("tagId:{}, message:{} ack ok", message.getMessageProperties().getDeliveryTag(), new String(message.getBody()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
