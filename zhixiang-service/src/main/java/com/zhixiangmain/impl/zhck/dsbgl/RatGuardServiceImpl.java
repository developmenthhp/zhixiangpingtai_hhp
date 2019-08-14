package com.zhixiangmain.impl.zhck.dsbgl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.ratplateAlert.dto.RatplateAlertDTO;
import com.zhixiangmain.api.service.zhck.dsbgl.RatGuardService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.ratGuardBase.RatGuardBaseMapper;
import com.zhixiangmain.dao.ratplateAlert.RatplateAlertMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.zhck.dsbgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-03 16:25
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = RatGuardService.class)
public class RatGuardServiceImpl implements RatGuardService {
    private static final Logger logger = LoggerFactory
            .getLogger(RatGuardServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private RatGuardBaseMapper ratGuardBaseMapper;
    @Autowired
    private RatplateAlertMapper ratplateAlertMapper;

    @Override
    public ResultBean getRGAlertPieBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        Integer allFWallCount = 0;
        Integer warningCount = 0;
        Integer normalCount = 0;
        List<Map<String,Object>> siteFWAlertMaps = Lists.newArrayList();
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
                    //获取采购单
                    baseEntityDTO.setSdId(Integer.parseInt(sdId));

                    Integer countAll = ratGuardBaseMapper.findAllRGBase(baseEntityDTO);

                    RatplateAlertDTO ratplateAlertDTO = new RatplateAlertDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, ratplateAlertDTO);

                    ratplateAlertDTO.setRatplateStatus("1");

                    if(countAll>0){
                        allFWallCount = allFWallCount + 1;
                        //如果这个点装了，需要查询, 正在报警中
                        Integer warningTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                        if(warningTotal>0){
                            //当前在报警
                            warningCount = warningCount + 1;
                        }
                        ratplateAlertDTO.setRatplateStatus("2");
                        Integer normalTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                        if(normalTotal>0){
                            //当前已处理或未报警
                            normalCount = normalCount + 1;
                        }
                        ratplateAlertDTO.setRatplateStatus("");

                        //查询所有报警数,包括已处理的报警
                        Integer allWarnTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                        Map<String,Object> alertMap = new HashMap<>();
                        alertMap.put("name",siteData.getName());
                        alertMap.put("photo",siteData.getPhoto());
                        alertMap.put("warningTotal",allWarnTotal);
                        siteFWAlertMaps.add(alertMap);
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

                        Integer countAll = ratGuardBaseMapper.findAllRGBase(baseEntityDTO);

                        RatplateAlertDTO ratplateAlertDTO = new RatplateAlertDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, ratplateAlertDTO);

                        ratplateAlertDTO.setRatplateStatus("1");

                        if(countAll>0){
                            allFWallCount = allFWallCount + 1;
                            //如果这个点装了，需要查询, 正在报警中
                            Integer warningTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                            if(warningTotal>0){
                                //当前在报警
                                warningCount = warningCount + 1;
                            }
                            System.out.println(warningCount);
                            ratplateAlertDTO.setRatplateStatus("2");
                            Integer normalTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                            if(normalTotal>0){
                                //当前已处理或未报警
                                normalCount = normalCount + 1;
                            }
                            ratplateAlertDTO.setRatplateStatus("");
                            //查询所有报警数,包括已处理的报警
                            Integer allWarnTotal = ratplateAlertMapper.findWarRatTotal(ratplateAlertDTO);
                            Map<String,Object> alertMap = new HashMap<>();
                            alertMap.put("name",siteData.getName());
                            alertMap.put("photo",siteData.getPhoto());
                            alertMap.put("warningTotal",allWarnTotal);
                            siteFWAlertMaps.add(alertMap);
                        }
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //已安装点总数
        resultBean.setTotal(allFWallCount);
        //报警站点数
        resultBean.setData(warningCount);
        //未报警站点数
        resultBean.setTotalPage(normalCount);
        //用作页面站点排行
        resultBean.setRows(siteFWAlertMaps);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
