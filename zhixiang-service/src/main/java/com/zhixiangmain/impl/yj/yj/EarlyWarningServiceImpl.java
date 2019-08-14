package com.zhixiangmain.impl.yj.yj;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.base.dto.AbnormalSnapshotDTO;
import com.zhixiangmain.api.module.disinfectionRcd.dto.DisinfectionRcdDTO;
import com.zhixiangmain.api.module.fromwallAlert.dto.FromwallAlertDTO;
import com.zhixiangmain.api.module.hjjc.dmjsjb.dto.SlipperyAlertDTO;
import com.zhixiangmain.api.module.hjjc.mqkgbjxx.dto.GasSwitchAlertDTO;
import com.zhixiangmain.api.module.hjjc.wsbjxx.dto.GasaramrDTO;
import com.zhixiangmain.api.module.ratplateAlert.dto.RatplateAlertDTO;
import com.zhixiangmain.api.module.zhjg.twjcgl.TemperatureDTO;
import com.zhixiangmain.api.service.yj.yj.EarlyWarningService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.disinfectionRcd.DisinfectionRcdMapper;
import com.zhixiangmain.dao.fromwallAlert.FromwallAlertMapper;
import com.zhixiangmain.dao.hjjc.dmjsjb.SlipperyAlertMapper;
import com.zhixiangmain.dao.hjjc.mqkgbjxx.GasSwitchAlertMapper;
import com.zhixiangmain.dao.hjjc.wsbjxx.GasaramrMapper;
import com.zhixiangmain.dao.lechengAptureRecord.LechengAptureRecordMapper;
import com.zhixiangmain.dao.lechengCheckRecord.LechengCheckRecordMapper;
import com.zhixiangmain.dao.qxjp.qxwg.CleanRecordMapper;
import com.zhixiangmain.dao.ratplateAlert.RatplateAlertMapper;
import com.zhixiangmain.dao.rlzb.tgjc.PepoleTempMapper;
import com.zhixiangmain.dao.sckj.xmcbjl.MsurementMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.dao.zjgl.zjxdgl.DisinfectionMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.EarlyWarningResult;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.yj.yj
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-11 16:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = EarlyWarningService.class)
public class EarlyWarningServiceImpl implements EarlyWarningService {
    private static final Logger logger = LoggerFactory
            .getLogger(EarlyWarningServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private MsurementMapper msurementMapper;//快检预警
    @Autowired
    private CleanRecordMapper cleanRecordMapper;//清洗异常
    @Autowired
    private DisinfectionRcdMapper disinfectionRcdMapper;//消毒异常
    @Autowired
    private SlipperyAlertMapper slipperyAlertMapper;//地面积水报警
    @Autowired
    private GasaramrMapper gasaramrMapper;//瓦斯报警
    @Autowired
    private RatplateAlertMapper ratplateAlertMapper;//挡鼠板报警
    @Autowired
    private FromwallAlertMapper fromwallAlertMapper;//三离报警
    @Autowired
    private GasSwitchAlertMapper gasSwitchAlertMapper;//煤气开关报警
    @Autowired
    private PepoleTempMapper pepoleTempMapper;//体感报警
    @Autowired
    private LechengCheckRecordMapper lechengCheckRecordMapper;//规范报警
    @Autowired
    private LechengAptureRecordMapper lechengAptureRecordMapper;//不明人员


    @Override
    public EarlyWarningResult getEWMonthBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj) {
        EarlyWarningResult earlyWarningResult = new EarlyWarningResult();
        //快检预警
        List<Map<String,Object>> msurementList = Lists.newArrayList();
        //清洗
        List<Map<String,Object>> cleanRecordList = Lists.newArrayList();
        //消毒异常
        List<Map<String,Object>> disinfectionList = Lists.newArrayList();
        //地面积水
        List<Map<String,Object>> slipperyAlertList = Lists.newArrayList();
        //瓦斯报警
        List<Map<String,Object>> gasaramrList = Lists.newArrayList();
        //挡鼠板
        List<Map<String,Object>> ratplateAlertList = Lists.newArrayList();
        //三离
        List<Map<String,Object>> fromwallAlertList = Lists.newArrayList();
        //煤气开关
        List<Map<String,Object>> gasSwitchAlertList = Lists.newArrayList();
        //体温
        List<Map<String,Object>> pepoleTempList = Lists.newArrayList();
        //规范报警(不戴口罩)
        List<Map<String,Object>> lechengCheckRecordList = Lists.newArrayList();
        //不明人员
        List<Map<String,Object>> lechengAptureRecordList = Lists.newArrayList();
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
                    //快检预警
                    List<Map<String,Object>> msurementMap  = msurementMapper.findMonthBySdId(baseEntityDTO);
                    msurementList = getMonthDataList(msurementList,msurementMap);
                    //清洗报警
                    List<Map<String,Object>> cleanRecordMap  = cleanRecordMapper.findMonthBySdId(baseEntityDTO);
                    cleanRecordList = getMonthDataList(cleanRecordList,cleanRecordMap);

                    DisinfectionRcdDTO disinfectionRcdDTO = new DisinfectionRcdDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, disinfectionRcdDTO);
                    disinfectionRcdDTO.setDisinReason("2");
                    List<Map<String,Object>> surplusFoodMap  = disinfectionRcdMapper.findDisMonthBySdId(disinfectionRcdDTO);
                    disinfectionList = getMonthDataList(disinfectionList,surplusFoodMap);

                    SlipperyAlertDTO slipperyAlertDTO = new SlipperyAlertDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, slipperyAlertDTO);
                    slipperyAlertDTO.setRatplateStatus("1");
                    List<Map<String,Object>> slipperyAlertMap  = slipperyAlertMapper.findMonthBySdId(slipperyAlertDTO);
                    slipperyAlertList = getMonthDataList(slipperyAlertList,slipperyAlertMap);

                    GasaramrDTO gasaramrDTO = new GasaramrDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, gasaramrDTO);
                    gasaramrDTO.setHandleStatus("1");
                    List<Map<String,Object>> gasaramrMap  = gasaramrMapper.findMonthBySdId(gasaramrDTO);
                    gasaramrList = getMonthDataList(gasaramrList,gasaramrMap);

                    RatplateAlertDTO ratplateAlertDTO = new RatplateAlertDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, ratplateAlertDTO);
                    ratplateAlertDTO.setRatplateStatus("1");
                    List<Map<String,Object>> ratplateAlertMap  = ratplateAlertMapper.findMonthBySdId(ratplateAlertDTO);
                    ratplateAlertList = getMonthDataList(ratplateAlertList,ratplateAlertMap);

                    FromwallAlertDTO fromwallAlertDTO = new FromwallAlertDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, fromwallAlertDTO);
                    fromwallAlertDTO.setRatplateStatus("1");
                    List<Map<String,Object>> fromwallAlertMap  = fromwallAlertMapper.findMonthBySdId(fromwallAlertDTO);
                    fromwallAlertList = getMonthDataList(fromwallAlertList,fromwallAlertMap);

                    GasSwitchAlertDTO gasSwitchAlertDTO = new GasSwitchAlertDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, gasSwitchAlertDTO);
                    gasSwitchAlertDTO.setStatus(1);
                    List<Map<String,Object>> gasSwitchAlertMap  = gasSwitchAlertMapper.findMonthBySdId(gasSwitchAlertDTO);
                    gasSwitchAlertList = getMonthDataList(gasSwitchAlertList,gasSwitchAlertMap);

                    TemperatureDTO temperatureDTO = new TemperatureDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, temperatureDTO);
                    temperatureDTO.setSomtempStart(37.2);
                    List<Map<String,Object>> pepoleTempMap  = pepoleTempMapper.findTemperatureBySdId(temperatureDTO);
                    pepoleTempList = getMonthDataList(pepoleTempList,pepoleTempMap);

                    AbnormalSnapshotDTO abnormalSnapshotDTO = new AbnormalSnapshotDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, abnormalSnapshotDTO);
                    List<Map<String,Object>> lechengCheckRecordMap  = lechengCheckRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO);
                    lechengCheckRecordList = getMonthDataList(lechengCheckRecordList,lechengCheckRecordMap);

                    AbnormalSnapshotDTO abnormalSnapshotDTO1 = new AbnormalSnapshotDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, abnormalSnapshotDTO1);
                    List<Map<String,Object>> lechengAptureRecordMap  = lechengAptureRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO1);
                    lechengAptureRecordList = getMonthDataList(lechengAptureRecordList,lechengAptureRecordMap);

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

                        //快检预警
                        List<Map<String,Object>> msurementMap  = msurementMapper.findMonthBySdId(baseEntityDTO);
                        msurementList = getMonthDataList(msurementList,msurementMap);
                        //清洗报警
                        List<Map<String,Object>> cleanRecordMap  = cleanRecordMapper.findMonthBySdId(baseEntityDTO);
                        cleanRecordList = getMonthDataList(cleanRecordList,cleanRecordMap);

                        DisinfectionRcdDTO disinfectionRcdDTO = new DisinfectionRcdDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, disinfectionRcdDTO);
                        disinfectionRcdDTO.setDisinReason("2");
                        List<Map<String,Object>> surplusFoodMap  = disinfectionRcdMapper.findDisMonthBySdId(disinfectionRcdDTO);
                        disinfectionList = getMonthDataList(disinfectionList,surplusFoodMap);

                        SlipperyAlertDTO slipperyAlertDTO = new SlipperyAlertDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, slipperyAlertDTO);
                        slipperyAlertDTO.setRatplateStatus("1");
                        List<Map<String,Object>> slipperyAlertMap  = slipperyAlertMapper.findMonthBySdId(slipperyAlertDTO);
                        slipperyAlertList = getMonthDataList(slipperyAlertList,slipperyAlertMap);

                        GasaramrDTO gasaramrDTO = new GasaramrDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, gasaramrDTO);
                        gasaramrDTO.setHandleStatus("1");
                        List<Map<String,Object>> gasaramrMap  = gasaramrMapper.findMonthBySdId(gasaramrDTO);
                        gasaramrList = getMonthDataList(gasaramrList,gasaramrMap);

                        RatplateAlertDTO ratplateAlertDTO = new RatplateAlertDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, ratplateAlertDTO);
                        ratplateAlertDTO.setRatplateStatus("1");
                        List<Map<String,Object>> ratplateAlertMap  = ratplateAlertMapper.findMonthBySdId(ratplateAlertDTO);
                        ratplateAlertList = getMonthDataList(ratplateAlertList,ratplateAlertMap);

                        FromwallAlertDTO fromwallAlertDTO = new FromwallAlertDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, fromwallAlertDTO);
                        fromwallAlertDTO.setRatplateStatus("1");
                        List<Map<String,Object>> fromwallAlertMap  = fromwallAlertMapper.findMonthBySdId(fromwallAlertDTO);
                        fromwallAlertList = getMonthDataList(fromwallAlertList,fromwallAlertMap);

                        GasSwitchAlertDTO gasSwitchAlertDTO = new GasSwitchAlertDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, gasSwitchAlertDTO);
                        gasSwitchAlertDTO.setStatus(1);
                        List<Map<String,Object>> gasSwitchAlertMap  = gasSwitchAlertMapper.findMonthBySdId(gasSwitchAlertDTO);
                        gasSwitchAlertList = getMonthDataList(gasSwitchAlertList,gasSwitchAlertMap);

                        TemperatureDTO temperatureDTO = new TemperatureDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, temperatureDTO);
                        temperatureDTO.setSomtempStart(37.2);
                        List<Map<String,Object>> pepoleTempMap  = pepoleTempMapper.findTemperatureBySdId(temperatureDTO);
                        pepoleTempList = getMonthDataList(pepoleTempList,pepoleTempMap);

                        AbnormalSnapshotDTO abnormalSnapshotDTO = new AbnormalSnapshotDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, abnormalSnapshotDTO);
                        List<Map<String,Object>> lechengCheckRecordMap  = lechengCheckRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO);
                        lechengCheckRecordList = getMonthDataList(lechengCheckRecordList,lechengCheckRecordMap);

                        AbnormalSnapshotDTO abnormalSnapshotDTO1 = new AbnormalSnapshotDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, abnormalSnapshotDTO1);
                        List<Map<String,Object>> lechengAptureRecordMap  = lechengAptureRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO1);
                        lechengAptureRecordList = getMonthDataList(lechengAptureRecordList,lechengAptureRecordMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //清洗
        earlyWarningResult.setCleanRecordList(cleanRecordList);
        //消毒
        earlyWarningResult.setDisinfectionList(disinfectionList);
        //三离
        earlyWarningResult.setFromwallAlertList(fromwallAlertList);
        //瓦斯
        earlyWarningResult.setGasaramrList(gasaramrList);
        //煤气开关
        earlyWarningResult.setGasSwitchAlertList(gasSwitchAlertList);
        //不明人员
        earlyWarningResult.setLechengAptureRecordList(lechengAptureRecordList);
        //不戴口罩
        earlyWarningResult.setLechengCheckRecordList(lechengCheckRecordList);
        //快检
        earlyWarningResult.setMsurementList(msurementList);
        //体感
        earlyWarningResult.setPepoleTempList(pepoleTempList);
        //挡鼠板
        earlyWarningResult.setRatplateAlertList(ratplateAlertList);
        //地面积水
        earlyWarningResult.setSlipperyAlertList(slipperyAlertList);
        earlyWarningResult.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return earlyWarningResult;
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
