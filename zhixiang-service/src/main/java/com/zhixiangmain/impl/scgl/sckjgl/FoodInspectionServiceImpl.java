package com.zhixiangmain.impl.scgl.sckjgl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.scgl.sckjgl.dto.FoodInspectionDTO;
import com.zhixiangmain.api.service.scgl.sckjgl.FoodInspectionService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.scgl.sckjgl.FoodInspectionMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.apache.commons.lang.ArrayUtils;
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
 * @Package com.zhixiangmain.impl.scgl.sckjgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-30 16:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = FoodInspectionService.class)
public class FoodInspectionServiceImpl implements FoodInspectionService {
    private static final Logger logger = LoggerFactory
            .getLogger(FoodInspectionServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private FoodInspectionMapper foodInspectionMapper;

    @Override
    public ResultBean getByWeek(FoodInspectionDTO foodInspectionDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mapList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(foodInspectionDTO.getSdIds())){
            String[] sdIds = foodInspectionDTO.getSdIds().split(",");
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
                    foodInspectionDTO.setSdId(Integer.parseInt(sdId));
                    foodInspectionDTO.setSiteName(siteData.getName());
                    foodInspectionDTO.setSitePhoto(siteData.getPhoto());
                    List<Map<String,Object>> maps = foodInspectionMapper.findByWeek(foodInspectionDTO);
                    mapList.addAll(maps);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(foodInspectionDTO.getUserId());
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
                        foodInspectionDTO.setSdId(siteVO.getSdId());
                        foodInspectionDTO.setSiteName(siteData.getName());
                        foodInspectionDTO.setSitePhoto(siteData.getPhoto());
                        List<Map<String,Object>> maps = foodInspectionMapper.findByWeek(foodInspectionDTO);
                        mapList.addAll(maps);
                    }
                }
            }

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(mapList);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getFInsBySdId(FoodInspectionDTO foodInspectionDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        //检测正常
        List<Map<String,Object>> normalList = Lists.newArrayList();
        //检测异常
        List<Map<String,Object>> abnormalList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(foodInspectionDTO)){
            if(!IsEmptyUtils.isEmpty(foodInspectionDTO.getSelectYear())&&!IsEmptyUtils.isEmpty(foodInspectionDTO.getSelectMonth())){
                //都不为空
                foodInspectionDTO.setSelectYearMonth(foodInspectionDTO.getSelectYear()+foodInspectionDTO.getSelectMonth());
            }else if(IsEmptyUtils.isEmpty(foodInspectionDTO.getSelectYear())&&IsEmptyUtils.isEmpty(foodInspectionDTO.getSelectMonth())){
                //都为空
                //都不为空
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
                foodInspectionDTO.setSelectYearMonth(simpleDateFormat.format(new Date()));
            }
        }
        if(!IsEmptyUtils.isEmpty(foodInspectionDTO.getSdIds())){
            String[] sdIds = foodInspectionDTO.getSdIds().split(",");
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
                    foodInspectionDTO.setSdId(Integer.parseInt(sdId));
                    FoodInspectionDTO foodInspectionDTO1 = new FoodInspectionDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(foodInspectionDTO, foodInspectionDTO1);

                    foodInspectionDTO.setWarn("1");
                    foodInspectionDTO1.setWarn("2");
                    //食材入库月数据，这里记录的是合格和不合格条数，不区分食材，
                    List<Map<String,Object>> normalMap  = foodInspectionMapper.findFInsBySdId(foodInspectionDTO);
                    normalList = getMonthDataList(normalList,normalMap);

                    List<Map<String,Object>> abnormalMap  = foodInspectionMapper.findFInsBySdId(foodInspectionDTO1);
                    abnormalList = getMonthDataList(abnormalList,abnormalMap);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(foodInspectionDTO.getUserId());
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
                        foodInspectionDTO.setSdId(siteVO.getSdId());
                        FoodInspectionDTO foodInspectionDTO1 = new FoodInspectionDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(foodInspectionDTO, foodInspectionDTO1);

                        foodInspectionDTO.setWarn("1");
                        foodInspectionDTO1.setWarn("2");
                        //食材入库月数据，这里记录的是合格和不合格条数，不区分食材，
                        List<Map<String,Object>> normalMap  = foodInspectionMapper.findFInsBySdId(foodInspectionDTO);
                        normalList = getMonthDataList(normalList,normalMap);

                        List<Map<String,Object>> abnormalMap  = foodInspectionMapper.findFInsBySdId(foodInspectionDTO1);
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
    public ResultBean getFInsByDateSdId(FoodInspectionDTO foodInspectionDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        /*int billTotal = 0;
        int healthCertificateTotal = 0;
        int certificateTotal = 0;
        if(!IsEmptyUtils.isEmpty(foodInspectionDTO.getSdIds())){
            String[] sdIds = foodInspectionDTO.getSdIds().split(",");
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
                    foodInspectionDTO.setSdId(Integer.parseInt(sdId));
                    WarehousDetailsDTO warehousDetailsDTO1 = new WarehousDetailsDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(warehousDetailsDTO, warehousDetailsDTO1);
                    System.out.println(warehousDetailsDTO1.getSdIds()+",,,,,,,,,,,,"+warehousDetailsDTO1.getSdId());
                    warehousDetailsDTO1.setNotePictures("pic");
                    //获取票据图片不为空条数,这里由于不同站点票据不同，不进行相同食材票据条数合并
                    Integer billCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                    billTotal = billTotal + billCount;

                    warehousDetailsDTO1.setNotePictures(null);
                    warehousDetailsDTO1.setSanitaryCertificate("pic");
                    //获取卫生证图片不为空条数
                    Integer healthCertificateCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                    healthCertificateTotal = healthCertificateTotal + healthCertificateCount;

                    warehousDetailsDTO1.setSanitaryCertificate(null);
                    warehousDetailsDTO1.setCertificateOfSoundness("pic");
                    //获取合格证图片不为空条数
                    Integer certificateCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                    certificateTotal = certificateTotal + certificateCount;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(warehousDetailsDTO.getUserId());
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
                        warehousDetailsDTO.setSdId(siteVO.getSdId());

                        WarehousDetailsDTO warehousDetailsDTO1 = new WarehousDetailsDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(warehousDetailsDTO, warehousDetailsDTO1);
                        System.out.println(warehousDetailsDTO1.getSdIds()+",,,,,,,,,,,,"+warehousDetailsDTO1.getSdId());
                        warehousDetailsDTO1.setNotePictures("pic");
                        //获取票据图片不为空条数,这里由于不同站点票据不同，不进行相同食材票据条数合并
                        Integer billCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                        billTotal = billTotal + billCount;

                        warehousDetailsDTO1.setNotePictures(null);
                        warehousDetailsDTO1.setSanitaryCertificate("pic");
                        //获取卫生证图片不为空条数
                        Integer healthCertificateCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                        healthCertificateTotal = healthCertificateTotal + healthCertificateCount;

                        warehousDetailsDTO1.setSanitaryCertificate(null);
                        warehousDetailsDTO1.setCertificateOfSoundness("pic");
                        //获取合格证图片不为空条数
                        Integer certificateCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                        certificateTotal = certificateTotal + certificateCount;
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //有票据数
        resultBean.setData(billTotal);
        //有卫生证数
        resultBean.setTotal(healthCertificateTotal);
        //有合格证数
        resultBean.setTotalPage(certificateTotal);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());*/
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
