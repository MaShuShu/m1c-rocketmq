package cn.m1c.rocketmq.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;

import cn.m1c.core.util.StringUtil;
import cn.m1c.rocketmq.MyProducer;

@Controller
@RequestMapping("rocket")
public class RocketAction {
	@Resource(name = "myProducer")
	protected MyProducer myProducer;
	Logger logger = LoggerFactory.getLogger(getClass());
	@RequestMapping("sendmsg")
	private void sendMsg(String topic) {
		logger.info("------------------------------------------------------");
		logger.info("topic:[{}]",topic);
//		"MyTopic1"
		 Message msg = new Message(topic, "MyTag", (("hello rocketmq")).getBytes());
	        SendResult sendResult = null;
	        try {
	            sendResult = myProducer.getDefaultMQProducer().send(msg);
	            logger.info("action response and produce msg win detail :[{}]",StringUtil.getString(msg.getBody()));
	        } catch (Exception e) {
	            logger.error(JSON.toJSONString(sendResult),e);
	        }
	        // 当消息发送失败时如何处理
	        if (sendResult == null || sendResult.getSendStatus() != SendStatus.SEND_OK) {
	            // TODO
	        }
	}
}
