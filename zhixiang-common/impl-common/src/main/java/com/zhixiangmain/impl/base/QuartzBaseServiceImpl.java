package com.zhixiangmain.impl.base;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.service.base.QuartzBaseService;
import org.slf4j.LoggerFactory;

/*import com.zhixiangmain.config.QuartzSchedulerBase;*/

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 16:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = QuartzBaseService.class)
public class QuartzBaseServiceImpl implements QuartzBaseService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(QuartzBaseServiceImpl.class);
    /*@Autowired
    private QuartzSchedulerBase quartzSchedulerBase;

    @Override
    public ResultBean startJob() throws Exception {
        ResultBean resultBean = new ResultBean();
        quartzSchedulerBase.startJob2();
        System.out.println("启动定时任务");
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getJobInfo(String name, String group) throws Exception {
        ResultBean resultBean = new ResultBean();
        String s = quartzSchedulerBase.getJobInfo(name,group);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean modifyJob(String name, String group, String time) throws Exception {
        ResultBean resultBean = new ResultBean();
        boolean b = quartzSchedulerBase.modifyJob(name,group,time);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean pauseJob(String name, String group) throws Exception {
        ResultBean resultBean = new ResultBean();
        quartzSchedulerBase.pauseJob(name,group);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean pauseAllJob() throws Exception {
        ResultBean resultBean = new ResultBean();
        quartzSchedulerBase.pauseAllJob();
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean deleteJob(String name, String group) throws Exception {
        ResultBean resultBean = new ResultBean();
        quartzSchedulerBase.deleteJob(name,group);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }*/
}
