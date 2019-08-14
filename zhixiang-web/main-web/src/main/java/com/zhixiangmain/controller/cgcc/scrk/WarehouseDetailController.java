package com.zhixiangmain.controller.cgcc.scrk;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cgcc.scrk.dto.WarehouseDetailDTO;
import com.zhixiangmain.api.service.cgcc.scrk.WarehouseDetailService;
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
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.controller.cgcc.scrk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-09 14:04
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/warehouseDetail")
public class WarehouseDetailController {
    private static final Logger logger = LoggerFactory
            .getLogger(WarehouseDetailController.class);
    @Reference(version = "1.0.0")
    private WarehouseDetailService warehouseDetailService;

    /**
     * 食材临期管理echarts页面
     * @return ModelAndView
     */
    @GetMapping( "/getIBMaturePage")
    @ResponseBody
    public ModelAndView getIBMaturePage() {
        logger.debug("返回从业人员概况页面");
        ModelAndView mav = new ModelAndView("/scgl/lqgl/lqgl");
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
     * 食材入库信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回食材入库信息列表页面");
        ModelAndView mav = new ModelAndView("/cgcc/scrk/scrk");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回食材入库信息列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 食材入库信息列表
     * @return ModelAndView
     */
    @PostMapping( "/getWarehouseDetails")
    @ResponseBody
    public ResultBean getWarehouseDetails(WarehouseDetailDTO warehouseDetailDTO) {
        logger.debug("查询食材入库信息列表！搜索条件：warehouseDetailDTO：" + warehouseDetailDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            warehouseDetailDTO.setUserId(existUser.getId());
            String path = WarehouseDetailController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = warehouseDetailService.getWarehouseDetails(warehouseDetailDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取查询食材入库信息列表异常！", e);
        }
        return resultBean;
    }

    /**
     * 食材入库信息列表
     * @return ModelAndView
     */
    @PostMapping( "/getByIdSdId")
    @ResponseBody
    public ResultBean getWarehouseDetailByIdSdId(WarehouseDetailDTO warehouseDetailDTO) {
        logger.debug("查询食材入库信息列表！搜索条件：warehouseDetailDTO：" + warehouseDetailDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            warehouseDetailDTO.setUserId(existUser.getId());
            String path = WarehouseDetailController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = warehouseDetailService.getWarehouseDetailByIdSdId(warehouseDetailDTO,jobj);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取查询食材入库信息列表异常！", e);
        }
        return resultBean;
    }

    /**
     * 根据食材id得到食材最近采购单价
     * @param id
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/getIngredientUnitPrice", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getIngredientUnitPrice(@RequestParam("id") int id) {
        logger.debug("根据食材id得到食材最近采购单价 参数id-" + id);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = warehouseDetailService.getBasicInfoFoodById(id,existUser.getSdid());
        } catch (Exception e) {
            resultBean.setFlag(false);
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取基础食材信息列表异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取某些站点食材临期月数据管理
     * @return ModelAndView
     */
    @PostMapping( "/getIBMMonthBySdId")
    @ResponseBody
    public ResultBean getIBMMonthBySdId(WarehouseDetailDTO warehouseDetailDTO) {
        logger.debug("平台对传过来的站点进行食材临期月数据管理分析！查询条件：warehouseDetailDTO：" + warehouseDetailDTO);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        warehouseDetailDTO.setUserId(existUser.getId());
        String path = WarehouseDetailController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = warehouseDetailService.getIBMMonthBySdId(warehouseDetailDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("食材临期月数据管理分析异常");
            logger.error("食材临期月数据管理分析异常！", e);
        }
        return resultBean;
    }
}
