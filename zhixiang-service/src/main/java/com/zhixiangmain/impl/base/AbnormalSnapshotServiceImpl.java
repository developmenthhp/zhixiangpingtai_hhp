package com.zhixiangmain.impl.base;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.base.dto.AbnormalSnapshotDTO;
import com.zhixiangmain.api.service.base.AbnormalSnapshotService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.lechengAptureRecord.LechengAptureRecordMapper;
import com.zhixiangmain.dao.lechengCheckRecord.LechengCheckRecordMapper;
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
 * @date: 2019-04-26 16:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = AbnormalSnapshotService.class)
public class AbnormalSnapshotServiceImpl implements AbnormalSnapshotService {
    private static final Logger logger = LoggerFactory
            .getLogger(AbnormalSnapshotServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private LechengCheckRecordMapper lechengCheckRecordMapper;
    @Autowired
    private LechengAptureRecordMapper lechengAptureRecordMapper;

    @Override
    public ResultBean getAbnormalSnapshot(AbnormalSnapshotDTO abnormalSnapshotDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> lcrmapList = Lists.newArrayList();
        List<Map<String,Object>> larmapList = Lists.newArrayList();
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
                    abnormalSnapshotDTO.setSiteName(siteData.getName());
                    abnormalSnapshotDTO.setSitePhoto(siteData.getPhoto());
                    List<Map<String,Object>> lcrMap = lechengCheckRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO);
                    if(!IsEmptyUtils.isEmpty(lcrmapList)){
                        for(Map<String,Object> map:lcrMap){
                            int index = -1;
                            for(int i=0;i<lcrmapList.size();i++){
                                String curDataLcrDate = map.get("createTime").toString();
                                String curDataLcrAllDate = lcrmapList.get(i).get("createTime").toString();
                                if(curDataLcrDate.equals(curDataLcrAllDate)){
                                    index = i;
                                    //相同日期数据相加
                                    lcrmapList.get(i).put("dataCount",Integer.parseInt(map.get("dataCount").toString())+Integer.parseInt(lcrmapList.get(i).get("dataCount").toString()));
                                    break;
                                }else{
                                    index = -1;
                                }
                            }
                            if(index == -1){
                                lcrmapList.add(map);
                            }
                        }
                    }else{
                        lcrmapList.addAll(lcrMap);
                    }

                    List<Map<String,Object>> larMap = lechengAptureRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO);
                    if(!IsEmptyUtils.isEmpty(larmapList)){
                        for(Map<String,Object> mapLar:larMap){
                            int index = -1;
                            for(int i=0;i<larmapList.size();i++){
                                String curDataLarDate = mapLar.get("createTime").toString();
                                String curDataLarAllDate = larmapList.get(i).get("createTime").toString();
                                if(curDataLarDate.equals(curDataLarAllDate)){
                                    index = i;
                                    //相同日期数据相加
                                    larmapList.get(i).put("dataCount",Integer.parseInt(mapLar.get("dataCount").toString())+Integer.parseInt(larmapList.get(i).get("dataCount").toString()));
                                    break;
                                }else{
                                    index = -1;
                                }
                            }
                            if(index == -1){
                                larmapList.add(mapLar);
                            }
                        }
                    }else{
                        larmapList.addAll(larMap);
                    }

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
                        abnormalSnapshotDTO.setSiteName(siteData.getName());
                        abnormalSnapshotDTO.setSitePhoto(siteData.getPhoto());

                        List<Map<String,Object>> lcrMap = lechengCheckRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO);
                        if(!IsEmptyUtils.isEmpty(lcrmapList)){
                            for(Map<String,Object> map:lcrMap){
                                int index = -1;
                                for(int i=0;i<lcrmapList.size();i++){
                                    String curDataLcrDate = map.get("createTime").toString();
                                    String curDataLcrAllDate = lcrmapList.get(i).get("createTime").toString();
                                    if(curDataLcrDate.equals(curDataLcrAllDate)){
                                        index = i;
                                        //相同日期数据相加
                                        lcrmapList.get(i).put("dataCount",Integer.parseInt(map.get("dataCount").toString())+Integer.parseInt(lcrmapList.get(i).get("dataCount").toString()));
                                        break;
                                    }else{
                                        index = -1;
                                    }
                                }
                                if(index == -1){
                                    lcrmapList.add(map);
                                }
                            }
                        }else{
                            lcrmapList.addAll(lcrMap);
                        }

                        List<Map<String,Object>> larMap = lechengAptureRecordMapper.findAbnormalSnapshot(abnormalSnapshotDTO);
                        if(!IsEmptyUtils.isEmpty(larmapList)){
                            for(Map<String,Object> mapLar:larMap){
                                int index = -1;
                                for(int i=0;i<larmapList.size();i++){
                                    String curDataLarDate = mapLar.get("createTime").toString();
                                    String curDataLarAllDate = larmapList.get(i).get("createTime").toString();
                                    if(curDataLarDate.equals(curDataLarAllDate)){
                                        index = i;
                                        //相同日期数据相加
                                        larmapList.get(i).put("dataCount",Integer.parseInt(mapLar.get("dataCount").toString())+Integer.parseInt(larmapList.get(i).get("dataCount").toString()));
                                        break;
                                    }else{
                                        index = -1;
                                    }
                                }
                                if(index == -1){
                                    larmapList.add(mapLar);
                                }
                            }
                        }else{
                            larmapList.addAll(larMap);
                        }
                    }
                }
            }

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        //不戴口罩
        resultBean.setRows(lcrmapList);
        //不明人员
        resultBean.setData(larmapList);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
