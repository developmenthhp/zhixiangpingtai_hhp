package com.zhixiangmain.controller.lygl.lygl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.lygl.lygl.dto.KeepSampleDTO;
import com.zhixiangmain.api.service.lygl.lygl.KeepSampleService;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.overallParam.OpslabConfig;
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
 * @Package com.zhixiangmain.controller.lygl.lygl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-08 18:02
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/keepSample")
public class KeepSampleController {
    private static final Logger logger = LoggerFactory
            .getLogger(KeepSampleController.class);
    @Reference(version = "1.0.0")
    private KeepSampleService keepSampleService;

    /**
     * 专间消毒管理列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回专间消毒管理概况页面");
        ModelAndView mav = new ModelAndView("/lygl/lygl/lygl");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回专间消毒管理页面异常！", e);
        }
        return mav;
    }

    /**
     * 留样记录列表
     * @return ModelAndView
     */
    @PostMapping( "/getPaginationData")
    @ResponseBody
    public ResultBean getPaginationData(@RequestParam("page") Integer page,
                                        @RequestParam("limit") Integer limit, KeepSampleDTO keepSampleDTO) {
        logger.debug("分页查询留样记录信息列表！搜索条件：keepSampleDTO：" + keepSampleDTO + ",page:" + page
                + ",每页记录数量limit:" + limit);
        ResultBean resultBean = new ResultBean();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            keepSampleDTO.setUserId(existUser.getId());
            String path = KeepSampleController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = keepSampleService.getPaginationData(keepSampleDTO,page,limit,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取每天消毒记录信息列表!=disinfectionRcdDTO:" + keepSampleDTO);

        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取留样记录信息列表异常！", e);
            resultBean.setMsg("获取留样记录信息列表异常！");
        }
        return resultBean;
    }

}
