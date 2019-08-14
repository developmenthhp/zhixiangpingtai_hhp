package com.zhixiangmain.controller.zhck.slgl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.service.zhck.slgl.ThreeLeaveAlarmService;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
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
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.controller.zhck.slgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-31 17:36
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/threeLeaveAlarm")
public class ThreeLeaveAlarmController {
    private static final Logger logger = LoggerFactory
            .getLogger(ThreeLeaveAlarmController.class);
    @Reference(version = "1.0.0")
    private ThreeLeaveAlarmService threeLeaveAlarmService;

    /**
     * 食材基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回从业人员概况页面");
        ModelAndView mav = new ModelAndView("/zhck/slgl/slgl");
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
     * 获取站点三离安装总数，报警中，正常状态占比
     * @return ModelAndView
     */
    @PostMapping( "/getTLAlarmPieBySdId")
    @ResponseBody
    public ResultBean getTLAlarmPieBySdId(BaseEntityDTO baseEntityDTO) {
        logger.debug("平台对传过来的站点进行三离安装总数，报警中，正常状态占比分析！查询条件：baseEntityDTO：" + baseEntityDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        baseEntityDTO.setUserId(existUser.getId());
        String path = ThreeLeaveAlarmController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = threeLeaveAlarmService.getTLAlarmPieBySdId(baseEntityDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("三离安装总数，报警中，正常状态占比分析异常");
            logger.error("三离安装总数，报警中，正常状态占比分析异常！", e);
        }
        return resultBean;
    }
}
