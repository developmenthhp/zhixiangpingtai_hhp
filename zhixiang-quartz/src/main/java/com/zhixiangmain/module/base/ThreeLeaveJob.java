package com.zhixiangmain.module.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.component.Sender;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.config.SpringBeanFactoryUtils;
import com.zhixiangmain.dao.ThreeWallAlert.ThreeWallAlertMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.web.responseConfig.ResultBean;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 16:08
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class ThreeLeaveJob implements Job {
    private static final Logger logger = LoggerFactory
            .getLogger(ThreeLeaveJob.class);
    @Autowired
    private Sender sender;
    @Bean(name = "sender")
    public Sender getSender(){
        return sender;
    }
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private ThreeWallAlertMapper threeWallAlertMapper;
    @Bean(name = "threeWallAlertMapper")
    public ThreeWallAlertMapper getThreeWallAlertMapper(){
        return threeWallAlertMapper;
    }
    public ThreeLeaveJob(){
        ThreeWallAlertMapper formWallAlertMapper = SpringBeanFactoryUtils.getBean("threeWallAlertMapper",ThreeWallAlertMapper.class);
        Sender senderBean = SpringBeanFactoryUtils.getBean("sender",Sender.class);
        this.threeWallAlertMapper = formWallAlertMapper;
        this.sender = senderBean;
    }
    private void before(){
        System.out.println("三离实时预警任务开始执行");
    }
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        before();
        System.out.println("开始："+System.currentTimeMillis());
        //web 选择站点的时候，可以发一个消息到rabbitmq,这里接收 选择的站点，暂时用所有
        //或者获取所有站点预警，前端筛选，
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
        //List<Map<String,Object>> siteFWAlertMaps = Lists.newArrayList();
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
                //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"----数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    Integer allWarnTotal = threeWallAlertMapper.findWarningTotal();
                    if(allWarnTotal>0){
                        if(IsEmptyUtils.isEmpty(resultMsg)){
                            resultMsg = "站点"+siteData.getName();
                        }else{
                            resultMsg = resultMsg +"、" + siteData.getName();
                        }
                    }

                }
            }
        }
        if(!IsEmptyUtils.isEmpty(resultMsg)){
            resultMsg = resultMsg + "-物品离墙过近，请及时处理！";
        }
        sender.sendThreeWall(resultMsg);
        //切换回默认数据源
        DynamicDataSourceContextHolder.setDataSourceType(null);

        System.out.println("结束："+System.currentTimeMillis());
        after();
    }
    private void after(){
        System.out.println("三离实时预警任务执行完毕。。。");
    }
}
