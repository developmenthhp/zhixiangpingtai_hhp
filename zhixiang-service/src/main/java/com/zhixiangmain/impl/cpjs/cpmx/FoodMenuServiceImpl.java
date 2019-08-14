package com.zhixiangmain.impl.cpjs.cpmx;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDTO;
import com.zhixiangmain.api.service.cpjs.cpmx.FoodMenuService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cpjs.cpmx.FoodMenuMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.cpjs.cpmx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-08 10:46
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = FoodMenuService.class)
public class FoodMenuServiceImpl implements FoodMenuService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(FoodMenuServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private FoodMenuMapper foodMenuMapper;

    @Override
    public ResultBean getFoodMenus(FoodMenuDTO foodMenuDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(foodMenuDTO.getSdIds())){
            String[] sdIds = foodMenuDTO.getSdIds().split(",");
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
                    foodMenuDTO.setSdId(Integer.parseInt(sdId));
                    foodMenuDTO.setSiteName(siteData.getName());
                    foodMenuDTO.setSitePhoto(siteData.getPhoto());
                    List<Map<String,Object>> mainLPList = foodMenuMapper.findFoodMenus(foodMenuDTO);
                    mainLPLs.addAll(mainLPList);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(foodMenuDTO.getUserId());
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
                        foodMenuDTO.setSdId(siteVO.getSdId());
                        foodMenuDTO.setSiteName(siteData.getName());
                        foodMenuDTO.setSitePhoto(siteData.getPhoto());
                        List<Map<String,Object>> mainLPList = foodMenuMapper.findFoodMenus(foodMenuDTO);
                        mainLPLs.addAll(mainLPList);
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
    public ResultBean getBySdIdOrderDate(FoodMenuDTO foodMenuDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        String dataSourceName = "";
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(foodMenuDTO)){
            if(!IsEmptyUtils.isEmpty(foodMenuDTO.getSdId())){
                String sdId = foodMenuDTO.getSdId().toString();
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    dataSourceName = jobj.get(sdId).toString();
                }
            }
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)){
            logger.info("切换至--"+dataSourceName+"--数据源");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            if(!IsEmptyUtils.isEmpty(siteData)){
                foodMenuDTO.setSitePhoto(siteData.getPhoto());
                foodMenuDTO.setSiteName(siteData.getName());
            }
            List<Map<String,Object>> mainLPList = foodMenuMapper.findBySdIdOrderDate(foodMenuDTO);
            resultBean.setRows(mainLPList);

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
