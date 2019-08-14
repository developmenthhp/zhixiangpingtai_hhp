package com.zhixiangmain.controller.fqwgl.fqwgl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.fqwgl.fqwgl.dto.WasteDTO;
import com.zhixiangmain.api.service.fqwgl.fqwgl.WasteService;
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
 * @Package com.zhixiangmain.controller.fqwgl.fqwgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-11 10:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/waste")
public class WasteController {
    private static final Logger logger = LoggerFactory
            .getLogger(WasteController.class);
    @Reference(version = "1.0.0")
    private WasteService wasteService;

    /**
     * 废弃物管理列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回废弃物管理概况页面");
        ModelAndView mav = new ModelAndView("/fqwgl/fqwgl/fqwgl");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回废弃物管理页面异常！", e);
        }
        return mav;
    }

    /**
     * 获取某些站点废弃物管理
     * @return ModelAndView
     */
    @PostMapping( "/getWasteMonthBySdId")
    @ResponseBody
    public ResultBean getWasteMonthBySdId(WasteDTO wasteDTO) {
        logger.debug("平台对传过来的站点进行废弃物管理分析！查询条件：wasteDTO：" + wasteDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        wasteDTO.setUserId(existUser.getId());
        String path = WasteController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = wasteService.getWasteMonthBySdId(wasteDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("废弃物管理分析异常");
            logger.error("废弃物管理分析异常！", e);
        }
        return resultBean;
    }
}
