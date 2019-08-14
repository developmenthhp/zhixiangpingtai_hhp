package com.zhixiangmain.controller.qksj.yyfx;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.qksj.yyfx.dto.NutingDTO;
import com.zhixiangmain.api.service.qksj.yyfx.NutingService;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.controller.qksj.yyfx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-18 10:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/nuting")
public class NutingController {
    private static final Logger logger = LoggerFactory
            .getLogger(NutingController.class);
    @Reference(version = "1.0.0")
    private NutingService nutingService;

    /**
     * 营养分析列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回营养分析页面");
        ModelAndView mav = new ModelAndView("/qksj/yyfx/yyfx");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回营养分析页面异常！", e);
        }
        return mav;
    }

    /**
     * 获取站点营养分析数据
     * @return ModelAndView
     */
    @PostMapping( "/getRankingListBySdId")
    @ResponseBody
    public ResultBean getRankingListBySdId(@RequestParam("page") Integer page,
                                           @RequestParam("limit") Integer limit, NutingDTO nutingDTO) {
        logger.debug("平台对传过来的站点进行营养分析！查询条件：nutingDTO：" + nutingDTO);
        ResultBean resultBean = new ResultBean();
        if (null == page) {
            page = 1;
        }
        if (null == limit) {
            limit = 10;
        }
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        nutingDTO.setUserId(existUser.getId());
        String path = NutingController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = nutingService.getRankingListBySdId(nutingDTO, page, limit, jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("营养分析异常");
            logger.error("营养分析异常！", e);
        }
        return resultBean;
    }
}
