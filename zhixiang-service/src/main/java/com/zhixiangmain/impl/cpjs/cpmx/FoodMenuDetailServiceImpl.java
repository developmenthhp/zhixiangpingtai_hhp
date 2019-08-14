package com.zhixiangmain.impl.cpjs.cpmx;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cpjs.cpmx.FoodMenuDetails;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDetailsBlendDTO;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDetailsDTO;
import com.zhixiangmain.api.service.cpjs.cpmx.FoodMenuDetailService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.web.requestdata.WebMassage;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cpjs.cpmx.FoodMenuDetailMapper;
import com.zhixiangmain.dao.cpjs.cpmx.FoodMenuMapper;
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
 * @date: 2019-04-09 14:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = FoodMenuDetailService.class)
public class FoodMenuDetailServiceImpl implements FoodMenuDetailService{
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(FoodMenuDetailServiceImpl.class);
    @Autowired
    private FoodMenuDetailMapper foodMenuDetailMapper;
    @Autowired
    private FoodMenuMapper foodMenuMapper;

    @Override
    public ResultBean getBySdIdFMId(FoodMenuDetailsDTO foodMenuDetailsDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        String dataSourceName = "";
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(foodMenuDetailsDTO)){
            if(!IsEmptyUtils.isEmpty(foodMenuDetailsDTO.getSdId())){
                String sdId = foodMenuDetailsDTO.getSdId().toString();
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
            }
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)){
            logger.info("切换至--"+dataSourceName+"--数据源");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            if(!IsEmptyUtils.isEmpty(siteData)){
                foodMenuDetailsDTO.setSiteName(siteData.getName());
                foodMenuDetailsDTO.setSitePhoto(siteData.getPhoto());
            }
            List<Map<String,Object>> mainLPList = foodMenuDetailMapper.findBySdIdFoodMenuId(foodMenuDetailsDTO);
            resultBean.setRows(mainLPList);

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean updateBlend(FoodMenuDetailsBlendDTO foodMenuDetailsBlendDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(foodMenuDetailsBlendDTO)){
            if(!IsEmptyUtils.isEmpty(foodMenuDetailsBlendDTO.getSdId())){
                String sdId = foodMenuDetailsBlendDTO.getSdId().toString();
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    dataSourceName = jobj.get(sdId).toString();
                }
            }
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)){
            logger.info("切换至--"+dataSourceName+"--数据源");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            List<FoodMenuDetails> foodMenuDetailsAdd = Lists.newArrayList();
            List<FoodMenuDetails> foodMenuDetailsDelete = Lists.newArrayList();
            List<FoodMenuDetails> foodMenuDetailsUpdate = Lists.newArrayList();
            if(!IsEmptyUtils.isEmpty(foodMenuDetailsBlendDTO.getAddJson())){
                foodMenuDetailsAdd = JSONArray.parseArray(foodMenuDetailsBlendDTO.getAddJson(), FoodMenuDetails.class);
            }
            if(!IsEmptyUtils.isEmpty(foodMenuDetailsBlendDTO.getAddJson())){
                foodMenuDetailsDelete = JSONArray.parseArray(foodMenuDetailsBlendDTO.getDeleteJson(), FoodMenuDetails.class);
            }
            if(!IsEmptyUtils.isEmpty(foodMenuDetailsBlendDTO.getAddJson())){
                foodMenuDetailsUpdate = JSONArray.parseArray(foodMenuDetailsBlendDTO.getUpdateJson(), FoodMenuDetails.class);
            }

            for(FoodMenuDetails foodMenuDetails:foodMenuDetailsAdd){
                if(!IsEmptyUtils.isEmpty(foodMenuDetails.getFoodMenuId())){
                    //新增一条foodMenu 这里不新增，门号（，这里先只让他去发布菜谱界面新增菜单）
                    Integer count = foodMenuDetailMapper.insertFoodMenuDetail(foodMenuDetails);
                }

            }
            for(FoodMenuDetails foodMenuDetails:foodMenuDetailsDelete){
                Integer count = foodMenuDetailMapper.deleteFoodMenuDetail(foodMenuDetails.getId());
            }
            for(FoodMenuDetails foodMenuDetails:foodMenuDetailsUpdate){
                Integer count = foodMenuDetailMapper.updateFoodMenuDetail(foodMenuDetails);
            }

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            resultBean.setMsg("请选择站点");
        }
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(WebMassage.FOOD_MENU_DETAIL_UPDATE_SUCCESS);
        return resultBean;
    }
}
