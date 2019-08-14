package com.zhixiangmain.controller.mclz.ztgk;

import com.zhixiangmain.overallParam.OpslabConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.controller.mclz.ztgk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-20 16:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/brightkitchen")
public class BrightkitchenController {
    private static final Logger logger = LoggerFactory
            .getLogger(BrightkitchenController.class);
    /**
     * 明厨亮灶列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回明厨亮灶页面");
        ModelAndView mav = new ModelAndView("/mclz/ztgk/ztgk");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("proUrl", OpslabConfig.get("PROJECT_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回明厨亮灶页面异常！", e);
        }
        return mav;
    }

    /**
     * 明厨亮灶h5页面
     * @return ModelAndView
     */
    @GetMapping( "/getHFivePage")
    @ResponseBody
    public ModelAndView getHFivePage() {
        logger.debug("返回明厨亮灶h5页面");
        ModelAndView mav = new ModelAndView("/mclz/ztgk/ztgkhfive");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            /*mav.addObject("proUrl", OpslabConfig.get("PROJECT_PREFIX"));*/
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回明厨亮灶h5页面异常！", e);
        }
        return mav;
    }
}
