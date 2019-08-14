package com.zhixiangmain.impl.base;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.base.dto.TrialSigningDTO;
import com.zhixiangmain.api.service.base.TrialSigningService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.cgcc.scck.LibraryOutMapper;
import com.zhixiangmain.dao.cgcc.scrk.IwareReportMapper;
import com.zhixiangmain.dao.jybb.hplldsq.IwareOutMapper;
import com.zhixiangmain.dao.jybb.kcclbbsq.IwarekCBBMapper;
import com.zhixiangmain.dao.jybb.kcpdbbsq.IwarekCPDMapper;
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
 * @Description:  所有待审签单处理
 * @author: hhp
 * @date: 2019-04-23 17:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = TrialSigningService.class)
public class TrialSigningServiceImpl implements TrialSigningService {
    private static final Logger logger = LoggerFactory
            .getLogger(TrialSigningServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private IwareReportMapper iwareReportMapper;
    @Autowired
    private LibraryOutMapper libraryOutMapper;
    @Autowired
    private IwareOutMapper iwareOutMapper;
    @Autowired
    private IwarekCBBMapper iwarekCBBMapper;
    @Autowired
    private IwarekCPDMapper iwarekCPDMapper;

    @Override
    public ResultBean getTotalTrialSigning(TrialSigningDTO trialSigningDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        Integer countAll = 0;
        if(!IsEmptyUtils.isEmpty(trialSigningDTO.getSdIds())){
            String[] sdIds = trialSigningDTO.getSdIds().split(",");
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
                    trialSigningDTO.setSdId(Integer.parseInt(sdId));
                    trialSigningDTO.setSiteName(siteData.getName());
                    trialSigningDTO.setSitePhoto(siteData.getPhoto());
                    Integer countIR = iwareReportMapper.findTrialSigningCount(trialSigningDTO);
                    Integer countIO = libraryOutMapper.findTrialSigningCount(trialSigningDTO);
                    Integer countIOu = iwareOutMapper.findTrialSigningCount(trialSigningDTO);
                    Integer countIC = iwarekCBBMapper.findTrialSigningCount(trialSigningDTO);
                    Integer countICP = iwarekCPDMapper.findTrialSigningCount(trialSigningDTO);
                    countAll =countAll+countIR+countIO+countIOu+countIC+countICP;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(trialSigningDTO.getUserId());
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
                        trialSigningDTO.setSdId(siteVO.getSdId());
                        trialSigningDTO.setSiteName(siteData.getName());
                        trialSigningDTO.setSitePhoto(siteData.getPhoto());
                        Integer countIR = iwareReportMapper.findTrialSigningCount(trialSigningDTO);
                        Integer countIO = libraryOutMapper.findTrialSigningCount(trialSigningDTO);
                        Integer countIOu = iwareOutMapper.findTrialSigningCount(trialSigningDTO);
                        Integer countIC = iwarekCBBMapper.findTrialSigningCount(trialSigningDTO);
                        Integer countICP = iwarekCPDMapper.findTrialSigningCount(trialSigningDTO);
                        countAll =countAll+countIR+countIO+countIOu+countIC+countICP;
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
