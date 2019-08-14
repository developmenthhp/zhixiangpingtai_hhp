package com.zhixiangmain.impl.cpjs.zdcpbom;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cpjs.zdcpbom.FoodBase;
import com.zhixiangmain.api.module.cpjs.zdcpbom.dto.FoodBaseDTO;
import com.zhixiangmain.api.service.cpjs.zdcpbom.FoodBaseService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.foodIngredient.FoodIngredientMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.pagination.MyStartEndUtil;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.PageDataResult;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cpjs.zdcpbom.FoodBaseMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.impl.cpjs.zdcpbom
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-06 13:09
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = FoodBaseService.class)
public class FoodBaseServiceImpl implements FoodBaseService{
    private static final Logger logger = LoggerFactory
            .getLogger(FoodBaseServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private FoodBaseMapper foodBaseMapper;
    @Autowired
    private FoodIngredientMapper foodIngredientMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageDataResult getFoodBaseList(FoodBaseDTO foodBaseDTO, Integer page, Integer limit) {
        PageDataResult pdr = new PageDataResult();
        PageHelper.startPage(page, limit);
        List<Map<String,Object>> basicInfoFoods = foodBaseMapper.findFoodBaseList(foodBaseDTO);
        // 获取分页查询后的数据
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(basicInfoFoods);
        // 设置获取到的总记录数total：
        pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
        pdr.setList(basicInfoFoods);
        return pdr;
    }

    @Override
    public PageDataResult getInsideFoodBaseList(FoodBaseDTO foodBaseDTO, Integer page, Integer limit) {
        PageDataResult pdr = new PageDataResult();
        PageHelper.startPage(page, limit);

        String[] menuTypes = foodBaseDTO.getMenuTypeDtoSel();
        if(!IsEmptyUtils.isEmpty(menuTypes)){
            if(menuTypes.length==4){
                foodBaseDTO.setMenuTypeDto("15");
            }else if(menuTypes.length==3){
                if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")){
                    foodBaseDTO.setMenuTypeDto("7");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("8")){
                    foodBaseDTO.setMenuTypeDto("11");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBaseDTO.setMenuTypeDto("13");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBaseDTO.setMenuTypeDto("14");
                }
            }else if(menuTypes.length==2){
                if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")){
                    foodBaseDTO.setMenuTypeDto("3");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("4")){
                    foodBaseDTO.setMenuTypeDto("5");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("8")){
                    foodBaseDTO.setMenuTypeDto("9");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")){
                    foodBaseDTO.setMenuTypeDto("6");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("8")){
                    foodBaseDTO.setMenuTypeDto("10");
                }else if(Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBaseDTO.setMenuTypeDto("12");
                }
            }else if(menuTypes.length==1){
                foodBaseDTO.setMenuTypeDto(menuTypes[0]);
            }
        }

        List<Map<String,Object>> basicInfoFoods = foodBaseMapper.findInsideFoodBaseList(foodBaseDTO);
        // 获取分页查询后的数据
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(basicInfoFoods);
        // 设置获取到的总记录数total：
        pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
        pdr.setList(basicInfoFoods);
        return pdr;
    }

    @Override
    public ResultBean checkName(FoodBase foodBase) {
        ResultBean resultBean = new ResultBean();
        Object id = foodBaseMapper.checkNameTotal(foodBase);
        if(IsEmptyUtils.isEmpty(id)){
            resultBean.setCode(IStatusMessage.LogicStatus.NAME_NOT_EXISTS.getCode());
        }else{
            if(Integer.parseInt(String.valueOf(id))==foodBase.getId()){
                resultBean.setCode(IStatusMessage.LogicStatus.NAME_NOT_EXISTS.getCode());
            }else{
                resultBean.setCode(IStatusMessage.LogicStatus.NAME_EXISTS.getCode());
                resultBean.setMsg("该菜品已录入，不可重复录入，您可进行编辑!");
            }
        }
        resultBean.setFlag(true);
        return resultBean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean updateFoodBase(FoodBase foodBase) {
        ResultBean resultBean = new ResultBean();
        String[] menuTypes = foodBase.getMenuTypeSelect();
        if(!IsEmptyUtils.isEmpty(menuTypes)){
            if(menuTypes.length==4){
                foodBase.setMenuType("15");
            }else if(menuTypes.length==3){
                if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")){
                    foodBase.setMenuType("7");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("11");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("13");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("14");
                }
            }else if(menuTypes.length==2){
                if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")){
                    foodBase.setMenuType("3");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("4")){
                    foodBase.setMenuType("5");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("9");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")){
                    foodBase.setMenuType("6");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("10");
                }else if(Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("12");
                }
            }else if(menuTypes.length==1){
                foodBase.setMenuType(menuTypes[0]);
            }
        }

        foodBaseMapper.updateFoodBase(foodBase);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg("更新菜品成功");
        return resultBean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean addFoodBase(FoodBase foodBase) {
        ResultBean resultBean = new ResultBean();
        foodBase.setDeleteStatus("1");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        foodBase.setGmtDate(simpleDateFormat.format(new Date()));

        String[] menuTypes = foodBase.getMenuTypeSelect();
        if(!IsEmptyUtils.isEmpty(menuTypes)){
            if(menuTypes.length==4){
                foodBase.setMenuType("15");
            }else if(menuTypes.length==3){
                if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")){
                    foodBase.setMenuType("7");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("11");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("13");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("14");
                }
            }else if(menuTypes.length==2){
                if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("2")){
                    foodBase.setMenuType("3");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("4")){
                    foodBase.setMenuType("5");
                }else if(Arrays.asList(menuTypes).contains("1")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("9");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("4")){
                    foodBase.setMenuType("6");
                }else if(Arrays.asList(menuTypes).contains("2")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("10");
                }else if(Arrays.asList(menuTypes).contains("4")&&Arrays.asList(menuTypes).contains("8")){
                    foodBase.setMenuType("12");
                }
            }else if(menuTypes.length==1){
                foodBase.setMenuType(menuTypes[0]);
            }
        }

        foodBaseMapper.addFoodBase(foodBase);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg("新增菜品成功");
        return resultBean;
    }

    @Override
    public ResultBean getFoodBaseById(int id, Integer sdId) {
        ResultBean resultBean = new ResultBean();
        Map<String,Object> basicInfoOfFood = foodBaseMapper.findFoodBaseById(id,sdId);
        resultBean.setData(basicInfoOfFood);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean delFoodBaseById(int id, Integer sdId) {
        ResultBean resultBean = new ResultBean();
        Integer total = foodBaseMapper.delFoodBaseById(id,sdId);
        resultBean.setFlag(true);
        resultBean.setMsg("删除成功!");
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getAllFood(FoodBase foodBase) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> foods = foodBaseMapper.getAllFood(foodBase);
        resultBean.setRows(foods);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getAllFoodIdName(FoodBaseDTO foodBaseDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSdIds())){
            String[] sdIds = foodBaseDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    dataSourceName = jobj.get(sdId).toString();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取采购单
                    foodBaseDTO.setSdId(Integer.parseInt(sdId));
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getIdSdId())){
                        foodBaseDTO.setId(Integer.parseInt(foodBaseDTO.getIdSdId().split("-")[0]));
                    }
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getMainCategoryIdSdId())){
                        foodBaseDTO.setMainCategoryId(Integer.parseInt(foodBaseDTO.getMainCategoryIdSdId().split("-")[0]));
                    }
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSmallCategoryIdSdId())){
                        foodBaseDTO.setSmallCategoryId(Integer.parseInt(foodBaseDTO.getSmallCategoryIdSdId().split("-")[0]));
                    }
                    List<Map<String,Object>> foods = foodBaseMapper.findAllFoodIdName(foodBaseDTO);
                    mainLPLs.addAll(foods);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(foodBaseDTO.getUserId());
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj)){
                    if(!IsEmptyUtils.isEmpty(jobj.get(siteVO.getSdId().toString()))){
                        dataSourceName = jobj.get(siteVO.getSdId().toString()).toString();
                    }
                    //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                    if(!IsEmptyUtils.isEmpty(dataSourceName)){
                        logger.info("切换至--"+dataSourceName+"----数据源");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                        //获取采购单
                        foodBaseDTO.setSdId(siteVO.getSdId());
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getIdSdId())){
                            foodBaseDTO.setId(Integer.parseInt(foodBaseDTO.getIdSdId().split("-")[0]));
                        }
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getMainCategoryIdSdId())){
                            foodBaseDTO.setMainCategoryId(Integer.parseInt(foodBaseDTO.getMainCategoryIdSdId().split("-")[0]));
                        }
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSmallCategoryIdSdId())){
                            foodBaseDTO.setSmallCategoryId(Integer.parseInt(foodBaseDTO.getSmallCategoryIdSdId().split("-")[0]));
                        }
                        List<Map<String,Object>> foods = foodBaseMapper.findAllFoodIdName(foodBaseDTO);
                        mainLPLs.addAll(foods);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }

        resultBean.setRows(mainLPLs);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getAllFoodBBySdId(FoodBaseDTO foodBaseDTO, Integer page, Integer limit, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> foodAllList = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();
        //所有食材名
        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSdIds())){
            String[] sdIds = foodBaseDTO.getSdIds().split(",");
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(sdId);
                    }else{
                        stringBuffer.append(","+sdId);
                    }
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取条数
                    Integer totalStatus = foodBaseMapper.findPaginationDataTotal(foodBaseDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 留样分页查询：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                foodBaseDTO.setStart(startEndNums.get(i).get(0));
                foodBaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                foodBaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                foodBaseDTO.setSiteName(siteData.getName());
                //获取分页数据
                List<Map<String,Object>> foods  = foodBaseMapper.findAllFoodBBySdId(foodBaseDTO);
                foodAllList.addAll(foods);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //如果sdid是空查询该用户下所有站点 for循环取该站点数据
            List<SiteVO> userSites = siteMapper.findUserSites(foodBaseDTO.getUserId());
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(siteVO.getSdId());
                    }else{
                        stringBuffer.append(","+siteVO.getSdId());
                    }
                    logger.info("切换至--"+dataSourceName+"----数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取条数
                    Integer totalStatus = foodBaseMapper.findPaginationDataTotal(foodBaseDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 留样分页查询 未传试点id：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                foodBaseDTO.setStart(startEndNums.get(i).get(0));
                foodBaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                foodBaseDTO.setSiteName(siteData.getName());
                //获取分页数据
                List<Map<String,Object>> foods  = foodBaseMapper.findAllFoodBBySdId(foodBaseDTO);
                foodAllList.addAll(foods);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(foodAllList);
        resultBean.setTotal(total);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getCompositionBySdIdId(FoodBaseDTO foodBaseDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        //菜品组成成分
        List<Map<String,Object>> slopsList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSdIds())){
            String[] sdIds = foodBaseDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //菜品组成成分
                    foodBaseDTO.setSdId(Integer.parseInt(sdId));
                    //菜品组成成分
                    List<Map<String,Object>> slopsMap  = foodIngredientMapper.findCompositionBySdIdId(foodBaseDTO);
                    slopsList.addAll(slopsMap);
                }
            }
            resultBean.setRows(slopsList);
            resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            resultBean.setCode(IStatusMessage.SystemStatus.LOGIC_ERROR.getCode());
        }
        return resultBean;
    }

}
