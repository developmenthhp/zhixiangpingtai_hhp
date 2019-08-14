package com.zhixiangmain.impl.scgl.spszgl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.scgl.spszgl.dto.WarehousDetailsDTO;
import com.zhixiangmain.api.service.scgl.spszgl.CableTicketCardService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.scgl.spszgl.CableTicketCardMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.scgl.spszgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-30 10:48
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CableTicketCardService.class)
public class CableTicketCardServiceImpl implements CableTicketCardService {
    private static final Logger logger = LoggerFactory
            .getLogger(CableTicketCardServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CableTicketCardMapper cableTicketCardMapper;

    @Override
    public ResultBean getCTCBySdId(WarehousDetailsDTO warehousDetailsDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> temperatureList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(warehousDetailsDTO)){
            if(!IsEmptyUtils.isEmpty(warehousDetailsDTO.getSelectYear())&&!IsEmptyUtils.isEmpty(warehousDetailsDTO.getSelectMonth())){
                //都不为空
                warehousDetailsDTO.setSelectYearMonth(warehousDetailsDTO.getSelectYear()+warehousDetailsDTO.getSelectMonth());
            }else if(IsEmptyUtils.isEmpty(warehousDetailsDTO.getSelectYear())&&IsEmptyUtils.isEmpty(warehousDetailsDTO.getSelectMonth())){
                //都为空
                //都不为空
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
                warehousDetailsDTO.setSelectYearMonth(simpleDateFormat.format(new Date()));
            }
        }
        if(!IsEmptyUtils.isEmpty(warehousDetailsDTO.getSdIds())){
            String[] sdIds = warehousDetailsDTO.getSdIds().split(",");
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
                    warehousDetailsDTO.setSdId(Integer.parseInt(sdId));
                    //食材入库月数据
                    List<Map<String,Object>> temperatureMap  = cableTicketCardMapper.findCTCBySdId(warehousDetailsDTO);
                    temperatureList = getMonthDataList(temperatureList,temperatureMap);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(warehousDetailsDTO.getUserId());
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
                        warehousDetailsDTO.setSdId(siteVO.getSdId());
                        //食材入库月数据
                        List<Map<String,Object>> temperatureMap  = cableTicketCardMapper.findCTCBySdId(warehousDetailsDTO);
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
    public ResultBean getCTCByDateSdId(WarehousDetailsDTO warehousDetailsDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        int billTotal = 0;
        int healthCertificateTotal = 0;
        int certificateTotal = 0;
        if(!IsEmptyUtils.isEmpty(warehousDetailsDTO.getSdIds())){
            String[] sdIds = warehousDetailsDTO.getSdIds().split(",");
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
                    warehousDetailsDTO.setSdId(Integer.parseInt(sdId));
                    WarehousDetailsDTO warehousDetailsDTO1 = new WarehousDetailsDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(warehousDetailsDTO, warehousDetailsDTO1);
                    warehousDetailsDTO1.setNotePictures("pic");
                    //获取票据图片不为空条数,这里由于不同站点票据不同，不进行相同食材票据条数合并
                    Integer billCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                    billTotal = billTotal + billCount;

                    if(dataSourceName.equals("szyd")||dataSourceName.equals("shaoxing")){
                        healthCertificateTotal = healthCertificateTotal + 0;
                        certificateTotal = certificateTotal + 0;
                    }else{
                        warehousDetailsDTO1.setNotePictures(null);
                        warehousDetailsDTO1.setSanitaryCertificate("pic");
                        //获取卫生证图片不为空条数
                        Integer healthCertificateCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                        healthCertificateTotal = healthCertificateTotal + healthCertificateCount;

                        warehousDetailsDTO1.setSanitaryCertificate(null);
                        warehousDetailsDTO1.setCertificateOfSoundness("pic");
                        //获取合格证图片不为空条数
                        Integer certificateCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                        certificateTotal = certificateTotal + certificateCount;
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(warehousDetailsDTO.getUserId());
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
                        warehousDetailsDTO.setSdId(siteVO.getSdId());

                        WarehousDetailsDTO warehousDetailsDTO1 = new WarehousDetailsDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(warehousDetailsDTO, warehousDetailsDTO1);
                        warehousDetailsDTO1.setNotePictures("pic");
                        //获取票据图片不为空条数,这里由于不同站点票据不同，不进行相同食材票据条数合并
                        Integer billCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                        billTotal = billTotal + billCount;

                        if(dataSourceName.equals("szyd")||dataSourceName.equals("shaoxing")){
                            healthCertificateTotal = healthCertificateTotal + 0;
                            certificateTotal = certificateTotal + 0;
                        }else{
                            warehousDetailsDTO1.setNotePictures(null);
                            warehousDetailsDTO1.setSanitaryCertificate("pic");
                            //获取卫生证图片不为空条数
                            Integer healthCertificateCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                            healthCertificateTotal = healthCertificateTotal + healthCertificateCount;

                            warehousDetailsDTO1.setSanitaryCertificate(null);
                            warehousDetailsDTO1.setCertificateOfSoundness("pic");
                            //获取合格证图片不为空条数
                            Integer certificateCount = cableTicketCardMapper.findCTByDateSdId(warehousDetailsDTO1);
                            certificateTotal = certificateTotal + certificateCount;
                        }
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //有票据数
        resultBean.setData(billTotal);
        //有卫生证数
        resultBean.setTotal(healthCertificateTotal);
        //有合格证数
        resultBean.setTotalPage(certificateTotal);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
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

    public static void main(String[] args){
        List<Map<String,Object>> allList = Lists.newArrayList();
        List<Map<String,Object>> paramList = Lists.newArrayList();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("ingredientId","167");
        map1.put("dataCount",1);
        map1.put("createTime","2019-05-17");
        Map<String,Object> map2 = new HashMap<>();
        map2.put("ingredientId","168,183");
        map2.put("dataCount",2);
        map2.put("createTime","2019-05-22");
        Map<String,Object> map3 = new HashMap<>();
        map3.put("ingredientId","231,183");
        map3.put("dataCount",2);
        map3.put("createTime","2019-05-28");
        Map<String,Object> map4 = new HashMap<>();
        map4.put("ingredientId","199,214,193,182");
        map4.put("dataCount",4);
        map4.put("createTime","2019-05-29");
        allList.add(map1);
        allList.add(map2);
        allList.add(map3);
        allList.add(map4);


        Map<String,Object> map5 = new HashMap<>();
        map5.put("ingredientId","167,169");
        map5.put("dataCount",1);
        map5.put("createTime","2019-05-17");
        Map<String,Object> map6 = new HashMap<>();
        map6.put("ingredientId","169");
        map6.put("dataCount",1);
        map6.put("createTime","2019-05-27");
        Map<String,Object> map7 = new HashMap<>();
        map7.put("ingredientId","231,183");
        map7.put("dataCount",2);
        map7.put("createTime","2019-05-28");
        Map<String,Object> map8 = new HashMap<>();
        map8.put("ingredientId","199,214,193,182");
        map8.put("dataCount",4);
        map8.put("createTime","2019-05-29");
        paramList.add(map5);
        paramList.add(map6);
        paramList.add(map7);
        paramList.add(map8);

        CableTicketCardServiceImpl ctci = new CableTicketCardServiceImpl();

        allList = ctci.getMonthDataList(allList,paramList);

        for(Map<String,Object> map:allList){
            System.out.println(map.get("ingredientId")+",,,,,"+map.get("dataCount")+",,,,"+map.get("createTime"));
        }
        System.out.println(allList);
    }
}
