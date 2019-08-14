package com.zhixiangmain.impl.zhjg.eggl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.jcxxpt.cyrygk.dto.HealthCertificateDTO;
import com.zhixiangmain.api.module.zhjg.eggl.dto.SecondWearDTO;
import com.zhixiangmain.api.module.zhjg.twjcgl.TemperatureDTO;
import com.zhixiangmain.api.service.zhjg.eggl.SecondWearService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.rlzb.nbyg.MainAccountMapper;
import com.zhixiangmain.dao.rlzb.tgjc.PepoleTempMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.zhjg.eggl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-29 16:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = SecondWearService.class)
public class SecondWearServiceImpl implements SecondWearService {
    private static final Logger logger = LoggerFactory
            .getLogger(SecondWearServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private PepoleTempMapper pepoleTempMapper;
    @Autowired
    private MainAccountMapper mainAccountMapper;

    @Override
    public ResultBean getSWearBySdId(SecondWearDTO secondWearDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> temperatureList = Lists.newArrayList();
        List<Map<String,Object>> temperatureequaList = Lists.newArrayList();
        Integer qualifiedTotal = 0;
        Integer unqualifiedTotal = 0;
        Integer accountTotal = 0;
        if(!IsEmptyUtils.isEmpty(secondWearDTO.getSdIds())){
            String[] sdIds = secondWearDTO.getSdIds().split(",");
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
                    TemperatureDTO temperatureDTO = new TemperatureDTO();
                    temperatureDTO.setSomtempStart(secondWearDTO.getSomtempStart());
                    temperatureDTO.setSdId(Integer.parseInt(sdId));
                    //获取采购单
                    secondWearDTO.setSdId(Integer.parseInt(sdId));
                    /*healthCertificateDTO.setSiteName(siteData.getName());
                    healthCertificateDTO.setSitePhoto(siteData.getPhoto());*/
                    //这里如果数据都正常，那么可以只查询健康证表的所有条数，如果精准点就需要从mainaccount innerjoin关联健康表查询条数
                    // 这里先只查询健康表条数，从优化角度考虑
                    //不合格
                    List<Map<String,Object>> temperatureMap  = pepoleTempMapper.findTemperatureBySdId(temperatureDTO);
                    temperatureList = getMonthDataList(temperatureList,temperatureMap);
                    //将今日合格及不合格体温检测数据也在这个接口返回，避免频繁切换数据源
                    //不合格
                    Integer quaTotal = pepoleTempMapper.findQualifiedTotal(temperatureDTO);
                    unqualifiedTotal = unqualifiedTotal + quaTotal;

                    TemperatureDTO temperatureDTO2 = new TemperatureDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    //BeanUtils.copyProperties(temperatureDTO, temperatureDTO2);
                    temperatureDTO2.setSdId(Integer.parseInt(sdId));
                    temperatureDTO2.setSomtempEnd(temperatureDTO.getSomtempStart());
                    //月趋势
                    List<Map<String,Object>> temperaturequaMap  = pepoleTempMapper.findTemperatureBySdId(temperatureDTO2);
                    temperatureequaList = getMonthDataList(temperatureequaList,temperaturequaMap);
                    //合格
                    Integer selUnqTotal = pepoleTempMapper.findQualifiedTotal(temperatureDTO2);
                    qualifiedTotal = qualifiedTotal + selUnqTotal;
                    //已录入的人员
                    HealthCertificateDTO healthCertificateDTO = new HealthCertificateDTO();
                    healthCertificateDTO.setSdId(Integer.parseInt(sdId));
                    Integer selATotal = mainAccountMapper.findAllMainAccountTotalBySdId(healthCertificateDTO);
                    accountTotal = accountTotal + selATotal;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(secondWearDTO.getUserId());
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
                        TemperatureDTO temperatureDTO = new TemperatureDTO();
                        temperatureDTO.setSomtempStart(secondWearDTO.getSomtempStart());
                        temperatureDTO.setSdId(siteVO.getSdId());
                        //获取采购单
                        temperatureDTO.setSdId(siteVO.getSdId());
                        List<Map<String,Object>> temperatureMap  = pepoleTempMapper.findTemperatureBySdId(temperatureDTO);
                        temperatureList = getMonthDataList(temperatureList,temperatureMap);
                        //将今日合格及不合格体温检测数据也在这个接口返回，避免频繁切换数据源
                        //不合格
                        Integer quaTotal = pepoleTempMapper.findQualifiedTotal(temperatureDTO);
                        unqualifiedTotal = unqualifiedTotal + quaTotal;

                        TemperatureDTO temperatureDTO2 = new TemperatureDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        //BeanUtils.copyProperties(temperatureDTO, temperatureDTO2);
                        temperatureDTO2.setSdId(siteVO.getSdId());
                        temperatureDTO2.setSomtempEnd(temperatureDTO.getSomtempStart());
                        //月趋势
                        List<Map<String,Object>> temperaturequaMap  = pepoleTempMapper.findTemperatureBySdId(temperatureDTO2);
                        temperatureequaList = getMonthDataList(temperatureequaList,temperaturequaMap);
                        //合格
                        Integer selUnqTotal = pepoleTempMapper.findQualifiedTotal(temperatureDTO2);
                        qualifiedTotal = qualifiedTotal + selUnqTotal;
                        //已录入的人员
                        HealthCertificateDTO healthCertificateDTO = new HealthCertificateDTO();
                        healthCertificateDTO.setSdId(siteVO.getSdId());
                        Integer selATotal = mainAccountMapper.findAllMainAccountTotalBySdId(healthCertificateDTO);
                        accountTotal = accountTotal + selATotal;
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //当日体温不合格人数
        resultBean.setTotal(unqualifiedTotal);
        //当日体温合格人数
        resultBean.setData(qualifiedTotal);
        //录入的所有人数
        resultBean.setTotalPage(accountTotal);
        //不合格
        resultBean.setRows(temperatureList);
        //合格
        resultBean.setObj(temperatureequaList);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    public List<Map<String,Object>> getMonthDataList(List<Map<String,Object>> allList, List<Map<String,Object>> paramList){
        if(!IsEmptyUtils.isEmpty(allList)){
            for(Map<String,Object> map:paramList){
                int index = -1;
                for(int i=0;i<allList.size();i++){
                    String curDataLcrDate = map.get("createTime").toString();
                    String curDataLcrAllDate = allList.get(i).get("createTime").toString();
                    if(curDataLcrDate.equals(curDataLcrAllDate)){
                        index = i;
                        //相同日期数据相加
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
