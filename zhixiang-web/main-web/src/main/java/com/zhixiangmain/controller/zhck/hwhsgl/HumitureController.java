package com.zhixiangmain.controller.zhck.hwhsgl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.zhck.hwhsgl.dto.HumitureDTO;
import com.zhixiangmain.api.service.zhck.hwhsgl.HumitureService;
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
 * @Package com.zhixiangmain.controller.zhck.hwhsgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-03 17:27
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/humiture")
public class HumitureController {
    private static final Logger logger = LoggerFactory
            .getLogger(HumitureController.class);
    @Reference(version = "1.0.0")
    private HumitureService humitureService;

    /**
     * 智慧仓库w温湿度管理列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回温湿度管理概况页面");
        ModelAndView mav = new ModelAndView("/zhck/hwhsgl/hwhsgl");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回温湿度管理页面异常！", e);
        }
        return mav;
    }

    /**
     * 专间管理专间温湿度管理列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getTemHumPage")
    @ResponseBody
    public ModelAndView getTemHumPage() {
        logger.debug("返回专间温湿度管理概况页面");
        ModelAndView mav = new ModelAndView("/zjgl/zjwsdgl/zjwsdgl");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回专间温湿度管理页面异常！", e);
        }
        return mav;
    }

    /**
     * 获取站点温湿度安装总数，报警中，正常状态占比
     * @return ModelAndView
     */
    @PostMapping( "/getHumitureBySdId")
    @ResponseBody
    public ResultBean getHumitureBySdId(BaseEntityDTO baseEntityDTO) {
        logger.debug("平台对传过来的站点进行温湿度安装总数，报警中，正常状态占比分析！查询条件：baseEntityDTO：" + baseEntityDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        baseEntityDTO.setUserId(existUser.getId());
        String path = HumitureController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = humitureService.getHumitureBySdId(baseEntityDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("温湿度安装总数，报警中，正常状态占比分析异常");
            logger.error("温湿度安装总数，报警中，正常状态占比分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取某些站点专间温湿度管理
     * @return ModelAndView
     */
    @PostMapping( "/getHumMonthBySdId")
    @ResponseBody
    public ResultBean getHumMonthBySdId(HumitureDTO humitureDTO) {
        logger.debug("平台对传过来的站点进行专间温湿度管理分析！查询条件：humitureDTO：" + humitureDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        humitureDTO.setUserId(existUser.getId());
        String path = HumitureController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = humitureService.getHumMonthBySdId(humitureDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("专间温湿度管理分析异常");
            logger.error("专间温湿度管理分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取该日期食材检测合格，不合格占比
     * @return ModelAndView
     */
    @PostMapping( "/getHumByDateSdId")
    @ResponseBody
    public ResultBean getHumByDateSdId(HumitureDTO humitureDTO) {
        logger.debug("平台对传过来的站点进行专间温湿度正常，异常占比分析！查询条件：humitureDTO：" + humitureDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        humitureDTO.setUserId(existUser.getId());
        String path = HumitureController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = humitureService.getHumByDateSdId(humitureDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("专间温湿度正常，异常占比分析异常");
            logger.error("专间温湿度正常，异常占比分析异常！", e);
        }
        return resultBean;
    }
}
