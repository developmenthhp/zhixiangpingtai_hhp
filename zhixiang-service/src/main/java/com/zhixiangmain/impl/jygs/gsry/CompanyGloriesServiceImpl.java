package com.zhixiangmain.impl.jygs.gsry;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.jygs.gsry.dto.CompanyGloriesDTO;
import com.zhixiangmain.api.service.jygs.gsry.CompanyGloriesService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.jygs.gsry.CompanyGloriesMapper;
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
 * @Package com.zhixiangyun.impl.jygs.gsry
 * @Description: 公司荣誉
 * @author: hhp
 * @date: 2019-04-03 10:57
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CompanyGloriesService.class)
public class CompanyGloriesServiceImpl implements CompanyGloriesService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(CompanyGloriesServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CompanyGloriesMapper companyGloriesMapper;

    @Override
    public ResultBean getCompanyGlories(CompanyGloriesDTO companyGloriesDTO, Integer page, Integer limit,JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
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
        if(!IsEmptyUtils.isEmpty(companyGloriesDTO.getSdIds())){
            String[] sdIds = companyGloriesDTO.getSdIds().split(",");
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    dataSourceName = jobj.get(sdId).toString();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(dataSourceName);
                    }else{
                        stringBuffer.append(","+dataSourceName);
                    }
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取采购单
                    Integer totalStatus = companyGloriesMapper.findCompanyGloriesTotal(companyGloriesDTO);
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
                logger.info("传了sdId 公司荣誉分页查询：切换至--"+sourceNames[i]+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(sourceNames[i]);
                companyGloriesDTO.setStart(selectStart);
                companyGloriesDTO.setCount(selectCount);
                //获取采购单
                List<Map<String,Object>> mainLPList = companyGloriesMapper.findCompanyGlories(companyGloriesDTO);
                mainLPLs.addAll(mainLPList);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(companyGloriesDTO.getUserId());
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj)){
                    if(!IsEmptyUtils.isEmpty(jobj.get(siteVO.getSdId().toString()))){
                        dataSourceName = jobj.get(siteVO.getSdId().toString()).toString();
                    }
                    //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                    if(!IsEmptyUtils.isEmpty(dataSourceName)){
                        if(IsEmptyUtils.isEmpty(stringBuffer)){
                            stringBuffer.append(dataSourceName);
                        }else{
                            stringBuffer.append(","+dataSourceName);
                        }
                        logger.info("切换至--"+dataSourceName+"----数据源");
                        DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                        //获取采购单
                        Integer totalStatus = companyGloriesMapper.findCompanyGloriesTotal(companyGloriesDTO);
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
                logger.info("公司荣誉分页查询：切换至--"+sourceNames[i]+"--数据源");
                DynamicDataSourceContextHolder.setDataSourceType(sourceNames[i]);
                companyGloriesDTO.setStart(selectStart);
                companyGloriesDTO.setCount(selectCount);
                //获取采购单
                List<Map<String,Object>> mainLPList = companyGloriesMapper.findCompanyGlories(companyGloriesDTO);
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
