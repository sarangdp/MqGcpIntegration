package org.example;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.jms.JmsIO;
import org.apache.beam.sdk.io.jms.JmsRecord;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

public class MqIntegration {
    private static final Logger LOG = LoggerFactory.getLogger(MqIntegration.class);

    private static final String HOST = "34.125.15.134";
    private static final Integer PORT = 1414;
    private static final String USERNAME = "mqm";
    private static final String PASSWORD = "passw0rd";
    private static final String QUEUE_MANAGER = "QM1";
    private static final String CHANNEL = "DEV.ADMIN.SVRCONN";
    private static final String QUEUE = "DEV.QUEUE.1";
    private static final MQConnectionFactory connectionFactory = new MQConnectionFactory();

    public static void init() throws JMSException {
        connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        connectionFactory.setHostName(HOST);
        connectionFactory.setPort(PORT);
        connectionFactory.setQueueManager(QUEUE_MANAGER);
        connectionFactory.setChannel(CHANNEL);
    }

    public static class FormatAsTextFn extends SimpleFunction<KV<String, Long>, String> {
        @Override
        public String apply(KV<String, Long> input) {
            return input.getKey() + ": " + input.getValue();
        }
    }

    public static void main(String[] args) {
        Pipeline p = Pipeline.create(
                PipelineOptionsFactory.fromArgs(args).withValidation().create());
        try {
            init();
            p.apply(JmsIO.read()
                            .withConnectionFactory(connectionFactory)
                            .withQueue(QUEUE)
                            .withMaxNumRecords(5))

                    .apply(MapElements.via(new SimpleFunction<JmsRecord, KV<Long, String>>() {
                        public KV<Long, String> apply(JmsRecord input) {
                            KV kv = KV.of(System.currentTimeMillis(), input.getPayload());
                            System.out.println("Key:" + kv.getKey() + " and value:" + kv.getValue());
                            return kv;
                        }
                    }));
            p.run();
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
