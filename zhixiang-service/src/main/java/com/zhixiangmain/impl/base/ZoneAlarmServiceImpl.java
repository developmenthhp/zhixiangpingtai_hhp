package com.zhixiangmain.impl.base;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.base.dto.ZoneAlarmDTO;
import com.zhixiangmain.api.service.base.ZoneAlarmService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.fromwallAlert.FromwallAlertMapper;
import com.zhixiangmain.dao.hjjc.dmjsjb.SlipperyAlertMapper;
import com.zhixiangmain.dao.hjjc.mqkgbjxx.GasSwitchAlertMapper;
import com.zhixiangmain.dao.hjjc.wsbjxx.GasaramrMapper;
import com.zhixiangmain.dao.lechengAptureRecord.LechengAptureRecordMapper;
import com.zhixiangmain.dao.ratplateAlert.RatplateAlertMapper;
import com.zhixiangmain.dao.rlzb.tgjc.PepoleTempMapper;
import com.zhixiangmain.dao.sckj.xmcbjl.MsurementMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 16:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = ZoneAlarmService.class)
public class ZoneAlarmServiceImpl implements ZoneAlarmService {
    private static final Logger logger = LoggerFactory
            .getLogger(ZoneAlarmServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private MsurementMapper msurementMapper;
    @Autowired
    private SlipperyAlertMapper slipperyAlertMapper;
    @Autowired
    private GasaramrMapper gasaramrMapper;
    @Autowired
    private RatplateAlertMapper ratplateAlertMapper;
    @Autowired
    private FromwallAlertMapper fromwallAlertMapper;
    @Autowired
    private GasSwitchAlertMapper gasSwitchAlertMapper;
    @Autowired
    private PepoleTempMapper pepoleTempMapper;
    @Autowired
    private LechengAptureRecordMapper lechengAptureRecordMapper;

    @Override
    public ResultBean getTotalZoneAlarm(ZoneAlarmDTO zoneAlarmDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        Integer countAll = 0;
        if(!IsEmptyUtils.isEmpty(zoneAlarmDTO.getSdIds())){
            String[] sdIds = zoneAlarmDTO.getSdIds().split(",");
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
                    zoneAlarmDTO.setSdId(Integer.parseInt(sdId));
                    zoneAlarmDTO.setSiteName(siteData.getName());
                    zoneAlarmDTO.setSitePhoto(siteData.getPhoto());
                    Integer countMM = msurementMapper.findTotalZoneAlarm(zoneAlarmDTO);
                    Integer countSA = slipperyAlertMapper.findTotalZoneAlarm(zoneAlarmDTO);
                    Integer countGA = gasaramrMapper.findTotalZoneAlarm(zoneAlarmDTO);
                    Integer countRA = ratplateAlertMapper.findTotalZoneAlarm(zoneAlarmDTO);
                    Integer countFA = fromwallAlertMapper.findTotalZoneAlarm(zoneAlarmDTO);
                    Integer countGSA = gasSwitchAlertMapper.findTotalZoneAlarm(zoneAlarmDTO);
                    Integer countPT = pepoleTempMapper.findTotalZoneAlarm(zoneAlarmDTO);
                    Integer countLAR = lechengAptureRecordMapper.findTotalZoneAlarm(zoneAlarmDTO);
                    countAll =countAll+countMM+countSA+countGA+countRA+countFA+countGSA+countPT+countLAR;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(zoneAlarmDTO.getUserId());
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
                        logger.info("切换至--"+dataSourceName+"----数据源"+"获取订单--");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                        //获取采购单
                        zoneAlarmDTO.setSdId(siteVO.getSdId());
                        zoneAlarmDTO.setSiteName(siteData.getName());
                        zoneAlarmDTO.setSitePhoto(siteData.getPhoto());
                        Integer countMM = msurementMapper.findTotalZoneAlarm(zoneAlarmDTO);
                        Integer countSA = slipperyAlertMapper.findTotalZoneAlarm(zoneAlarmDTO);
                        Integer countGA = gasaramrMapper.findTotalZoneAlarm(zoneAlarmDTO);
                        Integer countRA = ratplateAlertMapper.findTotalZoneAlarm(zoneAlarmDTO);
                        Integer countFA = fromwallAlertMapper.findTotalZoneAlarm(zoneAlarmDTO);
                        Integer countGSA = gasSwitchAlertMapper.findTotalZoneAlarm(zoneAlarmDTO);
                        Integer countPT = pepoleTempMapper.findTotalZoneAlarm(zoneAlarmDTO);
                        Integer countLAR = lechengAptureRecordMapper.findTotalZoneAlarm(zoneAlarmDTO);
                        countAll =countAll+countMM+countSA+countGA+countRA+countFA+countGSA+countPT+countLAR;
                    }
                }
            }

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setData(countAll);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
