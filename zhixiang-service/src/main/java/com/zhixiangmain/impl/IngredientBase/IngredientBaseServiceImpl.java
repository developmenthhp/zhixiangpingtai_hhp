package com.zhixiangmain.impl.IngredientBase;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto.BasicInfoFoodDTO;
import com.zhixiangmain.api.service.IngredientBase.IngredientBaseService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.cgcc.scjcxx.BasicInfoOfFoodMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.apache.commons.lang.ArrayUtils;
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
 * @Package com.zhixiangmain.impl.IngredientBase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-31 10:16
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = IngredientBaseService.class)
public class IngredientBaseServiceImpl implements IngredientBaseService {
    private static final Logger logger = LoggerFactory
            .getLogger(IngredientBaseServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private BasicInfoOfFoodMapper basicInfoOfFoodMapper;

    @Override
    public ResultBean getMonthBySdId(BasicInfoFoodDTO basicInfoFoodDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> temperatureList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(basicInfoFoodDTO.getSdIds())){
            String[] sdIds = basicInfoFoodDTO.getSdIdStr().split(",");
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
                    basicInfoFoodDTO.setSdId(Integer.parseInt(sdId));
                    //食材临期月数据
                    List<Map<String,Object>> temperatureMap  = basicInfoOfFoodMapper.findMonthBySdId(basicInfoFoodDTO);
                    temperatureList = getMonthDataList(temperatureList,temperatureMap);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(basicInfoFoodDTO.getUserId());
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
                        basicInfoFoodDTO.setSdId(siteVO.getSdId());
                        //食材临期月数据
                        List<Map<String,Object>> temperatureMap  = basicInfoOfFoodMapper.findMonthBySdId(basicInfoFoodDTO);
                        temperatureList = getMonthDataList(temperatureList,temperatureMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //不合格
        resultBean.setRows(temperatureList);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getByDateSdId(BasicInfoFoodDTO basicInfoFoodDTO, JSONObject jobj) {
        return null;
    }

    public List<Map<String,Object>> getMonthDataList(List<Map<String,Object>> allList,List<Map<String,Object>> paramList){
        if(!IsEmptyUtils.isEmpty(allList)){
            for(Map<String,Object> map:paramList){
                int index = -1;
                for(int i=0;i<allList.size();i++){
                    String curDataLcrDate = map.get("createTime").toString();
                    String curDataLcrAllDate = allList.get(i).get("createTime").toString();
                    if(curDataLcrDate.equals(curDataLcrAllDate)){
                        index = i;
                        //相同日期数据相加
                        //这里还要区分是否是同一种食材，这里取的是同一天入库的食材的种类数量
                        String[] mapIngredientId = map.get("ingredientId").toString().split(",");
                        String[] allIngredientId = allList.get(i).get("ingredientId").toString().split(",");
                        int countIngId = 0;
                        int addCount = 0;
                        for(String ingredientId:mapIngredientId){
                            if(ArrayUtils.contains(allIngredientId,ingredientId)){
                                //当前数组有食材在另一个数据源相同日期存在，记录这些相同数据条数
                                countIngId = countIngId + 1;
                            }else{
                                addCount = addCount + 1;
                                //将不同食材放入，该一天
                                allList.get(i).put("ingredientId",allList.get(i).get("ingredientId")+","+ingredientId);
                            }
                        }
                        //这里还需要减去相同食材数量
                        allList.get(i).put("dataCount",Integer.parseInt(map.get("dataCount").toString())+Integer.parseInt(allList.get(i).get("dataCount").toString())+addCount-countIngId);
                        break;
                    }else{
                        index = -1;
                    }
                }
                if(index == -1){
                    allList.add(map);
                }
            }
        }else{
            allList.addAll(paramList);
        }
        return allList;
    }
}
