package com.zhixiangmain.impl.cgcc.sckc;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.StockOMaterials.dto.LibrarySearchDTO;
import com.zhixiangmain.api.service.cgcc.sckc.LibraryService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cgcc.sckc.LibraryMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.pagination.MyStartEndUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.impl.cgcc.sckc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-01 8:40
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = LibraryService.class)
public class LibraryServiceImpl implements LibraryService{
    private static final Logger logger = LoggerFactory
            .getLogger(LibraryServiceImpl.class);
    @Autowired
    private LibraryMapper libraryMapper;
    @Autowired
    private SiteMapper siteMapper;

    @Override
    public ResultBean getLibraries(LibrarySearchDTO librarySearchDTO, Integer page, Integer limit, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        if (IsEmptyUtils.isEmpty(page)) {
            page = 1;
        }
        if (IsEmptyUtils.isEmpty(limit)) {
            limit = 10;
        }

        if(!IsEmptyUtils.isEmpty(librarySearchDTO.getIngBaseIdSdId())){
            librarySearchDTO.setIngredientBaseId(Integer.parseInt(librarySearchDTO.getIngBaseIdSdId().split("-")[0]));
        }
        if(!IsEmptyUtils.isEmpty(librarySearchDTO.getMainCategoryIdSdId())){
            librarySearchDTO.setMainCategoryId(Integer.parseInt(librarySearchDTO.getMainCategoryIdSdId().split("-")[0]));
        }
        if(!IsEmptyUtils.isEmpty(librarySearchDTO.getSmallCategoryIdSdId())){
            librarySearchDTO.setSmallCategoryId(Integer.parseInt(librarySearchDTO.getSmallCategoryIdSdId().split("-")[0]));
        }

        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(librarySearchDTO.getSdIds())){
            String[] sdIds = librarySearchDTO.getSdIds().split(",");
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
                        stringBuffer.append(dataSourceName+"-"+siteData.getName()+"-"+siteData.getPhoto());
                    }else{
                        stringBuffer.append(","+dataSourceName+"-"+siteData.getName()+"-"+siteData.getPhoto());
                    }
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取采购单
                    Integer totalStatus = libraryMapper.findLibrariesTotal(librarySearchDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            //这里也是按照siteJson的顺序取的，由于取够了就不再取，故可以按照这个来for循环,也可没必要再切换数据源
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                Integer selectStart = startEndNums.get(i).get(0);
                Integer selectCount = startEndNums.get(i).get(1)-startEndNums.get(i).get(0);
                if(selectCount==0){
                    continue;
                }
                logger.info("传了sdId 预采购分页查询：切换至--"+sourceNames[i]+"--数据源");
                String[] siteMsgs = sourceNames[i].split("-");
                DynamicDataSourceContextHolder.setDataSourceType(siteMsgs[0]);
                librarySearchDTO.setStart(selectStart);
                librarySearchDTO.setCount(selectCount);
                librarySearchDTO.setSiteName(siteMsgs[1]);
                librarySearchDTO.setSitePhoto(siteMsgs[2]);
                //获取采购单
                List<Map<String,Object>> mainLPList = libraryMapper.findLibraries(librarySearchDTO);
                mainLPLs.addAll(mainLPList);
            }
            /*for(int i=0;i<sourceNames.length;i++){
                Integer selectStart = startEndNums.get(i).get(0);
                Integer selectCount = startEndNums.get(i).get(1)-startEndNums.get(i).get(0);
                if(selectCount==0){
                    continue;
                }
                logger.info("传了sdId 预采购分页查询：切换至--"+sourceNames[i]+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(sourceNames[i]);
                librarySearchDTO.setStart(selectStart);
                librarySearchDTO.setCount(selectCount);
                //获取采购单
                List<Map<String,Object>> mainLPList = libraryMapper.findLibraries(librarySearchDTO);
                mainLPLs.addAll(mainLPList);
            }*/
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(librarySearchDTO.getUserId());
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
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
                        if(IsEmptyUtils.isEmpty(stringBuffer)){
                            stringBuffer.append(dataSourceName+"-"+siteData.getName()+"-"+siteData.getPhoto());
                        }else{
                            stringBuffer.append(","+dataSourceName+"-"+siteData.getName()+"-"+siteData.getPhoto());
                        }
                        logger.info("切换至--"+dataSourceName+"----数据源");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                        //获取采购单
                        Integer totalStatus = libraryMapper.findLibrariesTotal(librarySearchDTO);
                        total = total + totalStatus;
                        totalAll.add(totalStatus);
                    }
                }

            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                Integer selectStart = startEndNums.get(i).get(0);
                Integer selectCount = startEndNums.get(i).get(1)-startEndNums.get(i).get(0);
                if(selectCount==0){
                    continue;
                }
                logger.info("预采购分页查询：切换至--"+sourceNames[i]+"--数据源");
                String[] siteMsgs = sourceNames[i].split("-");
                DynamicDataSourceContextHolder.setDataSourceType(siteMsgs[0]);
                librarySearchDTO.setStart(selectStart);
                librarySearchDTO.setCount(selectCount);
                librarySearchDTO.setSiteName(siteMsgs[1]);
                librarySearchDTO.setSitePhoto(siteMsgs[2]);
                //获取采购单
                List<Map<String,Object>> mainLPList = libraryMapper.findLibraries(librarySearchDTO);
                mainLPLs.addAll(mainLPList);

                //这里速度提升没差别，另参数处理问题待解决
                    /*int num = 5;//每次查询的条数
                    //需要查询的次数
                    int times=selectCount / num;
                    if(selectCount%num !=0) {
                        times=times+1;
                    }

                    List<InvokeMethodEntity> mapperMethods = new ArrayList<>();
                    mapperMethods.add(new InvokeMethodEntity("com.zhixiangyun.dao.hjjc.dmjsjb.SlipperyAlertMapper","findSlipperyAlerts",map));

                    List<Callable<List<Map<String, Object>>>> tasks = Lists.newArrayList();//添加任务
                    for(int j = 0; j <times ; j++){
                        if(j==times-1){
                            if(selectCount%num !=0) {
                                num = selectCount%num;
                            }
                        }
                        System.out.println(selectStart);
                        Callable<List<Map<String, Object>>> qfe = new ThredQuery(mapperMethods,sqlSession,selectStart,num);
                        tasks.add(qfe);
                        selectStart=selectStart+num;
                    }
                    //定义固定长度的线程池  防止线程过多
                    ExecutorService executorService = Executors.newFixedThreadPool(15);
                    List<Future<List<Map<String, Object>>>> futures = executorService.invokeAll(tasks);
                    // 处理线程返回结果
                    if (futures != null && futures.size() > 0) {
                        for(Future<List<Map<String, Object>>> future : futures) {
                            mainLPLs.addAll(future.get());
                        }
                    }
                    executorService.shutdown();  // 关闭线程池*/
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
                /*long end = System.currentTimeMillis();
                System.out.println("用时"+(end-start)+",,,,,,,,,,,,,,,,,---------------------------------------------------");*/
        }
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setRows(mainLPLs);
        resultBean.setTotal(total);
        return resultBean;
    }

}
