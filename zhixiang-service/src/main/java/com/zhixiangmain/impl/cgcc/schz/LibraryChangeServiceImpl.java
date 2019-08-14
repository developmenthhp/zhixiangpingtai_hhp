package com.zhixiangmain.impl.cgcc.schz;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.schz.dto.LibraryChangeDTO;
import com.zhixiangmain.api.service.cgcc.schz.LibraryChangeService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cgcc.schz.LibraryChangeMapper;
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
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.cgcc.schz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-16 10:28
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = LibraryChangeService.class)
public class LibraryChangeServiceImpl implements LibraryChangeService {
    private static final Logger logger = LoggerFactory
            .getLogger(LibraryChangeServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private LibraryChangeMapper libraryChangeMapper;

    @Override
    public ResultBean getLibraryChanges(LibraryChangeDTO libraryChangeDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        String sdIdData = null;
        if(!IsEmptyUtils.isEmpty(libraryChangeDTO.getReservation())){
            String[] timeStartEnd = libraryChangeDTO.getReservation().split(" - ");
            libraryChangeDTO.setStartTime(timeStartEnd[0]+" 00:00:00");
            libraryChangeDTO.setEndTime(timeStartEnd[1]+" 23:59:59");
        }
        if(!IsEmptyUtils.isEmpty(libraryChangeDTO.getIngredientBaseIdSdId())){
            libraryChangeDTO.setIngredientBaseId(Integer.parseInt(libraryChangeDTO.getIngredientBaseIdSdId().split("-")[0]));
            sdIdData = libraryChangeDTO.getIngredientBaseIdSdId().split("-")[1];
        }
        if(!IsEmptyUtils.isEmpty(libraryChangeDTO.getSdIds())){
            String[] sdIds = libraryChangeDTO.getSdIds().split(",");
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
                    libraryChangeDTO.setSdId(Integer.parseInt(sdId));
                    libraryChangeDTO.setSiteName(siteData.getName());
                    libraryChangeDTO.setSitePhoto(siteData.getPhoto());
                    List<Map<String,Object>> mainLPList = libraryChangeMapper.findLibraryChanges(libraryChangeDTO);
                    mainLPLs.addAll(mainLPList);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            if(!IsEmptyUtils.isEmpty(sdIdData)){
                String dataSourceName = jobj.get(sdIdData).toString();
                //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"----数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取采购单
                    libraryChangeDTO.setSdId(Integer.parseInt(sdIdData));
                    List<Map<String,Object>> mainLPList = libraryChangeMapper.findLibraryChanges(libraryChangeDTO);
                    mainLPLs.addAll(mainLPList);
                }
            }else{
                //long start = System.currentTimeMillis();
                //查询该用户所拥有的站点权限
                List<SiteVO> userSites = siteMapper.findUserSites(libraryChangeDTO.getUserId());
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
                            libraryChangeDTO.setSdId(siteVO.getSdId());
                            libraryChangeDTO.setSiteName(siteData.getName());
                            libraryChangeDTO.setSitePhoto(siteData.getPhoto());
                            List<Map<String,Object>> mainLPList = libraryChangeMapper.findLibraryChanges(libraryChangeDTO);
                            mainLPLs.addAll(mainLPList);
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

    @Override
    public ResultBean getByIdSdId(LibraryChangeDTO libraryChangeDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(libraryChangeDTO)){
            if(!IsEmptyUtils.isEmpty(libraryChangeDTO.getSdId())){
                String sdId = libraryChangeDTO.getSdId().toString();
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    dataSourceName = jobj.get(sdId).toString();
                }
            }
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)){
            logger.info("切换至--"+dataSourceName+"--数据源");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            Map<String,Object> map = libraryChangeMapper.findByIdSdId(libraryChangeDTO);
            resultBean.setData(map);

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
