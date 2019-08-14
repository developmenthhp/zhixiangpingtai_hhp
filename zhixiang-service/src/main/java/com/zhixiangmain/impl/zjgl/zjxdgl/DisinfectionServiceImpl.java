package com.zhixiangmain.impl.zjgl.zjxdgl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.disinfectionRcd.dto.DisinfectionRcdDTO;
import com.zhixiangmain.api.service.zjgl.zjxdgl.DisinfectionService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.disinfectionRcd.DisinfectionRcdMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.dao.zjgl.zjxdgl.DisinfectionMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.pagination.MyStartEndUtil;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.zjgl.zjxdgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-04 12:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = DisinfectionService.class)
public class DisinfectionServiceImpl implements DisinfectionService {
    private static final Logger logger = LoggerFactory
            .getLogger(DisinfectionServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private DisinfectionMapper disinfectionMapper;
    @Autowired
    private DisinfectionRcdMapper disinfectionRcdMapper;

    @Override
    public ResultBean getDisMonthBySdId(DisinfectionRcdDTO disinfectionRcdDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        //消毒按规完成
        List<Map<String,Object>> normalList = Lists.newArrayList();
        //消毒异常记录数
        List<Map<String,Object>> abnormalList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(disinfectionRcdDTO.getSdIds())){
            String[] sdIds = disinfectionRcdDTO.getSdIds().split(",");
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
                    disinfectionRcdDTO.setSdId(Integer.parseInt(sdId));
                    DisinfectionRcdDTO disinfectionRcdDTO1 = new DisinfectionRcdDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(disinfectionRcdDTO, disinfectionRcdDTO1);
                    disinfectionRcdDTO.setDisinReason("1");
                    disinfectionRcdDTO1.setDisinReason("2");
                    //消毒按规完成
                    List<Map<String,Object>> normalMap  = disinfectionRcdMapper.findDisMonthBySdId(disinfectionRcdDTO);
                    normalList = getMonthDataList(normalList,normalMap);
                    //消毒异常记录数
                    List<Map<String,Object>> abnormalMap  = disinfectionRcdMapper.findDisMonthBySdId(disinfectionRcdDTO1);
                    abnormalList = getMonthDataList(abnormalList,abnormalMap);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(disinfectionRcdDTO.getUserId());
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
                        disinfectionRcdDTO.setSdId(siteVO.getSdId());
                        //本月消毒次数
                        List<Map<String,Object>> normalMap  = disinfectionRcdMapper.findDisMonthBySdId(disinfectionRcdDTO);
                        normalList = getMonthDataList(normalList,normalMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //本月消毒安规完成
        resultBean.setRows(normalList);
        //本月消毒异常
        resultBean.setData(abnormalList);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getDisByDateSdId(DisinfectionRcdDTO disinfectionRcdDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int normal = 0;
        int abnormal = 0;
        if(!IsEmptyUtils.isEmpty(disinfectionRcdDTO.getSdIds())){
            String[] sdIds = disinfectionRcdDTO.getSdIds().split(",");
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
                    disinfectionRcdDTO.setSdId(Integer.parseInt(sdId));
                    DisinfectionRcdDTO disinfectionRcdDTO1 = new DisinfectionRcdDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(disinfectionRcdDTO, disinfectionRcdDTO1);

                    /*humitureDTO.setStatus(1);
                    humitureDTO1.setStatus(2);*/
                    //获取该天正常消毒
                    Integer normalCount = disinfectionRcdMapper.findDisByDateSdId(disinfectionRcdDTO);
                    normal = normal + normalCount;

                    //获取该天异常消毒
                    Integer abnormalCount = disinfectionRcdMapper.findDisByDateSdId(disinfectionRcdDTO1);
                    abnormal = abnormal + abnormalCount;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(disinfectionRcdDTO.getUserId());
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
                        disinfectionRcdDTO.setSdId(siteVO.getSdId());
                        DisinfectionRcdDTO disinfectionRcdDTO1 = new DisinfectionRcdDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(disinfectionRcdDTO, disinfectionRcdDTO1);

                    /*humitureDTO.setStatus(1);
                    humitureDTO1.setStatus(2);*/
                        //获取该天正常消毒
                        Integer normalCount = disinfectionRcdMapper.findDisByDateSdId(disinfectionRcdDTO);
                        normal = normal + normalCount;

                        //获取该天异常消毒
                        Integer abnormalCount = disinfectionRcdMapper.findDisByDateSdId(disinfectionRcdDTO1);
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

    @Override
    public ResultBean getPaginationData(DisinfectionRcdDTO disinfectionRcdDTO, Integer page, Integer limit, JSONObject jobj) throws Exception {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!IsEmptyUtils.isEmpty(disinfectionRcdDTO.getTimeQuantum())){
            String[] startEnd = disinfectionRcdDTO.getTimeQuantum().split(" - ");
            if(!IsEmptyUtils.isEmpty(startEnd[0])){
                Date startTime = simpleDateFormat.parse(startEnd[0]+" 00:00:00");
                disinfectionRcdDTO.setStartTime(String.valueOf(startTime.getTime()));
            }
            if(!IsEmptyUtils.isEmpty(startEnd[1])){
                Date endTime = simpleDateFormat.parse(startEnd[0]+" 23:59:59");
                disinfectionRcdDTO.setEndTime(String.valueOf(endTime.getTime()));
            }
        }

        //所有异常消毒
        int abnormal = 0;
        if(!IsEmptyUtils.isEmpty(disinfectionRcdDTO.getSdIds())){
            String[] sdIds = disinfectionRcdDTO.getSdIds().split(",");
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
                    Integer totalStatus = disinfectionRcdMapper.findPaginationDataTotal(disinfectionRcdDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);

                    DisinfectionRcdDTO disinfectionRcdDTO1 = new DisinfectionRcdDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(disinfectionRcdDTO, disinfectionRcdDTO1);
                    disinfectionRcdDTO1.setDisinReason("2");
                    //获取所有消毒异常条数
                    Integer totalAllAbnormal = disinfectionRcdMapper.findPaginationDataTotal(disinfectionRcdDTO1);
                    abnormal = abnormal + totalAllAbnormal;
                }
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 预采购分页查询：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                disinfectionRcdDTO.setStart(startEndNums.get(i).get(0));
                disinfectionRcdDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                disinfectionRcdDTO.setSdId(Integer.parseInt(sourceNames[i]));
                disinfectionRcdDTO.setSiteName(siteData.getName());
                disinfectionRcdDTO.setSitePhoto(siteData.getPhoto());

                //获取分页数据
                List<Map<String,Object>> mainLPList = disinfectionRcdMapper.findPaginationData(disinfectionRcdDTO);
                mainLPLs.addAll(mainLPList);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //如果sdid是空查询该用户下所有站点 for循环取该站点数据
            List<SiteVO> userSites = siteMapper.findUserSites(disinfectionRcdDTO.getUserId());
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
                    //获取采购单
                    Integer totalStatus = disinfectionRcdMapper.findPaginationDataTotal(disinfectionRcdDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);

                    DisinfectionRcdDTO disinfectionRcdDTO1 = new DisinfectionRcdDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(disinfectionRcdDTO, disinfectionRcdDTO1);
                    disinfectionRcdDTO1.setDisinReason("2");
                    //获取所有消毒异常条数
                    Integer totalAllAbnormal = disinfectionRcdMapper.findPaginationDataTotal(disinfectionRcdDTO1);
                    abnormal = abnormal + totalAllAbnormal;
                }
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("预采购分页查询：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                disinfectionRcdDTO.setStart(startEndNums.get(i).get(0));
                disinfectionRcdDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                disinfectionRcdDTO.setSdId(Integer.parseInt(sourceNames[i]));
                disinfectionRcdDTO.setSiteName(siteData.getName());
                disinfectionRcdDTO.setSitePhoto(siteData.getPhoto());

                //获取分页数据
                List<Map<String,Object>> mainLPList = disinfectionRcdMapper.findPaginationData(disinfectionRcdDTO);
                mainLPLs.addAll(mainLPList);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(mainLPLs);
        resultBean.setTotal(total);
        resultBean.setData(abnormal);
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
