package com.zhixiangmain.controller.cpjs.cpmx;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDTO;
import com.zhixiangmain.api.service.cpjs.cpmx.FoodMenuService;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.overallParam.OpslabConfig;
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
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.controller.cpjs.cpmx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-08 10:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/foodMenu")
public class FoodMenuController {
    private static final Logger logger = LoggerFactory
            .getLogger(FoodMenuController.class);
    @Reference(version = "1.0.0")
    private FoodMenuService foodMenuService;

    /**
     * 菜谱明细列表页面
     * @return ModelAndView
     */
    @GetMapping( "/fmDetailPage")
    @ResponseBody
    public ModelAndView fmDetailPage() {
        logger.debug("返回菜谱明细列表页面");
        ModelAndView mav = new ModelAndView("/cpjs/cpmx/cpmx");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回菜谱明细列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 销售数据统计列表页面
     * @return ModelAndView
     */
    @GetMapping( "/statisticsMenuPage")
    @ResponseBody
    public ModelAndView statisticsMenuPage() {
        logger.debug("返回销售数据统计列表页面");
        ModelAndView mav = new ModelAndView("/cpjs/xssjtj/xssjtj");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回销售数据统计列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 已售卖菜品分析列表页面
     * @return ModelAndView
     */
    @GetMapping( "/statisticsedPage")
    @ResponseBody
    public ModelAndView statisticsedPage() {
        logger.debug("返回已售卖菜品分析列表页面");
        ModelAndView mav = new ModelAndView("/cpjs/ysmcpfx/ysmcpfx");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回已售卖菜品分析列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 发布菜谱页面
     * @return ModelAndView
     */
    @GetMapping( "/publishFBPage")
    @ResponseBody
    public ModelAndView publishFBPage() {
        logger.debug("返回发布菜谱页面");
        ModelAndView mav = new ModelAndView("/cpjs/fbcp/fbcp");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回发布菜谱页面异常！", e);
        }
        return mav;
    }

    /**
     * 食材基础信息列表
     * @return ModelAndView
     */
    @PostMapping( "/getFoodMenus")
    @ResponseBody
    public ResultBean getFoodMenus(FoodMenuDTO foodMenuDTO) {
        logger.debug("分页查询食材基础信息列表！搜索条件：foodMenuDTO：" + foodMenuDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            foodMenuDTO.setUserId(existUser.getId());
            String path = FoodMenuController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = foodMenuService.getFoodMenus(foodMenuDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取预采购信息列表异常！", e);
        }
        return resultBean;
    }

    /**
     * 食材基础信息列表
     * @return ModelAndView
     */
    @PostMapping( "/getBySdIdOrderDate")
    @ResponseBody
    public ResultBean getBySdIdOrderDate(FoodMenuDTO foodMenuDTO) {
        logger.debug("根据试点id菜单日期餐次查找该菜单下的所有菜品！搜索条件：foodMenuDTO：" + foodMenuDTO);
        ResultBean resultBean = new ResultBean();
        try {
            String path = FoodMenuDetailController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = foodMenuService.getBySdIdOrderDate(foodMenuDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("根据试点id菜单日期餐次查找该菜单下的所有菜品异常！", e);
        }
        return resultBean;
    }

    public static void main(String[] args){
        /*String path = FoodMenuController.class.getClassLoader().getResource("static/json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        //JSONArray jsonArray = JSON.parseArray(siteJsonData);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        *//*JSONObject nbnewjson = new JSONObject();
        nbnewjson.put("have", "0");*//*
        jobj.put("255","http://127.0.0.1:85");
        //将json转换为json字符串
        String newJsonString = jobj.toString();
        JsonUtil.writeJsonFile(newJsonString, path);

        System.out.println(siteJsonData);*/

    }
}
