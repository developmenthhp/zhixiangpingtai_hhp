package com.zhixiangmain.impl.zhck.hwhsgl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.zhck.hwhsgl.dto.HumitureDTO;
import com.zhixiangmain.api.service.zhck.hwhsgl.HumitureService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.dao.zhck.hwhsgl.HumitureMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.zhck.hwhsgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-03 17:32
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = HumitureService.class)
public class HumitureServiceImpl implements HumitureService {
    private static final Logger logger = LoggerFactory
            .getLogger(HumitureServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private HumitureMapper humitureMapper;

    @Override
    public ResultBean getHumitureBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj) {
        return new ResultBean();
        /*ResultBean resultBean = new ResultBean();
        Integer allFWallCount = 0;
        Integer warningCount = 0;
        Integer normalCount = 0;
        List<Map<String,Object>> siteFWAlertMaps = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(baseEntityDTO.getSdIds())){
            String[] sdIds = baseEntityDTO.getSdIds().split(",");
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
                    //获取采购单
                    baseEntityDTO.setSdId(Integer.parseInt(sdId));

                    Integer countAll = ratGuardBaseMapper.findAllRGBase(baseEntityDTO);

                    RatplateAlertDTO ratplateAlertDTO = new RatplateAlertDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, ratplateAlertDTO);

                    ratplateAlertDTO.setRatplateStatus("1");

                    if(countAll>0){
                        allFWallCount = allFWallCount + 1;
                        //如果这个点装了，需要查询, 正在报警中
                        Integer warningTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                        if(warningTotal>0){
                            //当前在报警
                            warningCount = warningCount + 1;
                        }
                        ratplateAlertDTO.setRatplateStatus("2");
                        Integer normalTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                        if(normalTotal>0){
                            //当前已处理或未报警
                            normalCount = normalCount + 1;
                        }
                        ratplateAlertDTO.setRatplateStatus("");

                        //查询所有报警数,包括已处理的报警
                        Integer allWarnTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                        Map<String,Object> alertMap = new HashMap<>();
                        alertMap.put("name",siteData.getName());
                        alertMap.put("photo",siteData.getPhoto());
                        alertMap.put("warningTotal",allWarnTotal);
                        siteFWAlertMaps.add(alertMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(baseEntityDTO.getUserId());
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj)){
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
                        logger.info("切换至--"+dataSourceName+"----数据源");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                        //获取采购单
                        baseEntityDTO.setSdId(siteVO.getSdId());

                        Integer countAll = ratGuardBaseMapper.findAllRGBase(baseEntityDTO);

                        RatplateAlertDTO ratplateAlertDTO = new RatplateAlertDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, ratplateAlertDTO);

                        ratplateAlertDTO.setRatplateStatus("1");

                        if(countAll>0){
                            allFWallCount = allFWallCount + 1;
                            //如果这个点装了，需要查询, 正在报警中
                            Integer warningTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                            if(warningTotal>0){
                                //当前在报警
                                warningCount = warningCount + 1;
                            }
                            System.out.println(warningCount);
                            ratplateAlertDTO.setRatplateStatus("2");
                            Integer normalTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                            if(normalTotal>0){
                                //当前已处理或未报警
                                normalCount = normalCount + 1;
                            }
                            ratplateAlertDTO.setRatplateStatus("");
                            //查询所有报警数,包括已处理的报警
                            Integer allWarnTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                            Map<String,Object> alertMap = new HashMap<>();
                            alertMap.put("name",siteData.getName());
                            alertMap.put("photo",siteData.getPhoto());
                            alertMap.put("warningTotal",allWarnTotal);
                            siteFWAlertMaps.add(alertMap);
                        }
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //已安装点总数
        resultBean.setTotal(allFWallCount);
        //报警站点数
        resultBean.setData(warningCount);
        //未报警站点数
        resultBean.setTotalPage(normalCount);
        //用作页面站点排行
        resultBean.setRows(siteFWAlertMaps);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;*/
    }

    @Override
    public ResultBean getHumMonthBySdId(HumitureDTO humitureDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        //检测正常
        List<Map<String,Object>> normalList = Lists.newArrayList();
        //检测异常
        List<Map<String,Object>> abnormalList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(humitureDTO)){
            if(!IsEmptyUtils.isEmpty(humitureDTO.getSelectYear())&&!IsEmptyUtils.isEmpty(humitureDTO.getSelectMonth())){
                //都不为空
                humitureDTO.setSelectYearMonth(humitureDTO.getSelectYear()+humitureDTO.getSelectMonth());
            }else if(IsEmptyUtils.isEmpty(humitureDTO.getSelectYear())&&IsEmptyUtils.isEmpty(humitureDTO.getSelectMonth())){
                //都为空
                //都不为空
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
                humitureDTO.setSelectYearMonth(simpleDateFormat.format(new Date()));
            }
        }
        if(!IsEmptyUtils.isEmpty(humitureDTO.getSdIds())){
            String[] sdIds = humitureDTO.getSdIds().split(",");
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
                    //获取采购单
                    humitureDTO.setSdId(Integer.parseInt(sdId));
                    HumitureDTO humitureDTO1 = new HumitureDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(humitureDTO, humitureDTO1);

                    humitureDTO.setStatus(1);
                    humitureDTO1.setStatus(2);
                    //食材入库月数据，这里记录的是合格和不合格条数，不区分食材，
                    List<Map<String,Object>> normalMap  = humitureMapper.findHumMonthBySdId(humitureDTO);
                    normalList = getMonthDataList(normalList,normalMap);

                    List<Map<String,Object>> abnormalMap  = humitureMapper.findHumMonthBySdId(humitureDTO1);
                    abnormalList = getMonthDataList(abnormalList,abnormalMap);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(humitureDTO.getUserId());
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj)){
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
                        logger.info("切换至--"+dataSourceName+"----数据源");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                        //获取采购单
                        humitureDTO.setSdId(siteVO.getSdId());
                        HumitureDTO humitureDTO1 = new HumitureDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(humitureDTO, humitureDTO1);

                        humitureDTO.setStatus(1);
                        humitureDTO1.setStatus(2);
                        //食材入库月数据，这里记录的是合格和不合格条数，不区分食材，
                        List<Map<String,Object>> normalMap  = humitureMapper.findHumMonthBySdId(humitureDTO);
                        normalList = getMonthDataList(normalList,normalMap);

                        List<Map<String,Object>> abnormalMap  = humitureMapper.findHumMonthBySdId(humitureDTO1);
                        abnormalList = getMonthDataList(abnormalList,abnormalMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //检测正常
        resultBean.setRows(normalList);
        //检测异常
        resultBean.setData(abnormalList);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getHumByDateSdId(HumitureDTO humitureDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int normal = 0;
        int abnormal = 0;
        if(!IsEmptyUtils.isEmpty(humitureDTO.getSdIds())){
            String[] sdIds = humitureDTO.getSdIds().split(",");
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
                    //获取采购单
                    humitureDTO.setSdId(Integer.parseInt(sdId));
                    HumitureDTO humitureDTO1 = new HumitureDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(humitureDTO, humitureDTO1);

                    humitureDTO.setStatus(1);
                    humitureDTO1.setStatus(2);
                    //获取该天正常温湿度
                    Integer normalCount = humitureMapper.findHumByDateSdId(humitureDTO);
                    normal = normal + normalCount;

                    //获取卫生证图片不为空条数
                    Integer abnormalCount = humitureMapper.findHumByDateSdId(humitureDTO1);
                    abnormal = abnormal + abnormalCount;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(humitureDTO.getUserId());
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj)){
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
                        logger.info("切换至--"+dataSourceName+"----数据源");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                        //获取采购单
                        humitureDTO.setSdId(siteVO.getSdId());

                        HumitureDTO humitureDTO1 = new HumitureDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(humitureDTO, humitureDTO1);

                        humitureDTO.setStatus(1);
                        humitureDTO1.setStatus(2);
                        //获取该天正常温湿度
                        Integer normalCount = humitureMapper.findHumByDateSdId(humitureDTO);
                        normal = normal + normalCount;

                        //获取该天异常温度
                        Integer abnormalCount = humitureMapper.findHumByDateSdId(humitureDTO1);
                        abnormal = abnormal + abnormalCount;
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //正常条数
        resultBean.setData(normal);
        //异常条数
        resultBean.setTotal(abnormal);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    public List<Map<String,Object>> getMonthDataList(List<Map<String,Object>> allList,List<Map<String,Object>> paramList){
        if(!IsEmptyUtils.isEmpty(allList)){
            for(Map<String,Object> map:paramList){
                int index = -1;
                for(int i=0;i<allList.size();i++){
                    String curDataLcrDate = map.get("createTime").toString();
                    String curDataLcrAllDate = allList.get(i).get("createTime").toString();
                    if(curDataLcrDate.equals(curDataLcrAllDate)){
                        index = i;
                        //相同日期数据相加
                        /*//这里还要区分是否是同一种食材，这里取的是同一天入库的食材的种类数量
                        String[] mapIngredientId = map.get("ingredientId").toString().split(",");
                        String[] allIngredientId = allList.get(i).get("ingredientId").toString().split(",");
                        int countIngId = 0;
                        int addCount = 0;
                        for(String ingredientId:mapIngredientId){
                            if(ArrayUtils.contains(allIngredientId,ingredientId)){
                                //当前数组有食材在另一个数据源相同日期存在，记录这些相同数据条数
                                countIngId = countIngId + 1;
                            }else{
                                addCount = addCount + 1;
                                //将不同食材放入，该一天
                                allList.get(i).put("ingredientId",allList.get(i).get("ingredientId")+","+ingredientId);
                            }
                        }*/
                        allList.get(i).put("dataCount",Integer.parseInt(map.get("dataCount").toString())+Integer.parseInt(allList.get(i).get("dataCount").toString()));
                        break;
                    }else{
                        index = -1;
                    }
                }
                if(index == -1){
                    allList.add(map);
                }
            }
        }else{
            allList.addAll(paramList);
        }
        return allList;
    }
}
