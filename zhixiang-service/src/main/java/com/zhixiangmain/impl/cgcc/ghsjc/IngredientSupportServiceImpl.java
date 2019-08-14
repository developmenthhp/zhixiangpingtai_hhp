package com.zhixiangmain.impl.cgcc.ghsjc;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.ghsjc.dto.IngredientSupportDTO;
import com.zhixiangmain.api.service.cgcc.ghsjc.IngredientSupportService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cgcc.ghsjc.IngredientSupportMapper;
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
 * @Package com.zhixiangyun.impl.cgcc.ghsjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-18 9:25
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = IngredientSupportService.class)
public class IngredientSupportServiceImpl implements IngredientSupportService {
    private static final Logger logger = LoggerFactory
            .getLogger(IngredientSupportServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private IngredientSupportMapper ingredientSupportMapper;

    @Override
    public ResultBean getAllBySdId(IngredientSupportDTO ingredientSupportDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(ingredientSupportDTO.getSdIds())){
            String[] sdIds = ingredientSupportDTO.getSdIds().split(",");
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
                    ingredientSupportDTO.setSdId(Integer.parseInt(sdId));
                    ingredientSupportDTO.setSiteName(siteData.getName());
                    ingredientSupportDTO.setSitePhoto(siteData.getPhoto());
                    /*if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getIdSdId())){
                        foodBaseDTO.setId(Integer.parseInt(foodBaseDTO.getIdSdId().split("-")[0]));
                    }
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getMainCategoryIdSdId())){
                        foodBaseDTO.setMainCategoryId(Integer.parseInt(foodBaseDTO.getMainCategoryIdSdId().split("-")[0]));
                    }
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSmallCategoryIdSdId())){
                        foodBaseDTO.setSmallCategoryId(Integer.parseInt(foodBaseDTO.getSmallCategoryIdSdId().split("-")[0]));
                    }*/
                    List<Map<String,Object>> categoryBases = ingredientSupportMapper.findAllIngSupBySdId(ingredientSupportDTO);
                    mainLPLs.addAll(categoryBases);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(ingredientSupportDTO.getUserId());
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
                        ingredientSupportDTO.setSdId(siteVO.getSdId());
                        ingredientSupportDTO.setSiteName(siteData.getName());
                        ingredientSupportDTO.setSitePhoto(siteData.getPhoto());
                        /*if(!IsEmptyUtils.isEmpty(foodBaseDTO.getIdSdId())){
                            foodBaseDTO.setId(Integer.parseInt(foodBaseDTO.getIdSdId().split("-")[0]));
                        }
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getMainCategoryIdSdId())){
                            foodBaseDTO.setMainCategoryId(Integer.parseInt(foodBaseDTO.getMainCategoryIdSdId().split("-")[0]));
                        }
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSmallCategoryIdSdId())){
                            foodBaseDTO.setSmallCategoryId(Integer.parseInt(foodBaseDTO.getSmallCategoryIdSdId().split("-")[0]));
                        }*/
                        List<Map<String,Object>> categoryBases = ingredientSupportMapper.findAllIngSupBySdId(ingredientSupportDTO);
                        mainLPLs.addAll(categoryBases);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }

        resultBean.setRows(mainLPLs);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
