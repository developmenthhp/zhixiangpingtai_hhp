package com.zhixiangmain.controller.zjgl.zjxdgl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.disinfectionRcd.dto.DisinfectionRcdDTO;
import com.zhixiangmain.api.service.zjgl.zjxdgl.DisinfectionService;
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
 * @Package com.zhixiangmain.controller.zjgl.zjxdgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-04 10:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/disinfection")
public class DisinfectionController {
    private static final Logger logger = LoggerFactory
            .getLogger(DisinfectionController.class);
    @Reference(version = "1.0.0")
    private DisinfectionService disinfectionService;

    /**
     * 专间消毒管理列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回专间消毒管理概况页面");
        ModelAndView mav = new ModelAndView("/zjgl/zjxdgl/zjxdgl");
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
     * 每天消毒记录管理列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getListPage")
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("返回专间消毒管理概况页面");
        ModelAndView mav = new ModelAndView("/xdgl/mtxdyc/mtxdyc");
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
     * 获取某些站点专间消毒管理
     * @return ModelAndView
     */
    @PostMapping( "/getDisMonthBySdId")
    @ResponseBody
    public ResultBean getDisMonthBySdId(DisinfectionRcdDTO disinfectionRcdDTO) {
        logger.debug("平台对传过来的站点进行专间消毒管理分析！查询条件：disinfectionRcdDTO：" + disinfectionRcdDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        disinfectionRcdDTO.setUserId(existUser.getId());
        String path = DisinfectionController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = disinfectionService.getDisMonthBySdId(disinfectionRcdDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("专间消毒管理分析异常");
            logger.error("专间消毒管理分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取该日期专间消毒正常，异常占比
     * @return ModelAndView
     */
    @PostMapping( "/getDisByDateSdId")
    @ResponseBody
    public ResultBean getDisByDateSdId(DisinfectionRcdDTO disinfectionRcdDTO) {
        logger.debug("平台对传过来的站点进行专间消毒正常，异常占比分析！查询条件：disinfectionRcdDTO：" + disinfectionRcdDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        disinfectionRcdDTO.setUserId(existUser.getId());
        String path = DisinfectionController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = disinfectionService.getDisByDateSdId(disinfectionRcdDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("专间消毒正常，异常占比分析异常");
            logger.error("专间消毒正常，异常占比分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 每天消毒记录列表
     * @return ModelAndView
     */
    @PostMapping( "/getPaginationData")
    @ResponseBody
    public ResultBean getPaginationData(@RequestParam("page") Integer page,
                                               @RequestParam("limit") Integer limit, DisinfectionRcdDTO disinfectionRcdDTO) {
        logger.debug("分页查询每天消毒记录信息列表！搜索条件：basicInfoFood：" + disinfectionRcdDTO + ",page:" + page
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
            disinfectionRcdDTO.setUserId(existUser.getId());
            String path = DisinfectionController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = disinfectionService.getPaginationData(disinfectionRcdDTO,page,limit,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取每天消毒记录信息列表!=disinfectionRcdDTO:" + disinfectionRcdDTO);

        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取每天消毒记录信息列表异常！", e);
            resultBean.setMsg("获取每天消毒记录信息列表异常！");
        }
        return resultBean;
    }
}
