package com.zhixiangmain.config;

import com.zhixiangmain.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @author hhp
 * @Title: RabbitConfiguration
 * @Description: mq 队列，配置
 * @return:
 * @throws
 */
@Configuration
public class RabbitConfiguration {

	// 声明队列
	@Bean
	public Queue emailQueue() {
		return new Queue(RabbitConstant.EMAIL_QUEUE, true); // true表示持久化该队列
	}

	// 声明交互器
	@Bean
    TopicExchange topicExchange() {
		return new TopicExchange(RabbitConstant.TOPICEXCHANGE);
	}

	// 绑定
	@Bean
	public Binding binding1() {
		return BindingBuilder.bind(emailQueue()).to(topicExchange()).with(RabbitConstant.EMAIL_ROUTING_KEY);
	}

	//
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
				try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(message.getBody()))){
					return (User)ois.readObject();
				}catch (Exception e){
					e.printStackTrace();
					return null;
				}
			}
		});

		return factory;
	}*/

}
