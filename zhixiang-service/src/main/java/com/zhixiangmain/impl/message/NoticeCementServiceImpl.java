package com.zhixiangmain.impl.message;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.message.dto.NoticeCementDTO;
import com.zhixiangmain.api.service.message.NoticeCementService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.aramhealth.AramhealthMapper;
import com.zhixiangmain.dao.inventoryAlert.InventoryAlertMapper;
import com.zhixiangmain.dao.message.NoticeCementMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.message
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-20 17:24
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = NoticeCementService.class)
public class NoticeCementServiceImpl implements NoticeCementService {
    private static final Logger logger = LoggerFactory
            .getLogger(NoticeCementServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private NoticeCementMapper noticeCementMapper;
    @Autowired
    private AramhealthMapper aramhealthMapper;
    @Autowired
    private InventoryAlertMapper inventoryAlertMapper;

    @Override
    public ResultBean getTopNoticeCements(NoticeCementDTO noticeCementDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(noticeCementDTO.getSdIds())){
            String[] sdIds = noticeCementDTO.getSdIds().split(",");
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
                    noticeCementDTO.setSdId(Integer.parseInt(sdId));
                    noticeCementDTO.setSiteName(siteData.getName());
                    noticeCementDTO.setSitePhoto(siteData.getPhoto());
                    Map<String,Object> mainLPList = noticeCementMapper.findTopNoticeCements(noticeCementDTO);
                    List<Map<String,Object>> aramHealthList = aramhealthMapper.findTopAramHealths(noticeCementDTO);
                    List<Map<String,Object>> inventoryAlertList = inventoryAlertMapper.findTopInventoryAlerts(noticeCementDTO);
                    mainLPLs.addAll(aramHealthList);
                    mainLPLs.addAll(inventoryAlertList);
                    if(!IsEmptyUtils.isEmpty(mainLPList)){
                        mainLPLs.add(mainLPList);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(noticeCementDTO.getUserId());
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
                        noticeCementDTO.setSdId(siteVO.getSdId());
                        noticeCementDTO.setSiteName(siteData.getName());
                        noticeCementDTO.setSitePhoto(siteData.getPhoto());
                        Map<String,Object> mainLPList = noticeCementMapper.findTopNoticeCements(noticeCementDTO);
                        List<Map<String,Object>> aramHealthList = aramhealthMapper.findTopAramHealths(noticeCementDTO);
                        List<Map<String,Object>> inventoryAlertList = inventoryAlertMapper.findTopInventoryAlerts(noticeCementDTO);
                        mainLPLs.addAll(aramHealthList);
                        mainLPLs.addAll(inventoryAlertList);
                        if(!IsEmptyUtils.isEmpty(mainLPList)){
                            mainLPLs.add(mainLPList);
                        }
                    }
                }
            }

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(mainLPLs);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
