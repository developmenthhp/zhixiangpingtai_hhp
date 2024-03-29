package com.zhixiangmain.controller.zhjg.eggl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.zhjg.eggl.dto.SecondWearDTO;
import com.zhixiangmain.api.service.zhjg.eggl.SecondWearService;
import com.zhixiangmain.json.JsonUtil;
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
 * @Package com.zhixiangmain.controller.zhjg.eggl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-29 15:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/secondWear")
public class SecondWearController {
    private static final Logger logger = LoggerFactory
            .getLogger(SecondWearController.class);
    @Reference(version = "1.0.0")
    private SecondWearService secondWearService;

    /**
     * 食材基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回从业人员概况页面");
        ModelAndView mav = new ModelAndView("/zhjg/eggl/eggl");
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
     * 获取二更数据
     * @return ModelAndView
     */
    @PostMapping( "/getSWearBySdId")
    @ResponseBody
    public ResultBean getSWearBySdId(SecondWearDTO secondWearDTO) {
        logger.debug("平台对传过来的站点进行二更管理分析！查询条件：secondWearDTO：" + secondWearDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        secondWearDTO.setUserId(existUser.getId());
        String path = SecondWearController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = secondWearService.getSWearBySdId(secondWearDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("二更管理分析异常");
            logger.error("二更管理分析异常！", e);
        }
        return resultBean;
    }
}
