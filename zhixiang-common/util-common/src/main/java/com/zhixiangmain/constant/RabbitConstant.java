package com.zhixiangmain.constant;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.constant
 * @Description: rabbitMQ常量配置
 * @author: hhp
 * @date: 2019-03-27 17:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class RabbitConstant {
    //邮件队列
    public static final String EMAIL_QUEUE = "email";
    //三离消息队列
    public static final String THREE_LEAVE_ALARM_QUEUE = "tlAlarm";
    //挡鼠板消息队列
    public static final String RAT_ALARM_QUEUE = "ratAlarm";
    //服务器检测时间消息队列
    public static final String SERVICE_FEE_QUEUE = "serviceFee";
    //邮件队列路由键（*表示一个词,#表示零个或多个词）
    public static final String EMAIL_ROUTING_KEY = "email.key";
    //三离消息队列路由键
    public static final String THREE_LEAVE_ALARM_KEY = "tlAlarm.key";
    //挡鼠板消息队列路由键
    public static final String RAT_ALARM_KEY = "ratAlarm.key";
    //服务器检测时间消息队列路由键
    public static final String SERVICE_FEE_KEY = "serviceFee.key";
    //交换机
    public static final String TOPICEXCHANGE = "topicExchange";
    //交换机_三离
    public static final String TOPICEXCHANGE_TLALARM = "topicExchangeTlAlarm";
    //交换机_挡鼠板
    public static final String TOPICEXCHANGE_RATALARM= "topicExchangeRatAlarm";
    //交换机_服务器检测时间
    public static final String TOPICEXCHANGE_SERVICEFEE= "topicExchangeServiceFee";
}
