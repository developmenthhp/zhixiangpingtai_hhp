package com.zhixiangmain.controller.cgcc.scjcxx;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.BasicInfoFood;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto.BasicInfoFoodDTO;
import com.zhixiangmain.api.service.cgcc.BasicInfoOfFoodService;
import com.zhixiangmain.module.excel.ReadDataBase;
import com.zhixiangmain.module.user.User;
import com.zhixiangmain.web.requestdata.WebMassage;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.PageDataResult;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.json.JsonUtil;
import com.zhixiangmain.overallParam.OpslabConfig;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.controller.cgcc
 * @Description: 食材基础信息controller
 * @author: hhp
 * @date: 2019-02-24 10:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/bioFood")
public class BasicInfoOfFoodController {
    private static final Logger logger = LoggerFactory
            .getLogger(BasicInfoOfFoodController.class);
    @Reference(version = "1.0.0")
    private BasicInfoOfFoodService basicInfoOfFoodService;

    /**
     * 食材基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/bioFoodPage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回食材基础信息列表页面");
        ModelAndView mav = new ModelAndView("/cgcc/scjcxx");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回食材基础信息列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 食材使用分析信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/foodAnalysisPage")
    @ResponseBody
    public ModelAndView foodAnalysisPage() {
        logger.debug("返回食材基础信息列表页面");
        ModelAndView mav = new ModelAndView("/qksj/scsyfx/scsyfx");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回食材使用分析信息列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 食材基础信息列表
     * @return ModelAndView
     */
    @GetMapping( "/getBIOFoodList")
    @ResponseBody
    public void getBIOFoodList(@RequestParam("page") Integer page,
                                         @RequestParam("limit") Integer limit, BasicInfoFoodDTO basicInfoFoodDTO, HttpServletRequest request,HttpServletResponse response) {
        logger.debug("分页查询食材基础信息列表！搜索条件：basicInfoFood：" + basicInfoFoodDTO + ",page:" + page
                + ",每页记录数量limit:" + limit);
        PageDataResult pdr = new PageDataResult();
        try {

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            if(!IsEmptyUtils.isEmpty(existUser)){
                //这里默认登录的为蓝天总公司
            }else{
                //未登录，并传sdid 查询蓝天总公司和该试点的食材 sdid in(sdid1,sdid2)
                List<Integer> sdIds = new ArrayList<Integer>();
                if(!IsEmptyUtils.isEmpty(basicInfoFoodDTO.getSdId())){
                    sdIds.add(basicInfoFoodDTO.getSdId());
                    //蓝天总公司试点
                    sdIds.add(25);
                }
                basicInfoFoodDTO.setSdIds(sdIds);
            }
            pdr = basicInfoOfFoodService.getBIOFoodList(basicInfoFoodDTO,page,limit);
            pdr.setCode(Integer.parseInt(IStatusMessage.SystemStatus.SUCCESS.getCode()));
            pdr.setImgPath(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取基础食材信息列表!=pdr:" + pdr);

            //前端传过来的回调函数名称
            String callback = request.getParameter("callback");
            //用回调函数名称包裹返回数据，这样，返回数据就作为回调函数的参数传回去了，callback()这个（）里面放的是json格式
            String str = callback + "(" + new Gson().toJson(pdr) + ")";

            response.getWriter().write(str);

        } catch (Exception e) {
            e.printStackTrace();
            pdr.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取基础食材信息列表异常！", e);

        }



        /*return pdr;*/
    }

    /**
     * 食材基础信息列表
     * @return ModelAndView
     */
    @PostMapping( "/getInsideBIOFoodList")
    @ResponseBody
    public PageDataResult getInsideBIOFoodList(@RequestParam("page") Integer page,
                               @RequestParam("limit") Integer limit, BasicInfoFoodDTO basicInfoFoodDTO) {
        logger.debug("分页查询食材基础信息列表！搜索条件：basicInfoFood：" + basicInfoFoodDTO + ",page:" + page
                + ",每页记录数量limit:" + limit);
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            basicInfoFoodDTO.setSdId(existUser.getSdid());
            pdr = basicInfoOfFoodService.getInsideBIOFoodList(basicInfoFoodDTO,page,limit);
            pdr.setCode(Integer.parseInt(IStatusMessage.SystemStatus.SUCCESS.getCode()));
            pdr.setImgPath(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取基础食材信息列表!=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            pdr.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取基础食材信息列表异常！", e);
        }
        return pdr;
    }

    /**
     * 食材基础信息列表
     * @return ModelAndView
     */
    @PostMapping( "/checkName")
    @ResponseBody
    public ResultBean checkName(BasicInfoFood basicInfoFood) {
        logger.debug("校验食材是否存在！搜索条件：basicInfoFood：" + basicInfoFood);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            basicInfoFood.setSdId(existUser.getSdid());
            resultBean = basicInfoOfFoodService.checkName(basicInfoFood);
        } catch (Exception e) {
            resultBean.setFlag(false);
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取基础食材信息列表异常！", e);
        }
        return resultBean;
    }

    /**
     * 添加或编辑
     * @param basicInfoFood
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/setBasicInfoFood", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setPerm(BasicInfoFood basicInfoFood) {
        logger.debug("添加或编辑食材 basicInfoFood-" + basicInfoFood);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        basicInfoFood.setSdId(existUser.getSdid());
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(basicInfoFood.getId())&&basicInfoFood.getId()!=-1){
            try {
                resultBean = basicInfoOfFoodService.updateBasicInfoFood(basicInfoFood);
            } catch (Exception e) {
                e.printStackTrace();
                resultBean.setFlag(false);
                logger.error("更新食材异常！", e);
                resultBean.setMsg(WebMassage.UPDATE_INGREDIENT_ERROR);
                resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            }
            return resultBean;
        }else{
            try {
                resultBean = basicInfoOfFoodService.addBasicInfoFood(basicInfoFood);
            } catch (Exception e) {
                e.printStackTrace();
                resultBean.setFlag(false);
                logger.error("新增食材异常！", e);
                resultBean.setMsg(WebMassage.INSERT_INGREDIENT_ERROR);
                resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            }
            return resultBean;
        }
    }

    /**
     * 添加或编辑
     * @param id
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/getBasicInfoFoodById", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getBasicInfoFoodById(@RequestParam("id") int id) {
        logger.debug("根据i的查找食材 参数id-" + id);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = basicInfoOfFoodService.getBasicInfoFoodById(id,existUser.getSdid());
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            logger.error("新增食材异常！", e);
            resultBean.setMsg(WebMassage.INGREDIENT_SELECT_ERROR);
            resultBean.setObj(e.getMessage());
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        return resultBean;
    }

    /**
     * 添加或编辑
     * @param
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/getBasicInfoFoodByIds", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> getBasicInfoFoodByIds(@RequestParam("ids") String ids) {
        logger.debug("根据i的查找食材 参数id-" + ids);
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
        try {
            maps = basicInfoOfFoodService.getBasicInfoFoodByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据id得到食材异常！", e);
        }
        return maps;
    }

    /**
     * 添加或编辑
     * @param id
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/delBasicInfoFoodById", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delBasicInfoFoodById(@RequestParam("id") int id) {
        logger.debug("根据i的查找食材 参数id-" + id);
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        try {
            basicInfoOfFoodService.delBasicInfoFoodById(id,existUser.getSdid());
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            logger.error("删除食材异常！", e);
            resultBean.setMsg(WebMassage.DELETE_INGREDIENT_ERROR);
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return resultBean;
    }

    /**
     * 添加或编辑
     * @param
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/doAddFood", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean doAddFood() {
        logger.debug("cessho ////  dao ru 参数id-" );
        return basicInfoOfFoodService.doAddFood();
    }

    /**
     * 添加或编辑
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/searchAll", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean searchAll() {
        logger.debug("查找所有食材");
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = basicInfoOfFoodService.getAll(existUser.getSdid());
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            logger.error("查询所有食材异常！", e);
            resultBean.setMsg(WebMassage.INGREDIENT_SELECT_ERROR);
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return resultBean;
    }

    /**
     * 导入食材基础价格
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/importBasePrice", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean importBasePrice(@RequestParam("namePrices") String namePrices) {
        logger.debug("导入食材基础价格");
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        List<ReadDataBase> readDataBases= JSONArray.parseArray(namePrices, ReadDataBase.class);
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = basicInfoOfFoodService.importBasePrice(existUser.getSdid(),readDataBases);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            logger.error("导入食材基础价格！", e);
            resultBean.setMsg(WebMassage.IMPORT_BASE_PRICE_ERROR);
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return resultBean;
    }

    /**
     * 食材基础信息列表根据站点（所有平台用）
     * @return ModelAndView
     */
    @PostMapping( "/getBaseInfoOfFoodBySdId")
    @ResponseBody
    public ResultBean getBaseInfoOfFoodBySdId(BasicInfoFoodDTO basicInfoFoodDTO) {
        logger.debug("根据pid获取分类信息！");
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            basicInfoFoodDTO.setUserId(existUser.getId());
            String path = BasicInfoOfFoodController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = basicInfoOfFoodService.getBaseInfoOfFoodBySdId(basicInfoFoodDTO,jobj);
            resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            logger.debug("根据sdId获取分类结果 basicInfoFoodDTO:" + basicInfoFoodDTO);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            logger.error("根据sdId获取分类结果异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取所有食材信息，筛选食材名
     * @return ModelAndView
     */
    @PostMapping( "/getAllIngBBySdId")
    @ResponseBody
    public ResultBean getAllIngBBySdId(@RequestParam("page") Integer page,
                                        @RequestParam("limit") Integer limit, BasicInfoFoodDTO basicInfoFoodDTO) {
        logger.debug("分页查询食材名信息列表！搜索条件：basicInfoFoodDTO：" + basicInfoFoodDTO + ",page:" + page
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
            basicInfoFoodDTO.setUserId(existUser.getId());
            String path = BasicInfoOfFoodController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = basicInfoOfFoodService.getAllIngBBySdId(basicInfoFoodDTO,page,limit,jobj);
            resultBean.setObj(OpslabConfig.get("PROJECT_PREFIX"));
            logger.debug("获取筛选食材名信息列表!=basicInfoFoodDTO:" + basicInfoFoodDTO);

        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取筛选食材名信息列表异常！", e);
            resultBean.setMsg("获取筛选食材名信息列表异常！");
        }
        return resultBean;
    }

    /**
     * 食材基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getIngBUseBySdIdName")
    @ResponseBody
    public ModelAndView getIngBUseBySdIdName(BasicInfoFoodDTO basicInfoFoodDTO) {
        logger.debug("返回食材基础信息列表页面");
        ModelAndView mav = new ModelAndView("/qksj/scsyfx/ingredientStep");
        try
        {
            String path = BasicInfoOfFoodController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            ResultBean resultBean = basicInfoOfFoodService.getIngBUseBySdIdName(basicInfoFoodDTO,jobj);
            //resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取筛选食材名信息列表!=basicInfoFoodDTO:" + basicInfoFoodDTO);
			mav.addObject("resultBean", JSON.toJSONString(resultBean.getRows()));
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回食材基础信息列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 食材基础信息列表页面 需要登录
     * @return ModelAndView
     */
    @GetMapping( "/getIBUNLBySdIdName")
    @ResponseBody
    public ModelAndView getIBUNLBySdIdName(BasicInfoFoodDTO basicInfoFoodDTO) {
        logger.debug("返回食材基础信息列表页面，需要登录");
        ModelAndView mav = new ModelAndView("/qksj/scsyfx/ingredientStepNL");
        try
        {
            String path = BasicInfoOfFoodController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            ResultBean resultBean = basicInfoOfFoodService.getIngBUseBySdIdName(basicInfoFoodDTO,jobj);
            //resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取筛选食材名信息列表，需要登录!=basicInfoFoodDTO:" + basicInfoFoodDTO);
            mav.addObject("resultBean", JSON.toJSONString(resultBean.getRows()));
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回食材基础信息列表页面，需要登录 异常！", e);
        }
        return mav;
    }

    /**
     * 获取所有食材信息，条件筛选食材名
     * @return ModelAndView
     */
    @PostMapping( "/getIBUNLByFName")
    @ResponseBody
    public ResultBean getIBUNLByFName(BasicInfoFoodDTO basicInfoFoodDTO) {
        logger.debug("返回食材基础信息列表页面，条件筛选 basicInfoFoodDTO:");
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        basicInfoFoodDTO.setUserId(existUser.getId());
        String path = BasicInfoOfFoodController.class.getClassLoader().getResource("json/siteData.json").getPath();
        String siteJsonData = JsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(siteJsonData);
        try {
            resultBean = basicInfoOfFoodService.getIngBUseBySdIdName(basicInfoFoodDTO,jobj);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            resultBean.setMsg("返回食材基础信息列表页面，条件筛选异常");
            logger.error("返回食材基础信息列表页面，条件筛选异常！", e);
        }
        return resultBean;
    }
}
