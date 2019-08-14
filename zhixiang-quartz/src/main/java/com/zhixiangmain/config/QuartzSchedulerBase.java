package com.zhixiangmain.config;

import com.zhixiangmain.module.base.ThreeLeaveJob;
import com.zhixiangmain.module.base.RatBaffleJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.config
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 15:48
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */

@Configuration
public class QuartzSchedulerBase {
    // 任务调度
    @Autowired
    private Scheduler scheduler;
    /**
     * 开始执行所有任务
     * @throws SchedulerException
     * */

    public void startTLJob() throws SchedulerException {
        startThreeLeaveJob(scheduler);
        scheduler.start();
    }
    public void startRBJob() throws SchedulerException {
        startRatBaffleJob(scheduler);
        scheduler.start();
    }
    /**
     * 获取Job信息
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     * */

    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(), scheduler.getTriggerState(triggerKey).name());
    }
    /**
     * 修改某个任务的执行时间
     *
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     * */

    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null; TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group) .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger); } return date != null;
    }
   /**
     * 暂停所有任务
     * @throws SchedulerException
     * */

    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }
    /**
     * 暂停某个任务
     * @param name
     * @param group
     * @throws SchedulerException
     * */

    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) return;
        scheduler.pauseJob(jobKey);
    }
    /**
     * 恢复所有任务
     * @throws SchedulerException
     * */

    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }
    /**
     * 恢复某个任务
     * @param name
     * @param group
     * @throws SchedulerException
     * */

    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) return;
        scheduler.resumeJob(jobKey);
    }
    /**
     * 删除某个任务
     * @param name
     * @param group
     * @throws SchedulerException
     * */

      public void deleteJob(String name, String group) throws SchedulerException {
         JobKey jobKey = new JobKey(name, group);
         JobDetail jobDetail = scheduler.getJobDetail(jobKey);
         if (jobDetail == null) return; scheduler.deleteJob(jobKey);
     }
     private void startThreeLeaveJob(Scheduler scheduler) throws SchedulerException {
         // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
         // JobDetail 是具体Job实例
         JobDetail jobDetail = JobBuilder.newJob(ThreeLeaveJob.class).withIdentity("job1", "group1").build();
         // 基于表达式构建触发器
         CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");//每10秒执行一次
         // CronTrigger表达式触发器 继承于Trigger
         // TriggerBuilder 用于构建触发器实例
         CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job1", "group1") .withSchedule(cronScheduleBuilder).build(); scheduler.scheduleJob(jobDetail, cronTrigger);
     }
     private void startRatBaffleJob(Scheduler scheduler) throws SchedulerException {
         JobDetail jobDetail = JobBuilder.newJob(RatBaffleJob.class).withIdentity("job2", "group2").build();
         CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
         CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job2", "group2") .withSchedule(cronScheduleBuilder).build();
         scheduler.scheduleJob(jobDetail, cronTrigger);
     }

}
