package com.zhixiangmain.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.rabbitmq.client.Channel;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.handler
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-01 14:02
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@Component
public class MessageEventHandler {
    private static final Logger logger = LoggerFactory
            .getLogger(MessageEventHandler.class);
    public static SocketIOServer socketIoServer;
    private Object myRabbitmqMsg;
    private SocketIOClient sioClient;
    private boolean isOpen = false;
    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        MessageEventHandler.socketIoServer = server;
    }
    @OnConnect
    public void onConnect(SocketIOClient client) {
        sioClient = client;
        isOpen = true;
        UUID socketSessionId = client.getSessionId();
        String ip = client.getRemoteAddress().toString();
        System.out.println("client connect, socketSessionId:{}, ip:{}"+socketSessionId+ip);
    }
    @OnMessage
    @RabbitListener(queues = RabbitConstant.THREE_LEAVE_ALARM_QUEUE)
    public void processMessage(String resultArg,Message message,Channel channel) {
        try {
            //这段代码表示，这次消息，我已经接受并消费掉了，不会再重复发送消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        myRabbitmqMsg = resultArg;
        if(!IsEmptyUtils.isEmpty(sioClient)){
            if(isOpen){
                //往前端推送消息
                socketIoServer.getClient(sioClient.getSessionId()).sendEvent("tlAlarm", myRabbitmqMsg);
            }
        }
    }
    @RabbitListener(queues = RabbitConstant.RAT_ALARM_QUEUE)
    public void processRatMessage(String resultArg,Message message,Channel channel) {
        //这段代码表示，这次消息，我已经接受并消费掉了，不会再重复发送消费
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        myRabbitmqMsg = resultArg;
        if(!IsEmptyUtils.isEmpty(sioClient)){
            if(isOpen){
                socketIoServer.getClient(sioClient.getSessionId()).sendEvent("ratAlarm", myRabbitmqMsg);
            }
        }
    }

    @OnEvent("tlAlarm")
    public void onTlAlarm(SocketIOClient client, AckRequest request, Object resultArg) throws IOException {
        System.out.println("发来消息：" + resultArg);
    }

    @OnEvent("ratAlarm")
    public void onRatAlarm(SocketIOClient client, AckRequest request, Object resultArg) throws IOException {
        System.out.println("挡鼠板页面发来消息：" + resultArg);
    }
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        isOpen = false;
        UUID socketSessionId = client.getSessionId();
        String ip = client.getRemoteAddress().toString();
        logger.info("client disconnect, socketSessionId:{}, ip:{}", socketSessionId, ip);
        Set<String> rooms = client.getAllRooms();
        for (String room : rooms) {
            logger.info("after client disconnect, room:{}", room);
        }
    }
    public static void sendAllEvent(String data) {
        socketIoServer.getRoomOperations("tlAlarm").sendEvent("tlAlarm", data);
    }
}
