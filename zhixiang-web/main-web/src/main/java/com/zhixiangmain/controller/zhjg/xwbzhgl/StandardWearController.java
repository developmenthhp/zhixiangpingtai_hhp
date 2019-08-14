package com.zhixiangmain.controller.zhjg.xwbzhgl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.base.dto.AbnormalSnapshotDTO;
import com.zhixiangmain.api.service.zhjg.xwbzhgl.StandardWearService;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.json.JsonUtil;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.controller.zhjg.xwbzhgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-22 16:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/standardWear")
public class StandardWearController {
    private static final Logger logger = LoggerFactory
            .getLogger(StandardWearController.class);
    @Reference(version = "1.0.0")
    private StandardWearService standardWearService;

    /**
     * 二更echarts页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回从业人员概况页面");
        ModelAndView mav = new ModelAndView("/zhjg/xwbzhgl/xwbzhgl");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回从业人员概况页面异常！", e);
        }
        return mav;
    }

    /**
     * 获取某些站点的标准穿戴管理，待二更接入，目前只读取口罩抓拍-2019-05-22
     * @return ModelAndView
     */
    @PostMapping( "/getSWCharBySdId")
    @ResponseBody
    public ResultBean getSWCharBySdId(AbnormalSnapshotDTO abnormalSnapshotDTO) {
        logger.debug("平台对传过来的站点进行标准穿戴管理分析！查询条件：abnormalSnapshotDTO：" + abnormalSnapshotDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        abnormalSnapshotDTO.setUserId(existUser.getId());
        String path = StandardWearController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = standardWearService.getSWCharBySdId(abnormalSnapshotDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("标准穿戴管理分析异常");
            logger.error("标准穿戴管理分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取某些站点的健康证信息
     * @return ModelAndView
     */
    /*@PostMapping( "/getHealthTypeCharBySdId")
    @ResponseBody
    public ResultBean getHealthTypeCharBySdId(HealthCertificateDTO healthCertificateDTO) {
        logger.debug("平台对传过来的站点进行拥有健康证的从业人员健康证分析！查询条件：healthCertificateDTO：" + healthCertificateDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        healthCertificateDTO.setUserId(existUser.getId());
        String path = StandardWearController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = standardWearService.getHealthTypeCharBySdId(healthCertificateDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("健康证的从业人员健康证分析异常");
            logger.error("健康证的从业人员健康证分析异常！", e);
        }
        return resultBean;
    }*/
}
