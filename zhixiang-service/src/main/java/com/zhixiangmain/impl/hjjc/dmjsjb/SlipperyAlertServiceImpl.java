package com.zhixiangmain.impl.hjjc.dmjsjb;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.hjjc.dmjsjb.dto.SlipperyAlertDTO;
import com.zhixiangmain.api.service.hjjc.dmjsjb.SlipperyAlertService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.PageDataResult;
import com.zhixiangmain.dao.hjjc.dmjsjb.SlipperyAlertMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.pagination.MyStartEndUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.hjjc.dmjsjb
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-26 10:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = SlipperyAlertService.class)
public class SlipperyAlertServiceImpl implements SlipperyAlertService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(SlipperyAlertServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private SlipperyAlertMapper slipperyAlertMapper;
    /*//自动注入SqlSession
    @Autowired
    private SqlSession sqlSession;*/

    @Override
    public PageDataResult getSlipperyAlerts(SlipperyAlertDTO slipperyAlertDTO, Integer page, Integer limit,JSONObject jobj1) {
        PageDataResult pdr = new PageDataResult();
        if (IsEmptyUtils.isEmpty(page)) {
            page = 1;
        }
        if (IsEmptyUtils.isEmpty(limit)) {
            limit = 10;
        }

        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(slipperyAlertDTO.getSdIds())){
            String[] sdIds = slipperyAlertDTO.getSdIds().split(",");
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj1.get(sdId))){
                    siteData = JSON.parseObject(jobj1.get(sdId).toString(),SiteData.class);
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
                    Integer totalStatus = slipperyAlertMapper.findSlipperyAlertTotal(slipperyAlertDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
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
                logger.info("传了sdId 预采购分页查询：切换至--"+sourceNames[i]+"--数据源");
                String[] siteMsgs = sourceNames[i].split("-");
                DynamicDataSourceContextHolder.setDataSourceType(siteMsgs[0]);
                slipperyAlertDTO.setStart(selectStart);
                slipperyAlertDTO.setCount(selectCount);
                slipperyAlertDTO.setSiteName(siteMsgs[1]);
                slipperyAlertDTO.setSitePhoto(siteMsgs[2]);
                //获取采购单
                List<Map<String,Object>> mainLPList = slipperyAlertMapper.findSlipperyAlerts(slipperyAlertDTO);
                mainLPLs.addAll(mainLPList);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(slipperyAlertDTO.getUserId());
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj1)){
                    SiteData siteData = null;
                    String sdIdStr = siteVO.getSdId().toString();
                    if(!IsEmptyUtils.isEmpty(jobj1.get(sdIdStr))){
                        siteData = JSON.parseObject(jobj1.get(sdIdStr).toString(),SiteData.class);
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
                        Integer totalStatus = slipperyAlertMapper.findSlipperyAlertTotal(slipperyAlertDTO);
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
                slipperyAlertDTO.setStart(selectStart);
                slipperyAlertDTO.setCount(selectCount);
                slipperyAlertDTO.setSiteName(siteMsgs[1]);
                slipperyAlertDTO.setSitePhoto(siteMsgs[2]);
                //获取采购单
                List<Map<String,Object>> mainLPList = slipperyAlertMapper.findSlipperyAlerts(slipperyAlertDTO);
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
        pdr.setList(mainLPLs);
        pdr.setTotals(total);
        return pdr;
    }

    /*public SlipperyAlert findMainLPList(){
        SlipperyAlert slipperyAlert = slipperyAlertMapper.findAll(22);
        return slipperyAlert;
    }*/

    /*public static void main(String[] args){
        List<InvokeMethodEntity> mapperMethods = new ArrayList<>();
        mapperMethods.add(new InvokeMethodEntity(new SlipperyAlertServiceImpl(),"findMainLPList",null));
        SlipperyAlert slipperyAlert = null; //myService.queryBySex(sex,bindex,num);
        for(InvokeMethodEntity invokeMethodEntity:mapperMethods){
            BaseInvokeMethod baseInvokeMethod = new BaseInvokeMethod();
            slipperyAlert = (SlipperyAlert)baseInvokeMethod.invokeMethod(invokeMethodEntity.getOwner(),invokeMethodEntity.getMethodName(),invokeMethodEntity.getArgs());
        }
        System.out.println(slipperyAlert+",,,,,,,");
    }*/
}
