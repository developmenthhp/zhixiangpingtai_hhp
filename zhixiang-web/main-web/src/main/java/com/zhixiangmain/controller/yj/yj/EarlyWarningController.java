package com.zhixiangmain.controller.yj.yj;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.service.yj.yj.EarlyWarningService;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.EarlyWarningResult;
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
 * @Package com.zhixiangmain.controller.yj.yj
 * @Description: 所有报警信息
 * @author: hhp
 * @date: 2019-06-11 16:32
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/earlyWarning")
public class EarlyWarningController {
    private static final Logger logger = LoggerFactory
            .getLogger(EarlyWarningController.class);
    @Reference(version = "1.0.0")
    private EarlyWarningService earlyWarningService;

    /**
     * 预警列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回预警页面");
        ModelAndView mav = new ModelAndView("/yj/yj/yj");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回预警页面异常！", e);
        }
        return mav;
    }

    /**
     * 获取某些站点预警管理
     * @return ModelAndView
     */
    @PostMapping( "/getEWMonthBySdId")
    @ResponseBody
    public EarlyWarningResult getEWMonthBySdId(BaseEntityDTO baseEntityDTO) {
        logger.debug("平台对传过来的站点进行预警分析！查询条件：baseEntityDTO：" + baseEntityDTO);
        EarlyWarningResult earlyWarningResult = new EarlyWarningResult();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        baseEntityDTO.setUserId(existUser.getId());
        String path = EarlyWarningController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            earlyWarningResult = earlyWarningService.getEWMonthBySdId(baseEntityDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            earlyWarningResult.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            earlyWarningResult.setMsg("预警分析异常");
            logger.error("预警分析异常！", e);
        }
        return earlyWarningResult;
    }
}
