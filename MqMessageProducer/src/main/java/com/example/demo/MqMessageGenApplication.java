package com.example.demo;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;

@SpringBootApplication
@RestController
@EnableJms
public class MqMessageGenApplication {

    private static final String HOST = "34.125.15.134";
    private static final Integer PORT = 1414;

    private static final String USERNAME = "mqm";
    private static final String PASSWORD = "passw0rd";
    private static final String QUEUE_MANAGER = "QM1";
    private static final String CHANNEL = "DEV.ADMIN.SVRCONN";

    private static final String QUEUE = "DEV.QUEUE.1";
    private MQConnectionFactory connectionFactory;

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {

        SpringApplication.run(MqMessageGenApplication.class, args);
        System.out.println("Hello World!!");
    }

    public void init() throws JMSException {
        connectionFactory = new MQConnectionFactory();
        connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        connectionFactory.setHostName(HOST);
        connectionFactory.setPort(PORT);
        connectionFactory.setQueueManager(QUEUE_MANAGER);
        connectionFactory.setChannel(CHANNEL);
    }

    public void sendMessages() throws JMSException {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue q2 = session.createQueue(QUEUE);
        System.out.println("Queue:" + q2.toString());
        MessageProducer producer = session.createProducer(q2);
        for (int i = 1; i <= 10; i++) {
            TextMessage message = session.createTextMessage("This is message number " + i + ".");
            producer.send(message);
        }
    }

    @GetMapping("send")
    String send() {
        try {
            //jmsTemplate.convertAndSend(QUEUE, "Hello World!");
            init();
            sendMessages();
            return "OK";
        } catch (JmsException ex) {
            ex.printStackTrace();
            return "FAIL";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    @GetMapping("recv")
    String recv() {
        try {
            return jmsTemplate.receiveAndConvert(QUEUE).toString();
        } catch (JmsException ex) {
            ex.printStackTrace();
            return "FAIL";
        }
    }

}

