package com.zhixiangmain.module.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.component.Sender;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.config.SpringBeanFactoryUtils;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.dao.zhck.dsbgl.RatplateAlertMapper;
import com.zhixiangmain.json.JsonUtil;
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

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 16:57
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class RatBaffleJob implements Job {
    private static final Logger logger = LoggerFactory
            .getLogger(RatBaffleJob.class);
    @Autowired
    private Sender sender;
    @Bean(name = "sender")
    public Sender getSender(){
        return sender;
    }
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private RatplateAlertMapper ratplateAlertMapper;
    @Bean(name = "ratplateAlertMapper")
    public RatplateAlertMapper getRatplateAlertMapper(){
        return ratplateAlertMapper;
    }
    public RatBaffleJob(){
        RatplateAlertMapper formWallAlertMapper = SpringBeanFactoryUtils.getBean("ratplateAlertMapper",RatplateAlertMapper.class);
        Sender senderBean = SpringBeanFactoryUtils.getBean("sender",Sender.class);
        this.ratplateAlertMapper = formWallAlertMapper;
        this.sender = senderBean;
    }
    private void before(){
        System.out.println("挡鼠板任务开始执行");
    }
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        before();
        System.out.println("开始：挡鼠板事件开始："+System.currentTimeMillis());
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
                    Integer allWarnTotal = ratplateAlertMapper.findWarRatTotal();
                    if(allWarnTotal>0){
                        if(IsEmptyUtils.isEmpty(resultMsg)){
                            resultMsg = "站点"+siteData.getName();
                        }else{
                            resultMsg = resultMsg +"、" + siteData.getName();
                        }
                    }
                    logger.info(resultMsg);
                }
            }
        }
        if(!IsEmptyUtils.isEmpty(resultMsg)){
            resultMsg = resultMsg + "-经系统检测挡鼠板出现异常状况，请及时处理！";
        }
        sender.sendRatAlarm(resultMsg);
        //切换回默认数据源
        DynamicDataSourceContextHolder.setDataSourceType(null);

        System.out.println("结束："+System.currentTimeMillis());
        after();
    }
    private void after(){
        System.out.println("挡鼠板任务执行完毕-------------");
    }
}
