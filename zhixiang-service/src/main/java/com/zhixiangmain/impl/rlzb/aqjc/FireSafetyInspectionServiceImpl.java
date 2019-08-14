package com.zhixiangmain.impl.rlzb.aqjc;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.rlzb.aqjc.dto.FireSafetyInspectionDTO;
import com.zhixiangmain.api.service.rlzb.aqjc.FireSafetyInspectionService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.rlzb.aqjc.FireSafetyInspectionMapper;
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
 * @Package com.zhixiangyun.impl.rlzb.aqjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-03 13:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = FireSafetyInspectionService.class)
public class FireSafetyInspectionServiceImpl implements FireSafetyInspectionService {
    private static final Logger logger = LoggerFactory
            .getLogger(FireSafetyInspectionServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private FireSafetyInspectionMapper fireSafetyInspectionMapper;

    @Override
    public ResultBean getByMonth(FireSafetyInspectionDTO fireSafetyInspectionDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mapList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(fireSafetyInspectionDTO.getSdIds())){
            String[] sdIds = fireSafetyInspectionDTO.getSdIds().split(",");
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
                    fireSafetyInspectionDTO.setSdId(Integer.parseInt(sdId));
                    fireSafetyInspectionDTO.setSiteName(siteData.getName());
                    fireSafetyInspectionDTO.setSitePhoto(siteData.getPhoto());
                    List<Map<String,Object>> maps = fireSafetyInspectionMapper.findByMonth(fireSafetyInspectionDTO);
                    mapList.addAll(maps);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(fireSafetyInspectionDTO.getUserId());
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
                        fireSafetyInspectionDTO.setSdId(siteVO.getSdId());
                        fireSafetyInspectionDTO.setSiteName(siteData.getName());
                        fireSafetyInspectionDTO.setSitePhoto(siteData.getPhoto());
                        List<Map<String,Object>> maps = fireSafetyInspectionMapper.findByMonth(fireSafetyInspectionDTO);
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
}
