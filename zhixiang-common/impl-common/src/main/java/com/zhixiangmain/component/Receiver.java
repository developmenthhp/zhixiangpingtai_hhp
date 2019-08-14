package com.zhixiangmain.component;

import com.rabbitmq.client.Channel;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.constant.RabbitConstant;
import com.zhixiangmain.module.base.mail.MailBean;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Component
public class Receiver {
	//private static Logger logger = Logger.getLogger(App.class);
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(Receiver.class);
	@Autowired
	private MailSenderUtil mailSenderUtil;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	//因为使用的是异步消息机制，故返回值是void
	@RabbitListener(queues = RabbitConstant.EMAIL_QUEUE)
	public void processEmail(Object object, Message message, Channel channel) throws IOException {
		MailBean mailBean = (MailBean) object;
		logger.info(Thread.currentThread().getName() + " 接收到来自email队列的消息：" + mailBean);
		if(!IsEmptyUtils.isEmpty(mailBean.getUploadFilePath())){
			String[] filePathName = mailBean.getUploadFilePath().split(",");
			try {
				mailBean.setAttachmentFilename(filePathName[1]);
				File file = new File(filePathName[0]);
				if(file.exists()){
					mailBean.setFile(new FileSystemResource(file));
					mailSenderUtil.sendMailAttachment(mailBean);
					//发送完邮件将文件从服务器删除
					file.delete();
				}
				//这段代码表示，这次消息，我已经接受并消费掉了，不会再重复发送消费
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				logger.warn("[Consumer Message 01] ===============> " + mailBean.toString());
			} catch (Exception e) {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
				e.printStackTrace();
			}
		}else{
			try {
				mailSenderUtil.sendMail(mailBean);
				//这段代码表示，这次消息，我已经接受并消费掉了，不会再重复发送消费
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("receiver false");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
				logger.warn("[Consumer Message 01] 普通邮件===============> " + mailBean.toString());
			}

		}
	}

	/*@RabbitListener(queues = RabbitConstant.MESSAGE_QUEUE)
	public void processMessage(String msg) {
		logger.info(Thread.currentThread().getName() + " 接收到来自message队列的消息：" + msg);
	}
*/
}