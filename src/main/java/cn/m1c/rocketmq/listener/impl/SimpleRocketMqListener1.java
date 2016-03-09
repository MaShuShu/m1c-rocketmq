package cn.m1c.rocketmq.listener.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

import cn.m1c.core.util.StringUtil;
import cn.m1c.rocketmq.listener.RocketMqMessageListener;

/**
 * 业务监听实现Demo
 * Created by wangxingzhe on 2015/11/3.
 */
public class SimpleRocketMqListener1 implements RocketMqMessageListener {
	Logger logger = LoggerFactory.getLogger(getClass());
    public boolean onMessage(List<MessageExt> messages, ConsumeConcurrentlyContext Context) {
    	logger.info("------------------------------------------------------");
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            logger.info("consumer1 consum produce detail:[{}]",StringUtil.getString(msg.getBody()));
        }
        return true;
    }
}
