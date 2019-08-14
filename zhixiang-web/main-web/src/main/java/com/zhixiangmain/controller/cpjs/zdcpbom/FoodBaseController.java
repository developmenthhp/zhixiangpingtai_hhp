package com.zhixiangmain.controller.cpjs.zdcpbom;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cpjs.zdcpbom.FoodBase;
import com.zhixiangmain.api.module.cpjs.zdcpbom.dto.FoodBaseDTO;
import com.zhixiangmain.api.service.cpjs.zdcpbom.FoodBaseService;
import com.zhixiangmain.module.user.User;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.controller.cpjs.xssjtj
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-06 10:40
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/foodBase")
public class FoodBaseController {
    private static final Logger logger = LoggerFactory
            .getLogger(FoodBaseController.class);
    @Reference(version = "1.0.0")
    private FoodBaseService foodBaseService;

    /**
     * 食材基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/foodBasePage")
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("返回食材基础信息列表页面");
        ModelAndView mav = new ModelAndView("/cpjs/zdcpbom/zdcpbom");
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
     * 菜品分析列表页面
     * @return ModelAndView
     */
    @GetMapping( "/foodCompositionPage")
    @ResponseBody
    public ModelAndView getFoodCompositionPage() {
        logger.debug("返回菜品分析列表页面");
        ModelAndView mav = new ModelAndView("/qksj/cpfx/cpfx");
        try {
			/*List<Permission> permList = authService.permList();
			logger.debug("权限列表查询=permList:" + permList);
			mav.addObject("permList", permList);*/
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回菜品分析列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 食材基础信息列表
     * @return ModelAndView
     */
    @GetMapping( "/getFoodBaseList")
    @ResponseBody
    public void getFoodBaseList(@RequestParam("page") Integer page,
                                @RequestParam("limit") Integer limit, FoodBaseDTO foodBaseDTO, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("分页查询菜品列表！搜索条件：foodBaseDTO：" + foodBaseDTO + ",page:" + page
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
            /*User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            foodBaseDTO.setSdId(existUser.getSdid());*/
            /*if(!IsEmptyUtils.isEmpty(existUser)){
                //这里默认登录的为蓝天总公司
            }else{
                //未登录，并传sdid 查询蓝天总公司和该试点的食材 sdid in(sdid1,sdid2)
                List<Integer> sdIds = new ArrayList<Integer>();
                if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSdId())){
                    sdIds.add(foodBaseDTO.getSdId());
                    //蓝天总公司试点
                    sdIds.add(23);
                }

            }*/
            pdr = foodBaseService.getFoodBaseList(foodBaseDTO,page,limit);
            pdr.setCode(Integer.parseInt(IStatusMessage.SystemStatus.SUCCESS.getCode()));
            pdr.setImgPath(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("分页查询菜品列表!=pdr:" + pdr);

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
    @PostMapping( "/getInsideFoodBaseList")
    @ResponseBody
    public PageDataResult getInsideFoodBaseList(@RequestParam("page") Integer page,
                                               @RequestParam("limit") Integer limit, FoodBaseDTO foodBaseDTO) {
        logger.debug("分页查询食材基础信息列表！搜索条件：foodBaseDTO：" + foodBaseDTO + ",page:" + page
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
            foodBaseDTO.setSdId(existUser.getSdid());
            pdr = foodBaseService.getInsideFoodBaseList(foodBaseDTO,page,limit);
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
    public ResultBean checkName(FoodBase foodBase) {
        logger.debug("校验菜品是否存在！搜索条件：foodBase：" + foodBase);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        foodBase.setSdId(existUser.getSdid());
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = foodBaseService.checkName(foodBase);
        } catch (Exception e) {
            resultBean.setFlag(false);
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("校验菜品唯一异常！", e);
        }
        return resultBean;
    }

    /**
     * 添加或编辑
     * @param foodBase
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/setFoodBase", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setFoodBase(FoodBase foodBase) {
        logger.debug("添加或编辑食材 foodBase-" + foodBase);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        foodBase.setSdId(existUser.getSdid());
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(foodBase.getId())&&foodBase.getId()!=-1){
            try {
                resultBean = foodBaseService.updateFoodBase(foodBase);
            } catch (Exception e) {
                e.printStackTrace();
                resultBean.setFlag(false);
                logger.error("更新菜品异常！", e);
                resultBean.setMsg("更新菜品出错，请您稍后再试");
                resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            }
            return resultBean;
        }else{
            try {
                resultBean = foodBaseService.addFoodBase(foodBase);
            } catch (Exception e) {
                e.printStackTrace();
                resultBean.setFlag(false);
                logger.error("新增菜品异常！", e);
                resultBean.setMsg("新增菜品出错，请您稍后再试");
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
    @RequestMapping(value = "/getFoodBaseById", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getFoodBaseById(@RequestParam("id") int id) {
        logger.debug("根据i的查找食材 参数id-" + id);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = foodBaseService.getFoodBaseById(id,existUser.getSdid());
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            logger.error("新增食材异常！", e);
            resultBean.setMsg("查询异常，请您联系管理员");
            resultBean.setObj(e.getMessage());
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        return resultBean;
    }

    /**
     * 添加或编辑
     * @param id
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/delFoodBaseById", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delFoodBaseById(@RequestParam("id") int id) {
        logger.debug("根据i的查找食材 参数id-" + id);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = foodBaseService.delFoodBaseById(id,existUser.getSdid());
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setFlag(false);
            logger.error("删除食材异常！", e);
            resultBean.setMsg("删除食材异常，请您联系管理员");
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return resultBean;
    }

    /**
     * 查找所有可用菜品
     * @return ResultBean
     */
    @PostMapping( "/getAllFood")
    @ResponseBody
    public ResultBean getAllFood(FoodBase foodBase) {
        logger.debug("查找所有可用菜品！搜索条件：foodBase：" + foodBase);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        foodBase.setSdId(existUser.getSdid());
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = foodBaseService.getAllFood(foodBase);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            resultBean.setFlag(false);
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("查找所有可用菜品异常！", e);
        }
        return resultBean;
    }

    /**
     * 查找所有可用菜品的id和name
     * @return ResultBean
     */
    @PostMapping( "/getAllFoodIdName")
    @ResponseBody
    public ResultBean getAllFoodIdName(FoodBaseDTO FoodBaseDTO) {
        logger.debug("查找所有可用菜品的id和name！搜索条件：FoodBaseDTO：" + FoodBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            FoodBaseDTO.setUserId(existUser.getId());
            String path = FoodBaseController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = foodBaseService.getAllFoodIdName(FoodBaseDTO,jobj);
        } catch (Exception e) {
            resultBean.setFlag(false);
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("查找所有可用菜品的id和name异常！", e);
        }
        return resultBean;
    }

    /**
     * 获取所有菜品信息，筛选菜品
     * @return ModelAndView
     */
    @PostMapping( "/getAllFoodBBySdId")
    @ResponseBody
    public ResultBean getAllFoodBBySdId(@RequestParam("page") Integer page,
                                       @RequestParam("limit") Integer limit, FoodBaseDTO foodBaseDTO) {
        logger.debug("分页查询菜品信息，筛选菜品列表！搜索条件：foodBaseDTO：" + foodBaseDTO + ",page:" + page
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
            foodBaseDTO.setUserId(existUser.getId());
            String path = FoodBaseController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            resultBean = foodBaseService.getAllFoodBBySdId(foodBaseDTO,page,limit,jobj);
            resultBean.setObj(OpslabConfig.get("PROJECT_PREFIX"));
            logger.debug("获取筛选菜品信息，筛选菜品列表!=foodBaseDTO:" + foodBaseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(Integer.parseInt(IStatusMessage.SystemStatus.ERROR.getCode()));
            logger.error("获取筛选菜品信息，筛选菜品列表异常！", e);
            resultBean.setMsg("获取筛选菜品信息，筛选菜品列表异常！");
        }
        return resultBean;
    }

    /**
     * 菜品基础信息列表页面
     * @return ModelAndView
     */
    @GetMapping( "/getCompositionBySdIdId")
    @ResponseBody
    public ModelAndView getCompositionBySdIdId(FoodBaseDTO foodBaseDTO) {
        logger.debug("返回菜品基础信息列表页面");
        ModelAndView mav = new ModelAndView("/qksj/cpfx/foodComposition");
        try
        {
            String path = FoodBaseController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            ResultBean resultBean = foodBaseService.getCompositionBySdIdId(foodBaseDTO,jobj);
            //resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取筛选菜品信息列表!=foodBaseDTO:" + foodBaseDTO);
            mav.addObject("resultBean", JSON.toJSONString(resultBean.getRows()));
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回菜品基础信息列表页面异常！", e);
        }
        return mav;
    }

    /**
     * 菜品BOM信息列表页面 需要登录
     * @return ModelAndView
     */
    @GetMapping( "/getComLFBySdIdId")
    @ResponseBody
    public ModelAndView getIBUNLBySdIdName(FoodBaseDTO foodBaseDTO) {
        logger.debug("返回菜品BOM信息列表页面，需要登录");
        ModelAndView mav = new ModelAndView("/qksj/cpfx/foodCompositionNL");
        try
        {
            String path = FoodBaseController.class.getClassLoader().getResource("json/siteData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);
            ResultBean resultBean = foodBaseService.getCompositionBySdIdId(foodBaseDTO,jobj);
            //resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
            logger.debug("获取筛选菜品BOM信息列表，需要登录!=foodBaseDTO:" + foodBaseDTO);
            mav.addObject("resultBean", JSON.toJSONString(resultBean.getRows()));
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("返回菜品BOM信息列表页面，需要登录 异常！", e);
        }
        return mav;
    }
}
