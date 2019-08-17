package com.zhixiangmain.impl.alertSummary;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.alert.Alert;
import com.zhixiangmain.api.module.aramhealth.Aramhealth;
import com.zhixiangmain.api.module.cleanRecord.CleanRecord;
import com.zhixiangmain.api.module.disinfectionRcd.DisinfectionRcd;
import com.zhixiangmain.api.module.fromwallAlert.FromwallAlert;
import com.zhixiangmain.api.module.hjjc.dmjsjb.SlipperyAlert;
import com.zhixiangmain.api.module.inventoryAlert.InventoryAlert;
import com.zhixiangmain.api.module.lechengAptureRecord.LechengAptureRecord;
import com.zhixiangmain.api.module.lechengCheckRecord.LechengCheckRecord;
import com.zhixiangmain.api.module.ratplateAlert.RatplateAlert;
import com.zhixiangmain.api.service.alertSummary.AlertSummaryService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.aramhealth.AramhealthMapper;
import com.zhixiangmain.dao.cleanJl.CleanJlMapper;
import com.zhixiangmain.dao.disinfectionRcd.DisinfectionRcdMapper;
import com.zhixiangmain.dao.fromwallAlert.FromwallAlertMapper;
import com.zhixiangmain.dao.hjjc.dmjsjb.SlipperyAlertMapper;
import com.zhixiangmain.dao.inventoryAlert.InventoryAlertMapper;
import com.zhixiangmain.dao.lechengAptureRecord.LechengAptureRecordMapper;
import com.zhixiangmain.dao.lechengCheckRecord.LechengCheckRecordMapper;
import com.zhixiangmain.dao.ratplateAlert.RatplateAlertMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.date.DateUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Tilsls
 * tle: zhixiangyun-charge
 * @Package com.zhixiangyun.impl.alertSummary
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 15:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = AlertSummaryService.class)
public class AlertSummaryServiceImpl implements AlertSummaryService/*,Callable<List>*/{
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(AlertSummaryServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private LechengAptureRecordMapper lechengAptureRecordMapper;
    @Autowired
    private LechengCheckRecordMapper lechengCheckRecordMapper;
    @Autowired
    private DisinfectionRcdMapper disinfectionRcdMapper;
    @Autowired
    private CleanJlMapper cleanJlMapper;
    @Autowired
    private RatplateAlertMapper ratplateAlertMapper;
    @Autowired
    private FromwallAlertMapper fromwallAlertMapper;
    @Autowired
    private InventoryAlertMapper inventoryAlertMapper;
    @Autowired
    private AramhealthMapper aramhealthMapper;
    @Autowired
    private SlipperyAlertMapper slipperyAlertMapper;

    private String type;

    @Override
    public ResultBean getFirstAlert(int userId, String sdIds, String type,JSONObject jobj) {

        ResultBean resultBean = new ResultBean();
        List<Alert> list= Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(sdIds)){
            boolean mainQueryFlag = false;
            String[] sdIdArray = sdIds.split(",");
            for(String sdId:sdIdArray){
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                logger.info("切换至"+dataSourceName+"数据源");
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取区域抓拍告警1条最新的
                    LechengAptureRecord lechengAptureRecord2=lechengAptureRecordMapper.findTopOne(0, "2");
                    if(!IsEmptyUtils.isEmpty(lechengAptureRecord2)&&"android".equals(type)){
                        //组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(lechengAptureRecord2.getId());
                        apture_ert.setTime(lechengAptureRecord2.getCreateTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(lechengAptureRecord2.getAreaName()+":有不明人员进入,按规范此区域禁止外人进入请及时处理.."+"区域抓拍告警");
                        apture_ert.setImgUrl(lechengAptureRecord2.getImgUrl());
                        apture_ert.setCategory("1");
                        apture_ert.setUsername("区域抓怕");
                        apture_ert.setSdId(lechengAptureRecord2.getSdId());
                        list.add(apture_ert);
                    }

                    //获取规范抓拍 1条
                    LechengCheckRecord lechengCheckRecord=lechengCheckRecordMapper.findTopOne(0, "2");
                    if(!IsEmptyUtils.isEmpty(lechengCheckRecord)&&"android".equals(type)){
                        //组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(lechengCheckRecord.getId());
                        apture_ert.setImgUrl(lechengCheckRecord.getImgUrl());
                        apture_ert.setTime(lechengCheckRecord.getCreateTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(lechengCheckRecord.getAreaName()+":检测扫描到有职员未正确佩戴口罩,违反相关规范请予以处理.."+"规范抓拍 ");
                        apture_ert.setCategory("2");
                        apture_ert.setUsername(lechengCheckRecord.getViolationName());
                        apture_ert.setSdId(lechengCheckRecord.getSdId());
                        list.add(apture_ert);
                    }

                    //获取消毒操作规范预警 1条最新
                    DisinfectionRcd disinfectionRcd=disinfectionRcdMapper.findToAll(0);
                    if(!IsEmptyUtils.isEmpty(disinfectionRcd)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(disinfectionRcd.getId());
                        apture_ert.setImgUrl(disinfectionRcd.getDisinPersonImg());
                        apture_ert.setTime(DateUtils.getFromattime(Long.parseLong(disinfectionRcd.getDisinStartTime())));
                        apture_ert.setContext("物品消毒操作违规,未按正常消毒消毒规定执行,规定温度消毒时间不达标 操作人:"+disinfectionRcd.getDisinPerson()+",请予以处理.."+"消毒操作规范");
                        apture_ert.setType("1");
                        apture_ert.setCategory("3");
                        apture_ert.setUsername(disinfectionRcd.getDisinPerson());
                        apture_ert.setSdId(disinfectionRcd.getSdId());
                        list.add(apture_ert);
                    }

                    //获取清洗浸泡操作规范 1条最新
                    CleanRecord cleanRecord=cleanJlMapper.findAlertOneCleanJl(0);
                    if(!IsEmptyUtils.isEmpty(cleanRecord)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(cleanRecord.getId());
                        apture_ert.setImgUrl(cleanRecord.getImgs());
                        apture_ert.setTime(cleanRecord.getStartTime());
                        apture_ert.setType("1");
                        apture_ert.setContext("物品清洗浸泡操作违规,未按正常清洗浸泡规定执行,规定时间不达标 操作人:"+cleanRecord.getCzrName()+",请予以处理.."+"清洗浸泡操作规范");
                        apture_ert.setCategory("4");
                        apture_ert.setUsername(cleanRecord.getCzrName());
                        apture_ert.setSdId(cleanRecord.getSdId());
                        list.add(apture_ert);
                    }

                    //获取挡鼠板 异常记录
                    RatplateAlert ratplateAlert=ratplateAlertMapper.findAll(0);
                    if(!IsEmptyUtils.isEmpty(ratplateAlert)){
                        //组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(ratplateAlert.getId());
                        apture_ert.setTime(ratplateAlert.getRatplateStartTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(ratplateAlert.getRatplateArea()+":"+ratplateAlert.getRatplateDescription()+"挡鼠板 异");
                        apture_ert.setCategory("5");
                        apture_ert.setUsername("仓库管理员");
                        apture_ert.setSdId(ratplateAlert.getSdId());
                        list.add(apture_ert);
                    }

                    //获取墙距异常记录
                    FromwallAlert fromwallAlert=fromwallAlertMapper.findAll(0);
                    if(!IsEmptyUtils.isEmpty(fromwallAlert)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(fromwallAlert.getId());
                        apture_ert.setTime(fromwallAlert.getRatplateStartTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(fromwallAlert.getFromwallArea()+":"+fromwallAlert.getRatplateDescription()+"墙距异常");
                        apture_ert.setUsername("仓库管理员");
                        apture_ert.setCategory("6");
                        apture_ert.setSdId(fromwallAlert.getSdId());
                        list.add(apture_ert);
                    }

                    //获取仓库货品即将过期橙色警告
                    InventoryAlert inventoryAlert=inventoryAlertMapper.findToFcznCon(0);
                    if(!IsEmptyUtils.isEmpty(inventoryAlert)){//组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(inventoryAlert.getId());
                        apture_ert.setTime(inventoryAlert.getOccurTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(inventoryAlert.getExpconText()+"仓库货品即将过期");
                        apture_ert.setCategory("7");
                        apture_ert.setUsername(inventoryAlert.getUnderTaker());
                        apture_ert.setSdId(inventoryAlert.getSdId());
                        list.add(apture_ert);
                    }

                    // new供应源
                    Aramhealth aramhealthg=new Aramhealth();
                    aramhealthg.setSdId(0);
                    aramhealthg.setAramType("2");
                    aramhealthg.setAramHandle("2");
                    Aramhealth aramhealth=aramhealthMapper.findAll(aramhealthg);
                    if(!IsEmptyUtils.isEmpty(aramhealth)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(aramhealth.getId());
                        apture_ert.setTime(aramhealth.getAramTime());
                        apture_ert.setContext(aramhealth.getAramconText()+"ew供应源");
                        apture_ert.setType("1");
                        apture_ert.setCategory("9");
                        apture_ert.setUsername("供应商家管理员");
                        apture_ert.setSdId(aramhealth.getSdId());
                        list.add(apture_ert);
                    }

                    //new人员健康
                    Aramhealth aramhealth3=new Aramhealth();
                    aramhealth3.setSdId(0);
                    aramhealth3.setAramType("1");
                    aramhealth3.setAramHandle("2");
                    Aramhealth aramhealthList=aramhealthMapper.findAll(aramhealth3);
                    if(!IsEmptyUtils.isEmpty(aramhealthList)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(aramhealthList.getId());
                        apture_ert.setTime(aramhealthList.getAramTime());
                        apture_ert.setContext(aramhealthList.getAramconText()+"new人员健康");
                        apture_ert.setType("1");
                        apture_ert.setCategory("10");
                        apture_ert.setUsername("作业员工");
                        apture_ert.setSdId(aramhealthList.getSdId());
                        list.add(apture_ert);
                    }

                    SlipperyAlert slipperyAlert=slipperyAlertMapper.findAll(0);
                    if(!IsEmptyUtils.isEmpty(slipperyAlert)){
                        //组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(slipperyAlert.getId());
                        apture_ert.setTime(slipperyAlert.getAlertTime());
                        apture_ert.setContext(slipperyAlert.getRatplateDescription()+"slipperyAlert");
                        apture_ert.setType("1");
                        apture_ert.setCategory("11");
                        apture_ert.setUsername("管理经理");
                        apture_ert.setSdId(slipperyAlert.getSdId());
                        list.add(apture_ert);
                    }

                }else{
                    if(!mainQueryFlag){
                        //获取区域抓拍告警1条最新的
                        LechengAptureRecord lechengAptureRecord2=lechengAptureRecordMapper.findTopOne(0, "2");
                        if(!IsEmptyUtils.isEmpty(lechengAptureRecord2)&&"android".equals(type)){
                            //组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(lechengAptureRecord2.getId());
                            apture_ert.setTime(lechengAptureRecord2.getCreateTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(lechengAptureRecord2.getAreaName()+":有不明人员进入,按规范此区域禁止外人进入请及时处理.."+"区域抓拍告警");
                            apture_ert.setImgUrl(lechengAptureRecord2.getImgUrl());
                            apture_ert.setCategory("1");
                            apture_ert.setUsername("区域抓怕");
                            apture_ert.setSdId(lechengAptureRecord2.getSdId());
                            list.add(apture_ert);
                        }

                        //获取规范抓拍 1条
                        LechengCheckRecord lechengCheckRecord=lechengCheckRecordMapper.findTopOne(0, "2");
                        if(!IsEmptyUtils.isEmpty(lechengCheckRecord)&&"android".equals(type)){
                            //组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(lechengCheckRecord.getId());
                            apture_ert.setImgUrl(lechengCheckRecord.getImgUrl());
                            apture_ert.setTime(lechengCheckRecord.getCreateTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(lechengCheckRecord.getAreaName()+":检测扫描到有职员未正确佩戴口罩,违反相关规范请予以处理.."+"规范抓拍 ");
                            apture_ert.setCategory("2");
                            apture_ert.setUsername(lechengCheckRecord.getViolationName());
                            apture_ert.setSdId(lechengCheckRecord.getSdId());
                            list.add(apture_ert);
                        }

                        //获取消毒操作规范预警 1条最新
                        DisinfectionRcd disinfectionRcd=disinfectionRcdMapper.findToAll(0);
                        if(!IsEmptyUtils.isEmpty(disinfectionRcd)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(disinfectionRcd.getId());
                            apture_ert.setImgUrl(disinfectionRcd.getDisinPersonImg());
                            apture_ert.setTime(DateUtils.getFromattime(Long.parseLong(disinfectionRcd.getDisinStartTime())));
                            apture_ert.setContext("物品消毒操作违规,未按正常消毒消毒规定执行,规定温度消毒时间不达标 操作人:"+disinfectionRcd.getDisinPerson()+",请予以处理.."+"消毒操作规范");
                            apture_ert.setType("1");
                            apture_ert.setCategory("3");
                            apture_ert.setUsername(disinfectionRcd.getDisinPerson());
                            apture_ert.setSdId(disinfectionRcd.getSdId());
                            list.add(apture_ert);
                        }

                        //获取清洗浸泡操作规范 1条最新
                        CleanRecord cleanRecord=cleanJlMapper.findAlertOneCleanJl(0);
                        if(!IsEmptyUtils.isEmpty(cleanRecord)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(cleanRecord.getId());
                            apture_ert.setImgUrl(cleanRecord.getImgs());
                            apture_ert.setTime(cleanRecord.getStartTime());
                            apture_ert.setType("1");
                            apture_ert.setContext("物品清洗浸泡操作违规,未按正常清洗浸泡规定执行,规定时间不达标 操作人:"+cleanRecord.getCzrName()+",请予以处理.."+"清洗浸泡操作规范");
                            apture_ert.setCategory("4");
                            apture_ert.setUsername(cleanRecord.getCzrName());
                            apture_ert.setSdId(cleanRecord.getSdId());
                            list.add(apture_ert);
                        }

                        //获取挡鼠板 异常记录
                        RatplateAlert ratplateAlert=ratplateAlertMapper.findAll(0);
                        if(!IsEmptyUtils.isEmpty(ratplateAlert)){
                            //组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(ratplateAlert.getId());
                            apture_ert.setTime(ratplateAlert.getRatplateStartTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(ratplateAlert.getRatplateArea()+":"+ratplateAlert.getRatplateDescription()+"挡鼠板 异");
                            apture_ert.setCategory("5");
                            apture_ert.setUsername("仓库管理员");
                            apture_ert.setSdId(ratplateAlert.getSdId());
                            list.add(apture_ert);
                        }

                        //获取墙距异常记录
                        FromwallAlert fromwallAlert=fromwallAlertMapper.findAll(0);
                        if(!IsEmptyUtils.isEmpty(fromwallAlert)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(fromwallAlert.getId());
                            apture_ert.setTime(fromwallAlert.getRatplateStartTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(fromwallAlert.getFromwallArea()+":"+fromwallAlert.getRatplateDescription()+"墙距异常");
                            apture_ert.setUsername("仓库管理员");
                            apture_ert.setCategory("6");
                            apture_ert.setSdId(fromwallAlert.getSdId());
                            list.add(apture_ert);
                        }

                        //获取仓库货品即将过期橙色警告
                        InventoryAlert inventoryAlert=inventoryAlertMapper.findToFcznCon(0);
                        if(!IsEmptyUtils.isEmpty(inventoryAlert)){//组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(inventoryAlert.getId());
                            apture_ert.setTime(inventoryAlert.getOccurTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(inventoryAlert.getExpconText()+"仓库货品即将过期");
                            apture_ert.setCategory("7");
                            apture_ert.setUsername(inventoryAlert.getUnderTaker());
                            apture_ert.setSdId(inventoryAlert.getSdId());
                            list.add(apture_ert);
                        }

                        // new供应源
                        Aramhealth aramhealthg=new Aramhealth();
                        aramhealthg.setSdId(0);
                        aramhealthg.setAramType("2");
                        aramhealthg.setAramHandle("2");
                        Aramhealth aramhealth=aramhealthMapper.findAll(aramhealthg);
                        if(!IsEmptyUtils.isEmpty(aramhealth)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(aramhealth.getId());
                            apture_ert.setTime(aramhealth.getAramTime());
                            apture_ert.setContext(aramhealth.getAramconText()+"ew供应源");
                            apture_ert.setType("1");
                            apture_ert.setCategory("9");
                            apture_ert.setUsername("供应商家管理员");
                            apture_ert.setSdId(aramhealth.getSdId());
                            list.add(apture_ert);
                        }

                        //new人员健康
                        Aramhealth aramhealth3=new Aramhealth();
                        aramhealth3.setSdId(0);
                        aramhealth3.setAramType("1");
                        aramhealth3.setAramHandle("2");
                        Aramhealth aramhealthList=aramhealthMapper.findAll(aramhealth3);
                        if(!IsEmptyUtils.isEmpty(aramhealthList)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(aramhealthList.getId());
                            apture_ert.setTime(aramhealthList.getAramTime());
                            apture_ert.setContext(aramhealthList.getAramconText()+"new人员健康");
                            apture_ert.setType("1");
                            apture_ert.setCategory("10");
                            apture_ert.setUsername("作业员工");
                            apture_ert.setSdId(aramhealthList.getSdId());
                            list.add(apture_ert);
                        }

                        SlipperyAlert slipperyAlert=slipperyAlertMapper.findAll(0);
                        if(!IsEmptyUtils.isEmpty(slipperyAlert)){
                            //组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(slipperyAlert.getId());
                            apture_ert.setTime(slipperyAlert.getAlertTime());
                            apture_ert.setContext(slipperyAlert.getRatplateDescription()+"slipperyAlert");
                            apture_ert.setType("1");
                            apture_ert.setCategory("11");
                            apture_ert.setUsername("管理经理");
                            apture_ert.setSdId(slipperyAlert.getSdId());
                            list.add(apture_ert);
                        }
                    }
                }
                mainQueryFlag = true;
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //如果sdid是空查询该用户下所有站点 for循环取该站点数据
            List<SiteVO> userSites = siteMapper.findUserSites(userId);
            //给为空查询主数据源一个标记，为空第一次查询完就不查询
            boolean mainQueryFlag = false;
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
                    logger.info("切换至"+dataSourceName+"数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取区域抓拍告警1条最新的
                    LechengAptureRecord lechengAptureRecord2=lechengAptureRecordMapper.findTopOne(0, "2");
                    if(!IsEmptyUtils.isEmpty(lechengAptureRecord2)&&"android".equals(type)){
                        //组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(lechengAptureRecord2.getId());
                        apture_ert.setTime(lechengAptureRecord2.getCreateTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(lechengAptureRecord2.getAreaName()+":有不明人员进入,按规范此区域禁止外人进入请及时处理.."+"区域抓拍告警");
                        apture_ert.setImgUrl(lechengAptureRecord2.getImgUrl());
                        apture_ert.setCategory("1");
                        apture_ert.setUsername("区域抓怕");
                        apture_ert.setSdId(lechengAptureRecord2.getSdId());
                        list.add(apture_ert);
                    }

                    //获取规范抓拍 1条
                    LechengCheckRecord lechengCheckRecord=lechengCheckRecordMapper.findTopOne(0, "2");
                    if(!IsEmptyUtils.isEmpty(lechengCheckRecord)&&"android".equals(type)){
                        //组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(lechengCheckRecord.getId());
                        apture_ert.setImgUrl(lechengCheckRecord.getImgUrl());
                        apture_ert.setTime(lechengCheckRecord.getCreateTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(lechengCheckRecord.getAreaName()+":检测扫描到有职员未正确佩戴口罩,违反相关规范请予以处理.."+"规范抓拍 ");
                        apture_ert.setCategory("2");
                        apture_ert.setUsername(lechengCheckRecord.getViolationName());
                        apture_ert.setSdId(lechengCheckRecord.getSdId());
                        list.add(apture_ert);
                    }

                    //获取消毒操作规范预警 1条最新
                    DisinfectionRcd disinfectionRcd=disinfectionRcdMapper.findToAll(0);
                    if(!IsEmptyUtils.isEmpty(disinfectionRcd)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(disinfectionRcd.getId());
                        apture_ert.setImgUrl(disinfectionRcd.getDisinPersonImg());
                        apture_ert.setTime(DateUtils.getFromattime(Long.parseLong(disinfectionRcd.getDisinStartTime())));
                        apture_ert.setContext("物品消毒操作违规,未按正常消毒消毒规定执行,规定温度消毒时间不达标 操作人:"+disinfectionRcd.getDisinPerson()+",请予以处理.."+"消毒操作规范");
                        apture_ert.setType("1");
                        apture_ert.setCategory("3");
                        apture_ert.setUsername(disinfectionRcd.getDisinPerson());
                        apture_ert.setSdId(disinfectionRcd.getSdId());
                        list.add(apture_ert);
                    }

                    //获取清洗浸泡操作规范 1条最新
                    CleanRecord cleanRecord=cleanJlMapper.findAlertOneCleanJl(0);
                    if(!IsEmptyUtils.isEmpty(cleanRecord)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(cleanRecord.getId());
                        apture_ert.setImgUrl(cleanRecord.getImgs());
                        apture_ert.setTime(cleanRecord.getStartTime());
                        apture_ert.setType("1");
                        apture_ert.setContext("物品清洗浸泡操作违规,未按正常清洗浸泡规定执行,规定时间不达标 操作人:"+cleanRecord.getCzrName()+",请予以处理.."+"清洗浸泡操作规范");
                        apture_ert.setCategory("4");
                        apture_ert.setUsername(cleanRecord.getCzrName());
                        apture_ert.setSdId(cleanRecord.getSdId());
                        list.add(apture_ert);
                    }

                    //获取挡鼠板 异常记录
                    RatplateAlert ratplateAlert=ratplateAlertMapper.findAll(0);
                    if(!IsEmptyUtils.isEmpty(ratplateAlert)){
                        //组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(ratplateAlert.getId());
                        apture_ert.setTime(ratplateAlert.getRatplateStartTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(ratplateAlert.getRatplateArea()+":"+ratplateAlert.getRatplateDescription()+"挡鼠板 异");
                        apture_ert.setCategory("5");
                        apture_ert.setUsername("仓库管理员");
                        apture_ert.setSdId(ratplateAlert.getSdId());
                        list.add(apture_ert);
                    }

                    //获取墙距异常记录
                    FromwallAlert fromwallAlert=fromwallAlertMapper.findAll(0);
                    if(!IsEmptyUtils.isEmpty(fromwallAlert)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(fromwallAlert.getId());
                        apture_ert.setTime(fromwallAlert.getRatplateStartTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(fromwallAlert.getFromwallArea()+":"+fromwallAlert.getRatplateDescription()+"墙距异常");
                        apture_ert.setUsername("仓库管理员");
                        apture_ert.setCategory("6");
                        apture_ert.setSdId(fromwallAlert.getSdId());
                        list.add(apture_ert);
                    }

                    //获取仓库货品即将过期橙色警告
                    InventoryAlert inventoryAlert=inventoryAlertMapper.findToFcznCon(0);
                    if(!IsEmptyUtils.isEmpty(inventoryAlert)){//组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(inventoryAlert.getId());
                        apture_ert.setTime(inventoryAlert.getOccurTime());
                        apture_ert.setType("1");
                        apture_ert.setContext(inventoryAlert.getExpconText()+"仓库货品即将过期");
                        apture_ert.setCategory("7");
                        apture_ert.setUsername(inventoryAlert.getUnderTaker());
                        apture_ert.setSdId(inventoryAlert.getSdId());
                        list.add(apture_ert);
                    }

                    // new供应源
                    Aramhealth aramhealthg=new Aramhealth();
                    aramhealthg.setSdId(0);
                    aramhealthg.setAramType("2");
                    aramhealthg.setAramHandle("2");
                    Aramhealth aramhealth=aramhealthMapper.findAll(aramhealthg);
                    if(!IsEmptyUtils.isEmpty(aramhealth)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(aramhealth.getId());
                        apture_ert.setTime(aramhealth.getAramTime());
                        apture_ert.setContext(aramhealth.getAramconText()+"ew供应源");
                        apture_ert.setType("1");
                        apture_ert.setCategory("9");
                        apture_ert.setUsername("供应商家管理员");
                        apture_ert.setSdId(aramhealth.getSdId());
                        list.add(apture_ert);
                    }

                    //new人员健康
                    Aramhealth aramhealth3=new Aramhealth();
                    aramhealth3.setSdId(0);
                    aramhealth3.setAramType("1");
                    aramhealth3.setAramHandle("2");
                    Aramhealth aramhealthList=aramhealthMapper.findAll(aramhealth3);
                    if(!IsEmptyUtils.isEmpty(aramhealthList)){
                        Alert apture_ert=new Alert();
                        apture_ert.setId(aramhealthList.getId());
                        apture_ert.setTime(aramhealthList.getAramTime());
                        apture_ert.setContext(aramhealthList.getAramconText()+"new人员健康");
                        apture_ert.setType("1");
                        apture_ert.setCategory("10");
                        apture_ert.setUsername("作业员工");
                        apture_ert.setSdId(aramhealthList.getSdId());
                        list.add(apture_ert);
                    }

                    SlipperyAlert slipperyAlert=slipperyAlertMapper.findAll(0);
                    if(!IsEmptyUtils.isEmpty(slipperyAlert)){
                        //组装统一格式
                        Alert apture_ert=new Alert();
                        apture_ert.setId(slipperyAlert.getId());
                        apture_ert.setTime(slipperyAlert.getAlertTime());
                        apture_ert.setContext(slipperyAlert.getRatplateDescription()+"slipperyAlert");
                        apture_ert.setType("1");
                        apture_ert.setCategory("11");
                        apture_ert.setUsername("管理经理");
                        apture_ert.setSdId(slipperyAlert.getSdId());
                        list.add(apture_ert);
                    }

                }else{
                    if(!mainQueryFlag){
                        //获取区域抓拍告警1条最新的
                        LechengAptureRecord lechengAptureRecord2=lechengAptureRecordMapper.findTopOne(0, "2");
                        if(!IsEmptyUtils.isEmpty(lechengAptureRecord2)&&"android".equals(type)){
                            //组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(lechengAptureRecord2.getId());
                            apture_ert.setTime(lechengAptureRecord2.getCreateTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(lechengAptureRecord2.getAreaName()+":有不明人员进入,按规范此区域禁止外人进入请及时处理.."+"区域抓拍告警");
                            apture_ert.setImgUrl(lechengAptureRecord2.getImgUrl());
                            apture_ert.setCategory("1");
                            apture_ert.setUsername("区域抓怕");
                            apture_ert.setSdId(lechengAptureRecord2.getSdId());
                            list.add(apture_ert);
                        }

                        //获取规范抓拍 1条
                        LechengCheckRecord lechengCheckRecord=lechengCheckRecordMapper.findTopOne(0, "2");
                        if(!IsEmptyUtils.isEmpty(lechengCheckRecord)&&"android".equals(type)){
                            //组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(lechengCheckRecord.getId());
                            apture_ert.setImgUrl(lechengCheckRecord.getImgUrl());
                            apture_ert.setTime(lechengCheckRecord.getCreateTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(lechengCheckRecord.getAreaName()+":检测扫描到有职员未正确佩戴口罩,违反相关规范请予以处理.."+"规范抓拍 ");
                            apture_ert.setCategory("2");
                            apture_ert.setUsername(lechengCheckRecord.getViolationName());
                            apture_ert.setSdId(lechengCheckRecord.getSdId());
                            list.add(apture_ert);
                        }

                        //获取消毒操作规范预警 1条最新
                        DisinfectionRcd disinfectionRcd=disinfectionRcdMapper.findToAll(0);
                        if(!IsEmptyUtils.isEmpty(disinfectionRcd)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(disinfectionRcd.getId());
                            apture_ert.setImgUrl(disinfectionRcd.getDisinPersonImg());
                            apture_ert.setTime(DateUtils.getFromattime(Long.parseLong(disinfectionRcd.getDisinStartTime())));
                            apture_ert.setContext("物品消毒操作违规,未按正常消毒消毒规定执行,规定温度消毒时间不达标 操作人:"+disinfectionRcd.getDisinPerson()+",请予以处理.."+"消毒操作规范");
                            apture_ert.setType("1");
                            apture_ert.setCategory("3");
                            apture_ert.setUsername(disinfectionRcd.getDisinPerson());
                            apture_ert.setSdId(disinfectionRcd.getSdId());
                            list.add(apture_ert);
                        }

                        //获取清洗浸泡操作规范 1条最新
                        CleanRecord cleanRecord=cleanJlMapper.findAlertOneCleanJl(0);
                        if(!IsEmptyUtils.isEmpty(cleanRecord)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(cleanRecord.getId());
                            apture_ert.setImgUrl(cleanRecord.getImgs());
                            apture_ert.setTime(cleanRecord.getStartTime());
                            apture_ert.setType("1");
                            apture_ert.setContext("物品清洗浸泡操作违规,未按正常清洗浸泡规定执行,规定时间不达标 操作人:"+cleanRecord.getCzrName()+",请予以处理.."+"清洗浸泡操作规范");
                            apture_ert.setCategory("4");
                            apture_ert.setUsername(cleanRecord.getCzrName());
                            apture_ert.setSdId(cleanRecord.getSdId());
                            list.add(apture_ert);
                        }

                        //获取挡鼠板 异常记录
                        RatplateAlert ratplateAlert=ratplateAlertMapper.findAll(0);
                        if(!IsEmptyUtils.isEmpty(ratplateAlert)){
                            //组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(ratplateAlert.getId());
                            apture_ert.setTime(ratplateAlert.getRatplateStartTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(ratplateAlert.getRatplateArea()+":"+ratplateAlert.getRatplateDescription()+"挡鼠板 异");
                            apture_ert.setCategory("5");
                            apture_ert.setUsername("仓库管理员");
                            apture_ert.setSdId(ratplateAlert.getSdId());
                            list.add(apture_ert);
                        }

                        //获取墙距异常记录
                        FromwallAlert fromwallAlert=fromwallAlertMapper.findAll(0);
                        if(!IsEmptyUtils.isEmpty(fromwallAlert)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(fromwallAlert.getId());
                            apture_ert.setTime(fromwallAlert.getRatplateStartTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(fromwallAlert.getFromwallArea()+":"+fromwallAlert.getRatplateDescription()+"墙距异常");
                            apture_ert.setUsername("仓库管理员");
                            apture_ert.setCategory("6");
                            apture_ert.setSdId(fromwallAlert.getSdId());
                            list.add(apture_ert);
                        }

                        //获取仓库货品即将过期橙色警告
                        InventoryAlert inventoryAlert=inventoryAlertMapper.findToFcznCon(0);
                        if(!IsEmptyUtils.isEmpty(inventoryAlert)){//组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(inventoryAlert.getId());
                            apture_ert.setTime(inventoryAlert.getOccurTime());
                            apture_ert.setType("1");
                            apture_ert.setContext(inventoryAlert.getExpconText()+"仓库货品即将过期");
                            apture_ert.setCategory("7");
                            apture_ert.setUsername(inventoryAlert.getUnderTaker());
                            apture_ert.setSdId(inventoryAlert.getSdId());
                            list.add(apture_ert);
                        }

                        // new供应源
                        Aramhealth aramhealthg=new Aramhealth();
                        aramhealthg.setSdId(0);
                        aramhealthg.setAramType("2");
                        aramhealthg.setAramHandle("2");
                        Aramhealth aramhealth=aramhealthMapper.findAll(aramhealthg);
                        if(!IsEmptyUtils.isEmpty(aramhealth)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(aramhealth.getId());
                            apture_ert.setTime(aramhealth.getAramTime());
                            apture_ert.setContext(aramhealth.getAramconText()+"ew供应源");
                            apture_ert.setType("1");
                            apture_ert.setCategory("9");
                            apture_ert.setUsername("供应商家管理员");
                            apture_ert.setSdId(aramhealth.getSdId());
                            list.add(apture_ert);
                        }

                        //new人员健康
                        Aramhealth aramhealth3=new Aramhealth();
                        aramhealth3.setSdId(0);
                        aramhealth3.setAramType("1");
                        aramhealth3.setAramHandle("2");
                        Aramhealth aramhealthList=aramhealthMapper.findAll(aramhealth3);
                        if(!IsEmptyUtils.isEmpty(aramhealthList)){
                            Alert apture_ert=new Alert();
                            apture_ert.setId(aramhealthList.getId());
                            apture_ert.setTime(aramhealthList.getAramTime());
                            apture_ert.setContext(aramhealthList.getAramconText()+"new人员健康");
                            apture_ert.setType("1");
                            apture_ert.setCategory("10");
                            apture_ert.setUsername("作业员工");
                            apture_ert.setSdId(aramhealthList.getSdId());
                            list.add(apture_ert);
                        }

                        SlipperyAlert slipperyAlert=slipperyAlertMapper.findAll(0);
                        if(!IsEmptyUtils.isEmpty(slipperyAlert)){
                            //组装统一格式
                            Alert apture_ert=new Alert();
                            apture_ert.setId(slipperyAlert.getId());
                            apture_ert.setTime(slipperyAlert.getAlertTime());
                            apture_ert.setContext(slipperyAlert.getRatplateDescription()+"slipperyAlert");
                            apture_ert.setType("1");
                            apture_ert.setCategory("11");
                            apture_ert.setUsername("管理经理");
                            apture_ert.setSdId(slipperyAlert.getSdId());
                            list.add(apture_ert);
                        }
                    }
                }
                mainQueryFlag = true;
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(list);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
