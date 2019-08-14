package com.zhixiangmain.controller.jcxxpt.zzgs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.service.jcxxpt.zzgs.NotarizedCertificateService;
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
 * @Package com.zhixiangmain.controller.jcxxpt.zzgs
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-25 18:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/notarizedCertificate")
public class NotarizedCertificateController {
    private static final Logger logger = LoggerFactory
            .getLogger(NotarizedCertificateController.class);
    @Reference(version = "1.0.0")
    private NotarizedCertificateService notarizedCertificateService;

    /**
     * 食材基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getBusPage")
    @ResponseBody
    public ModelAndView getBusPage() {
        logger.debug("返回从业人员概况页面");
        ModelAndView mav = new ModelAndView("/jcxxpt/zdjcxx/business");
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
     * 食材基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getLicPage")
    @ResponseBody
    public ModelAndView getLicPage() {
        logger.debug("返回从业人员概况页面");
        ModelAndView mav = new ModelAndView("/jcxxpt/zdjcxx/license");
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
     * 食材基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getCirPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回从业人员概况页面");
        ModelAndView mav = new ModelAndView("/jcxxpt/zdjcxx/circulation");
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
     * 获取站点营业执照 餐饮许可证，食品流通证概况
     * @return ModelAndView
     */
    @PostMapping( "/getNotCerBySdId")
    @ResponseBody
    public ResultBean getNotCerBySdId(BaseEntityDTO baseEntityDTO) {
        logger.debug("平台对传过来的站点进行营业执照 餐饮许可证，食品流通证概况分析！查询条件：baseEntityDTO：" + baseEntityDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        baseEntityDTO.setUserId(existUser.getId());
        String path = NotarizedCertificateController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = notarizedCertificateService.getNotCerBySdId(baseEntityDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("营业执照 餐饮许可证，食品流通证概况分析异常");
            logger.error("营业执照 餐饮许可证，食品流通证概况分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取站点营业执照概况
     * @return ModelAndView
     */
    @PostMapping( "/getBusInfoBySdId")
    @ResponseBody
    public ResultBean getBusInfoBySdId(BaseEntityDTO baseEntityDTO) {
        logger.debug("平台对传过来的站点进行营业执照概况分析！查询条件：baseEntityDTO：" + baseEntityDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        baseEntityDTO.setUserId(existUser.getId());
        String path = NotarizedCertificateController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = notarizedCertificateService.getBusInfoBySdId(baseEntityDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("营业执照概况分析异常");
            logger.error("营业执照概况分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取站点餐饮许可证概况
     * @return ModelAndView
     */
    @PostMapping( "/getLicInfoBySdId")
    @ResponseBody
    public ResultBean getLicInfoBySdId(BaseEntityDTO baseEntityDTO) {
        logger.debug("平台对传过来的站点进行餐饮许可证概况分析！查询条件：baseEntityDTO：" + baseEntityDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        baseEntityDTO.setUserId(existUser.getId());
        String path = NotarizedCertificateController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = notarizedCertificateService.getLicInfoBySdId(baseEntityDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("餐饮许可证概况分析异常");
            logger.error("餐饮许可证概况分析异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取站点餐饮流通证概况
     * @return ModelAndView
     */
    @PostMapping( "/getCirInfoBySdId")
    @ResponseBody
    public ResultBean getCirInfoBySdId(BaseEntityDTO baseEntityDTO) {
        logger.debug("平台对传过来的站点进行餐饮流通证概况分析！查询条件：baseEntityDTO：" + baseEntityDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        baseEntityDTO.setUserId(existUser.getId());
        String path = NotarizedCertificateController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = notarizedCertificateService.getCirInfoBySdId(baseEntityDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("餐饮流通证概况分析异常");
            logger.error("餐饮流通证概况分析异常！", e);
        }
        return resultBean;
    }
}
