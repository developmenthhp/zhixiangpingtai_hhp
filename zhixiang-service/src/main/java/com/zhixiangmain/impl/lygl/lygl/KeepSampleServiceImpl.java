package com.zhixiangmain.impl.lygl.lygl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.lygl.lygl.dto.KeepSampleDTO;
import com.zhixiangmain.api.service.lygl.lygl.KeepSampleService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.lygl.lygl.KeepSampleMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.pagination.MyStartEndUtil;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.lygl.lygl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-08 18:04
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = KeepSampleService.class)
public class KeepSampleServiceImpl implements KeepSampleService {
    private static final Logger logger = LoggerFactory
            .getLogger(KeepSampleServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private KeepSampleMapper keepSampleMapper;

    @Override
    public ResultBean getPaginationData(KeepSampleDTO keepSampleDTO, Integer page, Integer limit, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();

        if(!IsEmptyUtils.isEmpty(keepSampleDTO.getTimeQuantum())){
            String[] startEnd = keepSampleDTO.getTimeQuantum().split(" - ");
            keepSampleDTO.setStartTime(startEnd[0]+" 00:00:00");
            keepSampleDTO.setEndTime(startEnd[1]+" 23:59:59");
        }

        //所有异常消毒
        /*int abnormal = 0;*/
        if(!IsEmptyUtils.isEmpty(keepSampleDTO.getSdIds())){
            String[] sdIds = keepSampleDTO.getSdIds().split(",");
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
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
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(sdId);
                    }else{
                        stringBuffer.append(","+sdId);
                    }
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取条数
                    Integer totalStatus = keepSampleMapper.findPaginationDataTotal(keepSampleDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);

                    /*KeepSampleDTO KeepSampleDTO1 = new KeepSampleDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(keepSampleDTO, KeepSampleDTO1);
                    //获取所有消毒异常条数
                    Integer totalAllAbnormal = keepSampleMapper.findPaginationDataTotal(keepSampleDTO);
                    abnormal = abnormal + totalAllAbnormal;*/
                }
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 留样分页查询：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                keepSampleDTO.setStart(startEndNums.get(i).get(0));
                keepSampleDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                keepSampleDTO.setSdId(Integer.parseInt(sourceNames[i]));
                keepSampleDTO.setSiteName(siteData.getName());
                keepSampleDTO.setSitePhoto(siteData.getPhoto());

                //获取分页数据
                List<Map<String,Object>> mainLPList = keepSampleMapper.findPaginationData(keepSampleDTO);
                mainLPLs.addAll(mainLPList);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //如果sdid是空查询该用户下所有站点 for循环取该站点数据
            List<SiteVO> userSites = siteMapper.findUserSites(keepSampleDTO.getUserId());
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
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
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(siteVO.getSdId());
                    }else{
                        stringBuffer.append(","+siteVO.getSdId());
                    }
                    logger.info("切换至--"+dataSourceName+"----数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取条数
                    Integer totalStatus = keepSampleMapper.findPaginationDataTotal(keepSampleDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);

                    /*KeepSampleDTO KeepSampleDTO1 = new KeepSampleDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(keepSampleDTO, KeepSampleDTO1);
                    //获取所有消毒异常条数
                    Integer totalAllAbnormal = keepSampleMapper.findPaginationDataTotal(keepSampleDTO);
                    abnormal = abnormal + totalAllAbnormal;*/
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 留样分页查询 未传试点id：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                keepSampleDTO.setStart(startEndNums.get(i).get(0));
                keepSampleDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                keepSampleDTO.setSdId(Integer.parseInt(sourceNames[i]));
                keepSampleDTO.setSiteName(siteData.getName());
                keepSampleDTO.setSitePhoto(siteData.getPhoto());

                //获取分页数据
                List<Map<String,Object>> mainLPList = keepSampleMapper.findPaginationData(keepSampleDTO);
                mainLPLs.addAll(mainLPList);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setRows(mainLPLs);
        resultBean.setTotal(total);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

    @Override
    public ResultBean getDetailByIdSdId(KeepSampleDTO keepSampleDTO, JSONObject jobj) {
        return null;
    }
}
