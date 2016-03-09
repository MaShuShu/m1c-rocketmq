package cn.m1c.rocketmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

import cn.m1c.rocketmq.listener.RocketMqMessageListener;
import cn.m1c.rocketmq.listener.RocketMqMessageWrapper;

public class MyConsumer {

    private final Logger logger = LoggerFactory.getLogger(MyConsumer.class);

    private DefaultMQPushConsumer defaultMQPushConsumer;
    private String namesrvAddr;
    private String consumerGroup;
    
    private String topic;
    private String subExpression;
    private RocketMqMessageListener rocketMqMessageListener;
    
    /**
     * Spring bean init-method
     */
    public void init() throws InterruptedException, MQClientException {

        // 参数信息
        logger.info("---------------------------------------------------------");
        logger.info("initialize rocketConsumer start...{}",this);
        logger.info(consumerGroup);
        logger.info(namesrvAddr);
        logger.info(topic);
        logger.info(subExpression);
        logger.info(rocketMqMessageListener.getClass().getName());

        // 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
        // 注意：ConsumerGroupName需要由应用来保证唯一
        defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setInstanceName(String.valueOf(System.currentTimeMillis()));

        // 订阅指定MyTopic下tags等于MyTag

        defaultMQPushConsumer.subscribe(topic, subExpression);

        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 设置为集群消费(区别于广播消费)
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        
        RocketMqMessageWrapper rocketMqMessageWrapper=new RocketMqMessageWrapper();
        rocketMqMessageWrapper.setRocketMqMessageListener(this.rocketMqMessageListener);
        if(this.rocketMqMessageListener==null){
            throw new RuntimeException("please define a rocketMqMessageListener for consumer!");
        }
        defaultMQPushConsumer.registerMessageListener(rocketMqMessageWrapper);
        // Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
        defaultMQPushConsumer.start();
        logger.info("DefaultMQPushConsumer start success!");
    }
    /**
     * Spring bean destroy-method
     */
    public void destroy() {
        defaultMQPushConsumer.shutdown();
    }

    // ----------------- setter --------------------

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

	public void setDefaultMQPushConsumer(DefaultMQPushConsumer defaultMQPushConsumer) {
		this.defaultMQPushConsumer = defaultMQPushConsumer;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setSubExpression(String subExpression) {
		this.subExpression = subExpression;
	}

	public void setRocketMqMessageListener(RocketMqMessageListener rocketMqMessageListener) {
		this.rocketMqMessageListener = rocketMqMessageListener;
	}

}