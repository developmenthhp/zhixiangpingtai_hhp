package com.zhixiangmain.config;

import com.zhixiangmain.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hhp
 * @Title: RabbitConfiguration
 * @Description: mq 队列，配置
 * @return:
 * @throws
 */
@Configuration
public class RabbitConfiguration {

	// 声明三离警报队列
	@Bean
	public Queue tlAlarmQueue() {
		return new Queue(RabbitConstant.THREE_LEAVE_ALARM_QUEUE, true); // true表示持久化该队列
	}
	// 声明挡鼠板警报队列
	@Bean
	public Queue ratAlarmQueue() {
		return new Queue(RabbitConstant.RAT_ALARM_QUEUE, true); // true表示持久化该队列
	}
	// 声明交互器
	@Bean
    TopicExchange topicExchange() {
		return new TopicExchange(RabbitConstant.TOPICEXCHANGE_TLALARM);
	}
	// 声明交互器
	@Bean
	TopicExchange topicRatExchange() {
		return new TopicExchange(RabbitConstant.TOPICEXCHANGE_RATALARM);
	}

	@Bean
	public Binding bindingThreeLeaveAlarm() {
		return BindingBuilder.bind(tlAlarmQueue()).to(topicExchange()).with(RabbitConstant.THREE_LEAVE_ALARM_KEY);
	}

	@Bean
	public Binding bindingRatAlarm() {
		return BindingBuilder.bind(ratAlarmQueue()).to(topicRatExchange()).with(RabbitConstant.RAT_ALARM_KEY);
	}

	//接收对象时使用
	/*@Bean
	public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(new MessageConverter() {
			@Override
			public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
				return null;
			}

			@Override
			public Object fromMessage(Message message) throws MessageConversionException {
				System.out.println(message+",,,,,,,,,,,,,,,,,");
				try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(message.getBody()))){
					//return (User)ois.readObject();
					return ois.readObject();
				}catch (Exception e){
					e.printStackTrace();
					return null;
				}
			}
		});

		return factory;
	}*/

}
