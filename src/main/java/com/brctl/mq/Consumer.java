package com.brctl.mq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * Created by Orclover on 2016-11-30.
 */
//@Service
public class Consumer implements MessageListener {

    private JmsTemplate jmsTemplate;

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                final String request = textMessage.getText();
                Destination destination = textMessage.getJMSReplyTo();
                final String jmsCorrelationId = textMessage.getJMSCorrelationID();
                jmsTemplate.send(destination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        Message createdMessage = session.createTextMessage(request + "'s reply!");
                        createdMessage.setJMSCorrelationID(jmsCorrelationId);
                        return createdMessage;
                    }
                });
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
    }
}
