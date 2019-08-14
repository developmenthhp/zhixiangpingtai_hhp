package com.zhixiangmain.controller.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhixiangmain.service.base.QuartzBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.controller.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-28 16:15
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/quartz")
public class QuartzController {
    private static final Logger logger = LoggerFactory
            .getLogger(QuartzController.class);
    @Reference(version = "1.0.0")
    private QuartzBaseService quartzBaseService;

    /*@GetMapping( "/start")
    @ResponseBody
    public ResultBean startQuartzJob() {
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = quartzBaseService.startJob();
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            //WebMassage.EMAIL_SEND_ERROR
            resultBean.setMsg("提交数据异常");
            logger.error("更新某个站点预采购单审核状态！", e);
        }
        return resultBean;
    }
    @GetMapping( "/info")
    @ResponseBody
    public ResultBean getQuartzJob(String name, String group) {
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = quartzBaseService.getJobInfo(name, group);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("提交数据异常");
            logger.error("更新某个站点预采购单审核状态！", e);
        }
        return resultBean;
    }

    @GetMapping( "/modify")
    @ResponseBody
    public ResultBean modifyQuartzJob(String name, String group, String time) {
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = quartzBaseService.modifyJob(name, group, time);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("提交数据异常");
            logger.error("更新某个站点预采购单审核状态！", e);
        }
        return resultBean;
    }

    @GetMapping( "/pause")
    @ResponseBody
    public ResultBean pauseQuartzJob(String name, String group) {
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = quartzBaseService.pauseJob(name, group);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("提交数据异常");
            logger.error("更新某个站点预采购单审核状态！", e);
        }
        return resultBean;
    }

    @GetMapping( "/pauseAll")
    @ResponseBody
    public ResultBean pauseAllQuartzJob() {
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = quartzBaseService.pauseAllJob();
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("提交数据异常");
            logger.error("更新某个站点预采购单审核状态！", e);
        }
        return resultBean;
    }

    @GetMapping( "/delete")
    @ResponseBody
    public ResultBean deleteJob(String name, String group) {
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = quartzBaseService.deleteJob(name, group);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("提交数据异常");
            logger.error("更新某个站点预采购单审核状态！", e);
        }
        return resultBean;
    }*/
}
