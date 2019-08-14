package com.zhixiangmain.impl.zhjg.xwbzhgl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.base.dto.AbnormalSnapshotDTO;
import com.zhixiangmain.api.service.zhjg.xwbzhgl.StandardWearService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.lechengCheckRecord.LechengCheckRecordMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.zhjg.xwbzhgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-22 16:55
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = StandardWearService.class)
public class StandardWearServiceImpl implements StandardWearService {
    private static final Logger logger = LoggerFactory
            .getLogger(StandardWearServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private LechengCheckRecordMapper lechengCheckRecordMapper;

    @Override
    public ResultBean getSWCharBySdId(AbnormalSnapshotDTO abnormalSnapshotDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> lcrmapList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(abnormalSnapshotDTO)){
            if(!IsEmptyUtils.isEmpty(abnormalSnapshotDTO.getSelectYear())&&!IsEmptyUtils.isEmpty(abnormalSnapshotDTO.getSelectMonth())){
                //都不为空
                abnormalSnapshotDTO.setSelectYearMonth(abnormalSnapshotDTO.getSelectYear()+abnormalSnapshotDTO.getSelectMonth());
            }else if(IsEmptyUtils.isEmpty(abnormalSnapshotDTO.getSelectYear())&&IsEmptyUtils.isEmpty(abnormalSnapshotDTO.getSelectMonth())){
                //都为空
                //都不为空
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
                abnormalSnapshotDTO.setSelectYearMonth(simpleDateFormat.format(new Date()));
            }
        }
        if(!IsEmptyUtils.isEmpty(abnormalSnapshotDTO.getSdIds())){
            String[] sdIds = abnormalSnapshotDTO.getSdIds().split(",");
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
                    abnormalSnapshotDTO.setSdId(Integer.parseInt(sdId));
                    /*healthCertificateDTO.setSiteName(siteData.getName());
                    healthCertificateDTO.setSitePhoto(siteData.getPhoto());*/
                    //这里如果数据都正常，那么可以只查询健康证表的所有条数，如果精准点就需要从mainaccount innerjoin关联健康表查询条数
                    // 这里先只查询健康表条数，从优化角度考虑
                    List<Map<String,Object>> abnSnapMap  = lechengCheckRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO);
                    lcrmapList = getMonthDataList(lcrmapList,abnSnapMap);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(abnormalSnapshotDTO.getUserId());
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
                        abnormalSnapshotDTO.setSdId(siteVO.getSdId());
                        List<Map<String,Object>> abnSnapMap  = lechengCheckRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO);
                        lcrmapList = getMonthDataList(lcrmapList,abnSnapMap);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(lcrmapList);
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
                        allList.get(i).put("dataCount",Integer.parseInt(map.get("dataCount").toString())+Integer.parseInt(allList.get(i).get("dataCount").toString()));
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
