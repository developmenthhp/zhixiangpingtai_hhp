package com.zhixiangmain.impl.base;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.base.dto.CertificateWarningDTO;
import com.zhixiangmain.api.service.base.CertificateWarningService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.cgcc.ghsjc.SupplierCirculationCardMapper;
import com.zhixiangmain.dao.cgcc.ghsjc.SupplierLicenseMapper;
import com.zhixiangmain.dao.jcxxpt.cyrygk.HealthMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
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
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-22 15:46
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CertificateWarningService.class)
public class CertificateWarningServiceImpl implements CertificateWarningService {
    private static final Logger logger = LoggerFactory
            .getLogger(CertificateWarningServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    /*@Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private LicenseMapper licenseMapper;
    @Autowired
    private CirculationCardMapper circulationCardMapper;*/
    @Autowired
    private HealthMapper healthMapper;
    @Autowired
    private SupplierLicenseMapper supplierLicenseMapper;
    @Autowired
    private SupplierCirculationCardMapper supplierCirculationCardMapper;

    @Override
    public ResultBean getTopCertificateWarning(CertificateWarningDTO certificateWarningDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(certificateWarningDTO.getSdIds())){
            String[] sdIds = certificateWarningDTO.getSdIds().split(",");
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
                    certificateWarningDTO.setSdId(Integer.parseInt(sdId));
                    certificateWarningDTO.setSiteName(siteData.getName());
                    certificateWarningDTO.setSitePhoto(siteData.getPhoto());
                    //营业执照，许可证，流通证---格式不确定，展示不用
                    List<Map<String,Object>> mainLPList = healthMapper.findTopCertificateWarning(certificateWarningDTO);
                    List<Map<String,Object>> supplierLicenseList = supplierLicenseMapper.findTopSupplierLicenseWarning(certificateWarningDTO);
                    List<Map<String,Object>> supplierCirculationCarList = supplierCirculationCardMapper.findTopSupplierCirculationCarWarning(certificateWarningDTO);
                    mainLPLs.addAll(mainLPList);
                    mainLPLs.addAll(supplierLicenseList);
                    mainLPLs.addAll(supplierCirculationCarList);

                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(certificateWarningDTO.getUserId());
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
                        certificateWarningDTO.setSdId(siteVO.getSdId());
                        certificateWarningDTO.setSiteName(siteData.getName());
                        certificateWarningDTO.setSitePhoto(siteData.getPhoto());
                        List<Map<String,Object>> mainLPList = healthMapper.findTopCertificateWarning(certificateWarningDTO);
                        List<Map<String,Object>> supplierLicenseList = supplierLicenseMapper.findTopSupplierLicenseWarning(certificateWarningDTO);
                        List<Map<String,Object>> supplierCirculationCarList = supplierCirculationCardMapper.findTopSupplierCirculationCarWarning(certificateWarningDTO);
                        mainLPLs.addAll(mainLPList);
                        mainLPLs.addAll(supplierLicenseList);
                        mainLPLs.addAll(supplierCirculationCarList);
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
