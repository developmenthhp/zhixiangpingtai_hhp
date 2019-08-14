package com.zhixiangmain.impl.jcxxpt.zzgs;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.service.jcxxpt.zzgs.NotarizedCertificateService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.jygs.gszz.BusinessMapper;
import com.zhixiangmain.dao.jygs.gszz.CirculationCardMapper;
import com.zhixiangmain.dao.jygs.gszz.CompanyLicenseMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
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
 * @Package com.zhixiangmain.impl.jcxxpt.zzgs
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-26 8:57
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = NotarizedCertificateService.class)
public class NotarizedCertificateServiceImpl implements NotarizedCertificateService {
    private static final Logger logger = LoggerFactory
            .getLogger(NotarizedCertificateServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private CompanyLicenseMapper companyLicenseMapper;
    @Autowired
    private CirculationCardMapper circulationCardMapper;

    @Override
    public ResultBean getNotCerBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int siteCount = 0;
        int busCount = 0;
        int licCount = 0;
        int cirCount = 0;
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
                    baseEntityDTO.setSdId(Integer.parseInt(sdId));
                    siteCount = siteCount + 1;
                    Integer busTotal = businessMapper.findTotal(baseEntityDTO);
                    if(busTotal>0){
                        busCount = busCount + 1;
                    }
                    Integer licTotal = companyLicenseMapper.findTotal(baseEntityDTO);
                    if(licTotal>0){
                        licCount = licCount + 1;
                    }
                    Integer cirTotal = circulationCardMapper.findTotal(baseEntityDTO);
                    if(cirTotal>0){
                        cirCount = cirCount + 1;
                    }
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
                        siteCount = siteCount + 1;
                        Integer busTotal = businessMapper.findTotal(baseEntityDTO);
                        if(busTotal>0){
                            busCount = busCount + 1;
                        }
                        Integer licTotal = companyLicenseMapper.findTotal(baseEntityDTO);
                        if(licTotal>0){
                            licCount = licCount + 1;
                        }
                        Integer cirTotal = circulationCardMapper.findTotal(baseEntityDTO);
                        if(cirTotal>0){
                            cirCount = cirCount + 1;
                        }
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //总站点数
        resultBean.setData(siteCount);
        //营业执照上传数
        resultBean.setTotal(busCount);
        //餐饮许可证上传数
        resultBean.setTotalPage(licCount);
        //食品流通证上传数
        resultBean.setObj(cirCount);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getBusInfoBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int siteCount = 0;
        List<Map<String,Object>> resultList = Lists.newArrayList();
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
                    baseEntityDTO.setSdId(Integer.parseInt(sdId));
                    siteCount = siteCount + 1;
                    baseEntityDTO.setSiteName(siteData.getName());
                    baseEntityDTO.setSitePhoto(siteData.getPhoto());
                    List<Map<String,Object>> busMap = businessMapper.findBusInfoBySdId(baseEntityDTO);
                    resultList.addAll(busMap);
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
                        siteCount = siteCount + 1;
                        baseEntityDTO.setSiteName(siteData.getName());
                        baseEntityDTO.setSitePhoto(siteData.getPhoto());
                        List<Map<String,Object>> busMap = businessMapper.findBusInfoBySdId(baseEntityDTO);
                        resultList.addAll(busMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(resultList);
        resultBean.setTotal(siteCount);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getLicInfoBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int siteCount = 0;
        List<Map<String,Object>> resultList = Lists.newArrayList();
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
                    baseEntityDTO.setSdId(Integer.parseInt(sdId));
                    siteCount = siteCount + 1;
                    baseEntityDTO.setSiteName(siteData.getName());
                    baseEntityDTO.setSitePhoto(siteData.getPhoto());
                    List<Map<String,Object>> busMap = companyLicenseMapper.findLicInfoBySdId(baseEntityDTO);
                    resultList.addAll(busMap);
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
                        siteCount = siteCount + 1;
                        baseEntityDTO.setSiteName(siteData.getName());
                        baseEntityDTO.setSitePhoto(siteData.getPhoto());
                        List<Map<String,Object>> busMap = companyLicenseMapper.findLicInfoBySdId(baseEntityDTO);
                        resultList.addAll(busMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(resultList);
        resultBean.setTotal(siteCount);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getCirInfoBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int siteCount = 0;
        List<Map<String,Object>> resultList = Lists.newArrayList();
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
                    baseEntityDTO.setSdId(Integer.parseInt(sdId));
                    siteCount = siteCount + 1;
                    baseEntityDTO.setSiteName(siteData.getName());
                    baseEntityDTO.setSitePhoto(siteData.getPhoto());
                    List<Map<String,Object>> busMap = circulationCardMapper.findCirInfoBySdId(baseEntityDTO);
                    resultList.addAll(busMap);
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
                        siteCount = siteCount + 1;
                        baseEntityDTO.setSiteName(siteData.getName());
                        baseEntityDTO.setSitePhoto(siteData.getPhoto());
                        List<Map<String,Object>> busMap = circulationCardMapper.findCirInfoBySdId(baseEntityDTO);
                        resultList.addAll(busMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(resultList);
        resultBean.setTotal(siteCount);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
