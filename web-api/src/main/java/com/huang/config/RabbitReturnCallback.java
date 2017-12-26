package com.huang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Created on 2017/12/25 17:35
 *
 * @author <a href="mailto: huangyugui@gomeholdings.com">黄渝贵</a>
 * @version 1.0
 */
@Component
@Slf4j
public class RabbitReturnCallback implements RabbitTemplate.ReturnCallback {

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("\nmessage:{}\nreplyCode:{}\nreplyText:{}\nexchange:{}\nroutingKey:{}\n", new String(message.getBody()),
                replyCode, replyText, exchange, routingKey);
    }
}
