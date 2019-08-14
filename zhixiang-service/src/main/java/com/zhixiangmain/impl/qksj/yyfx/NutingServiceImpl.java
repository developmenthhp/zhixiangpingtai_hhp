package com.zhixiangmain.impl.qksj.yyfx;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.qksj.yyfx.dto.NutingDTO;
import com.zhixiangmain.api.module.qksj.yyfx.vo.NutingVO;
import com.zhixiangmain.api.service.qksj.yyfx.NutingService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.qksj.yyfx.NutingMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.pagination.MyStartEndUtil;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.qksj.yyfx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-18 11:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = NutingService.class)
public class NutingServiceImpl implements NutingService {
    private static final Logger logger = LoggerFactory
            .getLogger(NutingServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private NutingMapper nutingMapper;

    @Override
    public ResultBean getRankingListBySdId(NutingDTO nutingDTO, Integer page, Integer limit, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();
        List<NutingVO> rankingList = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(nutingDTO.getTimeQuantum())){
            String[] startEnd = nutingDTO.getTimeQuantum().split(" - ");
            nutingDTO.setStartTime(startEnd[0]+" 00:00:00");
            nutingDTO.setEndTime(startEnd[1]+" 23:59:59");
        }
        //所有食材名
        if(!IsEmptyUtils.isEmpty(nutingDTO.getSdIds())){
            String[] sdIds = nutingDTO.getSdIds().split(",");
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
                    Integer totalStatus = nutingMapper.findPaginationDataTotal(nutingDTO);
                    if(IsEmptyUtils.isEmpty(totalStatus)){
                        totalStatus = 0;
                    }
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 留样分页查询：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                nutingDTO.setStart(startEndNums.get(i).get(0));
                nutingDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                nutingDTO.setSdId(Integer.parseInt(sourceNames[i]));
                nutingDTO.setSiteName(siteData.getName());
                nutingDTO.setSitePhoto(siteData.getPhoto());
                //获取分页数据
                List<NutingVO> nutingVOS  = nutingMapper.findRankingListBySdId(nutingDTO);
                rankingList.addAll(nutingVOS);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //如果sdid是空查询该用户下所有站点 for循环取该站点数据
            List<SiteVO> userSites = siteMapper.findUserSites(nutingDTO.getUserId());
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
                    Integer totalStatus = nutingMapper.findPaginationDataTotal(nutingDTO);
                    if(IsEmptyUtils.isEmpty(totalStatus)){
                        totalStatus = 0;
                    }
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){

                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);

                logger.info("传了sdId 留样分页查询 未传试点id：切换至--"+siteData.getDateSourceName()+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                nutingDTO.setStart(startEndNums.get(i).get(0));
                nutingDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));

                //获取分页数据
                List<NutingVO> nutingVOS  = nutingMapper.findRankingListBySdId(nutingDTO);
                rankingList.addAll(nutingVOS);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        Collections.sort(rankingList);
        resultBean.setRows(rankingList);
        resultBean.setTotal(total);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
