package com.zhixiangmain.impl.jcxxpt.cyrygk;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.jcxxpt.cyrygk.dto.HealthCertificateDTO;
import com.zhixiangmain.api.service.jcxxpt.cyrygk.HealthService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.jcxxpt.cyrygk.HealthMapper;
import com.zhixiangmain.dao.rlzb.nbyg.MainAccountMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.jcxxpt.cyrygk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-21 12:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = HealthService.class)
public class HealthServiceImpl implements HealthService {
    private static final Logger logger = LoggerFactory
            .getLogger(HealthServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private HealthMapper healthMapper;
    @Autowired
    private MainAccountMapper mainAccountMapper;

    @Override
    public ResultBean getHealthCharBySdId(HealthCertificateDTO healthCertificateDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int healthTotal = 0;
        int allTotal = 0;
        if(!IsEmptyUtils.isEmpty(healthCertificateDTO.getSdIds())){
            String[] sdIds = healthCertificateDTO.getSdIds().split(",");
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
                    healthCertificateDTO.setSdId(Integer.parseInt(sdId));
                    /*healthCertificateDTO.setSiteName(siteData.getName());
                    healthCertificateDTO.setSitePhoto(siteData.getPhoto());*/
                    //这里如果数据都正常，那么可以只查询健康证表的所有条数，如果精准点就需要从mainaccount innerjoin关联健康表查询条数
                    // 这里先只查询健康表条数，从优化角度考虑
                    Integer healthSingleTotal = healthMapper.findHasHealthBySdId(healthCertificateDTO);
                    healthTotal = healthTotal + healthSingleTotal;

                    Integer allTotalSingle = mainAccountMapper.findAllMainAccountTotalBySdId(healthCertificateDTO);
                    allTotal = allTotal + allTotalSingle;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(healthCertificateDTO.getUserId());
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
                        healthCertificateDTO.setSdId(siteVO.getSdId());
                        Integer healthSingleTotal = healthMapper.findHasHealthBySdId(healthCertificateDTO);
                        healthTotal = healthTotal + healthSingleTotal;
                        Integer allTotalSingle = mainAccountMapper.findAllMainAccountTotalBySdId(healthCertificateDTO);
                        allTotal = allTotal + allTotalSingle;
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setData(healthTotal);
        resultBean.setTotal(allTotal);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getHealthTypeCharBySdId(HealthCertificateDTO healthCertificateDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int matureHealthTotal = 0;
        int overdueHealthTotal = 0;
        if(!IsEmptyUtils.isEmpty(healthCertificateDTO.getSdIds())){
            String[] sdIds = healthCertificateDTO.getSdIds().split(",");
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
                    healthCertificateDTO.setSdId(Integer.parseInt(sdId));
                    /*healthCertificateDTO.setSiteName(siteData.getName());
                    healthCertificateDTO.setSitePhoto(siteData.getPhoto());*/
                    //这里如果数据都正常，那么可以只查询健康证表的所有条数，如果精准点就需要从mainaccount innerjoin关联健康表查询条数
                    // 这里先只查询健康表条数，从优化角度考虑
                    //查找正常
                    Integer matureHealthSingleTotal = healthMapper.findMatureHealthBySdId(healthCertificateDTO);
                    matureHealthTotal = matureHealthTotal + matureHealthSingleTotal;
                    //查找过期
                    Integer overdueHealthSingleTotal = healthMapper.findOverdueHealthBySdId(healthCertificateDTO);
                    overdueHealthTotal = overdueHealthTotal + overdueHealthSingleTotal;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(healthCertificateDTO.getUserId());
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
                        healthCertificateDTO.setSdId(siteVO.getSdId());
                        /*healthCertificateDTO.setSiteName(siteData.getName());
                        healthCertificateDTO.setSitePhoto(siteData.getPhoto());*/
                        //查找正常
                        Integer matureHealthSingleTotal = healthMapper.findMatureHealthBySdId(healthCertificateDTO);
                        matureHealthTotal = matureHealthTotal + matureHealthSingleTotal;
                        //查找过期
                        Integer overdueHealthSingleTotal = healthMapper.findOverdueHealthBySdId(healthCertificateDTO);
                        overdueHealthTotal = overdueHealthTotal + overdueHealthSingleTotal;
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setData(matureHealthTotal);
        resultBean.setTotal(overdueHealthTotal);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
