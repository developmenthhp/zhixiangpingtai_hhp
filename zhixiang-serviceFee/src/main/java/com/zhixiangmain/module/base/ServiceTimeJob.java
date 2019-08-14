package com.zhixiangmain.module.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.component.Sender;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.config.SpringBeanFactoryUtils;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.date.DateUtils;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.base.mail.MailBean;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/10 8:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class ServiceTimeJob implements Job {
    private static final Logger logger = LoggerFactory
            .getLogger(ServiceTimeJob.class);
    @Autowired
    private Sender sender;
    @Bean(name = "sender")
    public Sender getSender(){
        return sender;
    }
    @Autowired
    private SiteMapper siteMapper;
    @Bean(name = "siteMapper")
    public SiteMapper getSiteMapperMapper(){
        return siteMapper;
    }
    public ServiceTimeJob(){
        SiteMapper siteMapperInit = SpringBeanFactoryUtils.getBean("siteMapper",SiteMapper.class);
        Sender senderBean = SpringBeanFactoryUtils.getBean("sender",Sender.class);
        this.siteMapper = siteMapperInit;
        this.sender = senderBean;
    }
    private void before(){
        System.out.println("服务费任务开始执行");
    }
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        before();
        System.out.println("开始：服务费校验到期事件开始："+System.currentTimeMillis());
        //获取所有站点预警，前端筛选，这里进行前端筛选
        //这里是解决打jar 包报filenotfoundException的方式，采用inputstream
        StringBuffer stringBuffer = new StringBuffer();
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //这里是旧有方式，打jar包会报filenotfoundexception
        /*String path = ThreeLeaveJob.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);*/
        //inputstream方式
        JSONObject jobj = JSON.parseObject(stringBuffer.toString());
        String resultMsg = "";
        //或者这里使用dubbo里注册的服务
        for(String str:jobj.keySet()){
            System.out.println(str + ":" +jobj.get(str));
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(jobj.get(str))){
                //String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(str))){
                    siteData = JSON.parseObject(jobj.get(str).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"----数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    Map<String,Object> timesMap = siteMapper.findOverTimeSiteId(str);
                    if(!IsEmptyUtils.isEmpty(timesMap)){
                        if(!IsEmptyUtils.isEmpty(timesMap.get("overTime"))){
                            String currentDate = DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
                            int bigCompare = currentDate.compareTo(timesMap.get("overTime").toString());
                            switch (bigCompare){
                                case -1:
                                    //比较临期时间
                                    if(!IsEmptyUtils.isEmpty(timesMap.get("advanceTime"))){
                                        int advanceCompare = currentDate.compareTo(timesMap.get("advanceTime").toString());
                                        switch (advanceCompare){
                                            case -1:
                                                break;
                                            case 0:
                                                if(IsEmptyUtils.isEmpty(resultMsg)){
                                                    resultMsg = "站点："+siteData.getName()+"服务已超过临期预警时间，请及时提醒收取服务费";
                                                }else{
                                                    resultMsg = resultMsg +"、" + siteData.getName()+"服务已超过临期预警时间，请及时提醒收取服务费";
                                                }
                                                break;
                                            case 1:
                                                if(IsEmptyUtils.isEmpty(resultMsg)){
                                                    resultMsg = "站点："+siteData.getName()+"服务已超过临期预警时间，请及时提醒收取服务费";
                                                }else{
                                                    resultMsg = resultMsg +"、" + siteData.getName()+"服务已超过临期预警时间，请及时提醒收取服务费";
                                                }
                                                break;
                                        }
                                    }
                                    break;
                                case 0:
                                    if(IsEmptyUtils.isEmpty(resultMsg)){
                                        resultMsg = "站点："+siteData.getName()+"服务已过期";
                                    }else{
                                        resultMsg = resultMsg +"、" + siteData.getName()+"服务已过期";
                                    }
                                    break;
                                case 1:
                                    if(IsEmptyUtils.isEmpty(resultMsg)){
                                        resultMsg = "站点："+siteData.getName()+"服务已过期";
                                    }else{
                                        resultMsg = resultMsg +"、" + siteData.getName()+"服务已过期";
                                    }
                                    break;
                            }

                        }
                    }
                    logger.info(resultMsg);
                }
            }
        }
        if(!IsEmptyUtils.isEmpty(resultMsg)){
            MailBean mailBean = MailBean.getMailBean();
            mailBean.setReceiver("2437579794@qq.com");
            mailBean.setSubject("服务器预警提醒");
            mailBean.setText(resultMsg);
            sender.sendServiceFeeMail(mailBean);
        }
        //切换回默认数据源
        DynamicDataSourceContextHolder.setDataSourceType(null);

        System.out.println("结束："+System.currentTimeMillis());
        after();
    }
    private void after(){
        System.out.println("服务器预警过期提醒任务执行完毕-------------");
    }
}
