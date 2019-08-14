package com.zhixiangmain.controller.scgl.sckjgl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.scgl.sckjgl.dto.FoodInspectionDTO;
import com.zhixiangmain.api.service.scgl.sckjgl.FoodInspectionService;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.overallParam.OpslabConfig;
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
 * @Package com.zhixiangmain.controller.scgl.sckjgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-30 16:24
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/foodInspection")
public class FoodInspectionController {
    private static final Logger logger = LoggerFactory
            .getLogger(FoodInspectionController.class);
    @Reference(version = "1.0.0")
    private FoodInspectionService foodInspectionService;

    /**
     * 食材索票索证管理echarts页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回从业人员概况页面");
        ModelAndView mav = new ModelAndView("/scgl/sckjgl/sckjgl");
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
     * 查询食材快检信息，一周记录
     * @return ModelAndView
     */
    @PostMapping( "/getByWeek")
    @ResponseBody
    public ResultBean getByWeek(FoodInspectionDTO foodInspectionDTO) {
        logger.debug("查询食材快检信息，一周记录！搜索条件：foodInspectionDTO：" + foodInspectionDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            foodInspectionDTO.setUserId(existUser.getId());
            String path = FoodInspectionController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = foodInspectionService.getByWeek(foodInspectionDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取查询食材快检信息，一周记录列表异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取某些站点食材检测管理
     * @return ModelAndView
     */
    @PostMapping( "/getFInsBySdId")
    @ResponseBody
    public ResultBean getFInsBySdId(FoodInspectionDTO foodInspectionDTO) {
        logger.debug("平台对传过来的站点进行食材检测管理分析！查询条件：foodInspectionDTO：" + foodInspectionDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        foodInspectionDTO.setUserId(existUser.getId());
        String path = FoodInspectionController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = foodInspectionService.getFInsBySdId(foodInspectionDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("食材检测管理分析异常");
            logger.error("食食材检测管理分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取该日期食材检测合格，不合格占比
     * @return ModelAndView
     */
    @PostMapping( "/getCTCByDateSdId")
    @ResponseBody
    public ResultBean getFInsByDateSdId(FoodInspectionDTO foodInspectionDTO) {
        logger.debug("平台对传过来的站点进行食材检测合格，不合格占比分析！查询条件：foodInspectionDTO：" + foodInspectionDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        foodInspectionDTO.setUserId(existUser.getId());
        String path = FoodInspectionController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = foodInspectionService.getFInsByDateSdId(foodInspectionDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("食材检测合格，不合格占比分析异常");
            logger.error("食材检测合格，不合格占比分析异常！", e);
        }
        return resultBean;
    }
}
