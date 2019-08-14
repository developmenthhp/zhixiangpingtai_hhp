package com.zhixiangmain.impl.cgcc.scrk;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.scrk.dto.IwareReportDTO;
import com.zhixiangmain.api.service.cgcc.scrk.IwareReportService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.cgcc.scrk.IwareReportMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.cgcc.scrk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-23 17:21
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = IwareReportService.class)
public class IwareReportServiceImpl implements IwareReportService {
    private static final Logger logger = LoggerFactory
            .getLogger(IwareReportServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private IwareReportMapper iwareReportMapper;

    @Override
    public ResultBean getTotalBySdIdStatus(IwareReportDTO iwareReportDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int notTrialSigningTotal = 0;
        int trialSigningTotal = 0;
        if(!IsEmptyUtils.isEmpty(iwareReportDTO.getSdIds())){
            String[] sdIds = iwareReportDTO.getSdIds().split(",");
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
                    iwareReportDTO.setSdId(Integer.parseInt(sdId));
                    iwareReportDTO.setReportStatus("1");
                    //货品入库单总数审签中
                    Integer notTrialSigningCount = iwareReportMapper.findTotalBySdIdStatus(iwareReportDTO);
                    notTrialSigningTotal = notTrialSigningTotal + notTrialSigningCount;

                    IwareReportDTO iwareReportDTO1 = new IwareReportDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(iwareReportDTO, iwareReportDTO1);
                    iwareReportDTO1.setReportStatus("3");
                    //货品入库单总数审签完成
                    Integer trialSigningCount  = iwareReportMapper.findTotalBySdIdStatus(iwareReportDTO1);
                    trialSigningTotal = trialSigningTotal + trialSigningCount;


                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(iwareReportDTO.getUserId());
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
                        iwareReportDTO.setSdId(siteVO.getSdId());

                        iwareReportDTO.setReportStatus("1");
                        //货品入库单总数审签中
                        Integer notTrialSigningCount = iwareReportMapper.findTotalBySdIdStatus(iwareReportDTO);
                        notTrialSigningTotal = notTrialSigningTotal + notTrialSigningCount;

                        IwareReportDTO iwareReportDTO1 = new IwareReportDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(iwareReportDTO, iwareReportDTO1);
                        iwareReportDTO1.setReportStatus("3");
                        //货品入库单总数审签完成
                        Integer trialSigningCount  = iwareReportMapper.findTotalBySdIdStatus(iwareReportDTO1);
                        trialSigningTotal = trialSigningTotal + trialSigningCount;
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setData(notTrialSigningTotal);
        resultBean.setTotal(trialSigningTotal);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
