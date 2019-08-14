package com.zhixiangmain.component;


import com.zhixiangmain.App;
import com.zhixiangmain.constant.RabbitConstant;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author hhp
 * @Title: Sender
 * @Description: 向mq发送消息
 * @param:
 * @return:
 * @throws
 */
@Component
public class Sender implements RabbitTemplate.ConfirmCallback, ReturnCallback {

	private static Logger logger = Logger.getLogger(App.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@PostConstruct
	public void init() {
		rabbitTemplate.setConfirmCallback(this);
		rabbitTemplate.setReturnCallback(this);
	}

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		if (ack) {
			logger.info("消息发送成功:" + correlationData);
		} else {
			logger.info("消息发送失败:" + cause);
		}

	}

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		logger.error(message.getMessageProperties() + " 发送失败");
	}

	// 发送消息，
	public void sendThreeWall(Object object) {
		CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
		logger.info("开始发送消息 : " + object);
		// 同步消息
		//String response = rabbitTemplate.convertSendAndReceive(RabbitConstant.TOPICEXCHANGE, RabbitConstant.EMAIL_ROUTING_KEY, mailBean, correlationId).toString();
		//异步消息
		//rabbitTemplate.convertAndSend(RabbitConstant.TOPICEXCHANGE, RabbitConstant.THREE_LEAVE_ALARM_KEY, object, correlationId);
		/*MailBean mailBean = new MailBean();
		mailBean.setSubject("s  lasdf you jian");
		mailBean.setText("lsdjf");*/
		rabbitTemplate.convertAndSend(RabbitConstant.TOPICEXCHANGE_TLALARM, RabbitConstant.THREE_LEAVE_ALARM_KEY, object, correlationId);
		logger.info("rabbitmq消息已经发送到交换机, 等待交换机接受...");
		logger.info("结束发送消息 : ");
		//logger.info("消费者响应 : " + response + " 消息处理完成");
	}

	// 发送挡鼠板
	public void sendRatAlarm(Object object) {
		CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
		logger.info("开始发送挡鼠板消息 : " + object);
		// 同步消息
		rabbitTemplate.convertAndSend(RabbitConstant.TOPICEXCHANGE_RATALARM, RabbitConstant.RAT_ALARM_KEY, object, correlationId);
		logger.info("rabbitmq挡鼠板消息已经发送到交换机, 等待交换机接受...");
		logger.info("结束发送挡鼠板消息 : ");
		//logger.info("消费者响应 : " + response + " 消息处理完成");
	}
}