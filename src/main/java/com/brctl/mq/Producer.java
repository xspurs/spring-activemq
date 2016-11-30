package com.brctl.mq;

import com.brctl.vo.ReplyMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Orclover on 2016-11-30.
 */
//@Service
public class Producer implements MessageListener {

    private JmsTemplate jmsTemplate;
    private Destination requestDestination;
    private Destination replyDestination;

    // 以此作为消息队列？
    private ConcurrentMap<String, ReplyMessage> concurrentMap = new ConcurrentHashMap<>();


    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Destination getRequestDestination() {
        return requestDestination;
    }

    public void setRequestDestination(Destination requestDestination) {
        this.requestDestination = requestDestination;
    }

    public Destination getReplyDestination() {
        return replyDestination;
    }

    public void setReplyDestination(Destination replyDestination) {
        this.replyDestination = replyDestination;
    }


    public String sendMessage(final String message) {
        ReplyMessage replyMessage = new ReplyMessage();
        final String correlationID = UUID.randomUUID().toString();
        concurrentMap.put(correlationID, replyMessage);

        jmsTemplate.send(requestDestination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                Message msg = session.createTextMessage(message);
                msg.setJMSCorrelationID(correlationID);
                msg.setJMSReplyTo(replyDestination);
                return msg;
            }
        });

        try {
            boolean isReceiveMessage = replyMessage.getSemaphore().tryAcquire(10, TimeUnit.SECONDS);

            ReplyMessage result = concurrentMap.get(correlationID);

            if (isReceiveMessage && null != result) {
                Message msg = result.getMessage();
                if (null != msg) {
                    TextMessage textMessage = (TextMessage) msg;
                    return textMessage.getText();
                }
            }

        } catch (InterruptedException e) {

        } catch (JMSException e) {

        }
        return null;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                concurrentMap.get(textMessage.getJMSCorrelationID()).setMessage(textMessage);
                concurrentMap.get(textMessage.getJMSCorrelationID()).getSemaphore().release();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
    }

}
