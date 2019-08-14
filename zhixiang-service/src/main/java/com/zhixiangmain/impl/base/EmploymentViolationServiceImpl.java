package com.zhixiangmain.impl.base;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.base.dto.EmploymentViolationDTO;
import com.zhixiangmain.api.service.base.EmploymentViolationService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.disinfectionRcd.DisinfectionRcdMapper;
import com.zhixiangmain.dao.lechengCheckRecord.LechengCheckRecordMapper;
import com.zhixiangmain.dao.qppr.prcz.CookingWeightMapper;
import com.zhixiangmain.dao.qppr.qpcz.CutWeightMapper;
import com.zhixiangmain.dao.qxjp.qxwg.CleanRecordMapper;
import com.zhixiangmain.dao.qxjp.scfgcz.AirDryingWeightMapper;
import com.zhixiangmain.dao.qxjp.scqxcz.CleanWeightMapper;
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
 * @date: 2019-04-24 10:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = EmploymentViolationService.class)
public class EmploymentViolationServiceImpl implements EmploymentViolationService {
    private static final Logger logger = LoggerFactory
            .getLogger(EmploymentViolationServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CleanRecordMapper cleanRecordMapper;
    @Autowired
    private CleanWeightMapper cleanWeightMapper;
    @Autowired
    private AirDryingWeightMapper airDryingWeightMapper;
    @Autowired
    private CutWeightMapper cutWeightMapper;
    @Autowired
    private CookingWeightMapper cookingWeightMapper;
    @Autowired
    private DisinfectionRcdMapper disinfectionRcdMapper;
    @Autowired
    private LechengCheckRecordMapper lechengCheckRecordMapper;


    @Override
    public ResultBean getTotalEmploymentViolation(EmploymentViolationDTO employmentViolationDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        Integer countAll = 0;
        if(!IsEmptyUtils.isEmpty(employmentViolationDTO.getSdIds())){
            String[] sdIds = employmentViolationDTO.getSdIds().split(",");
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
                    employmentViolationDTO.setSdId(Integer.parseInt(sdId));
                    employmentViolationDTO.setSiteName(siteData.getName());
                    employmentViolationDTO.setSitePhoto(siteData.getPhoto());
                    Integer countCR = cleanRecordMapper.findTotalEmploymentViolation(employmentViolationDTO);
                    Integer countCW = cleanWeightMapper.findTotalEmploymentViolation(employmentViolationDTO);
                    Integer countADW = airDryingWeightMapper.findTotalEmploymentViolation(employmentViolationDTO);
                    Integer countCutW = cutWeightMapper.findTotalEmploymentViolation(employmentViolationDTO);
                    Integer countCookW = cookingWeightMapper.findTotalEmploymentViolation(employmentViolationDTO);
                    Integer countDR = disinfectionRcdMapper.findTotalEmploymentViolation(employmentViolationDTO);
                    Integer countLCR = lechengCheckRecordMapper.findTotalEmploymentViolation(employmentViolationDTO);
                    countAll =countAll+countCR+countCW+countADW+countCutW+countCookW+countDR+countLCR;
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(employmentViolationDTO.getUserId());
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
                        employmentViolationDTO.setSdId(siteVO.getSdId());
                        employmentViolationDTO.setSiteName(siteData.getName());
                        employmentViolationDTO.setSitePhoto(siteData.getPhoto());
                        Integer countCR = cleanRecordMapper.findTotalEmploymentViolation(employmentViolationDTO);
                        Integer countCW = cleanWeightMapper.findTotalEmploymentViolation(employmentViolationDTO);
                        Integer countADW = airDryingWeightMapper.findTotalEmploymentViolation(employmentViolationDTO);
                        Integer countCutW = cutWeightMapper.findTotalEmploymentViolation(employmentViolationDTO);
                        Integer countCookW = cookingWeightMapper.findTotalEmploymentViolation(employmentViolationDTO);
                        Integer countDR = disinfectionRcdMapper.findTotalEmploymentViolation(employmentViolationDTO);
                        Integer countLCR = lechengCheckRecordMapper.findTotalEmploymentViolation(employmentViolationDTO);
                        countAll =countAll+countCR+countCW+countADW+countCutW+countCookW+countDR+countLCR;
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
