package com.zhixiangmain.impl.cgcc.scycg;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.scycg.MainLibraryPurchase;
import com.zhixiangmain.api.module.cgcc.scycg.dto.MainLibraryPurchaseDTO;
import com.zhixiangmain.api.service.cgcc.scycg.MainLibraryPurchaseService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.PageDataResult;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cgcc.sccg.LibraryPurchaseMapper;
import com.zhixiangmain.dao.cgcc.scycg.MainLibraryPurchaseMapper;
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
 * @Package com.zhixiangyun.impl.cgcc.scycg
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-19 16:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = MainLibraryPurchaseService.class)
public class MainLibraryPurchaseServiceImpl implements MainLibraryPurchaseService{
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(MainLibraryPurchaseServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private MainLibraryPurchaseMapper mainLibraryPurchaseMapper;
    @Autowired
    private LibraryPurchaseMapper libraryPurchaseMapper;

    @Override
    public PageDataResult getMainLPList(MainLibraryPurchaseDTO mainLibraryPurchaseDTO, Integer page, Integer limit,JSONObject jobj) {
        PageDataResult pdr = new PageDataResult();
        if (null == page) {
            page = 1;
        }
        if (null == limit) {
            limit = 10;
        }
            /*List<Map<String,Object>> mainLPList = mainLibraryPurchaseMapper.findMainLPList(mainLibraryPurchaseDTO);
            // 获取分页查询后的数据
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(mainLPList);
            // 设置获取到的总记录数total：
            pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
            pdr.setList(mainLPList);
            pdr.setCode(Integer.parseInt(IStatusMessage.SystemStatus.SUCCESS.getCode()));
            logger.debug("获取预采购信息列表!=pdr:" + pdr);*/

            /*List<InvokeMethodEntity> mapperMethods = new ArrayList<>();
            mapperMethods.add(new InvokeMethodEntity(mainLibraryPurchaseMapper,"findMainLPList",null));
            *//** 线程查询start*//*
            long start = System.currentTimeMillis();
            List<Map<String, Object>> result=new ArrayList<>();//返回结果

            int count = 10;//mydao.getCount(); 通过count查到数据总量

            int num = 1;//每次查询的条数
            //需要查询的次数
            int times=count / num;
            if(count%num !=0) {
                times=times+1;
            }
            //开始查询的行数
            int bindex = 0;

            List<Callable<List<Map<String, Object>>>> tasks = new ArrayList<Callable<List<Map<String, Object>>>>();//添加任务
            for(int i = 0; i <times ; i++){
                Callable<List<Map<String, Object>>> qfe = new ThredQuery(mapperMethods,"ss",bindex, num);
                tasks.add(qfe);
                bindex=bindex+num;
            }
            //定义固定长度的线程池  防止线程过多
            ExecutorService executorService = Executors.newFixedThreadPool(15);

            List<Future<List<Map<String, Object>>>> futures = executorService.invokeAll(tasks);

            // 处理线程返回结果
            if (futures != null && futures.size() > 0) {
                for(Future<List<Map<String, Object>>> future : futures) {
                    result.addAll(future.get());
                }
            }
            executorService.shutdown();  // 关闭线程池

            long end = System.currentTimeMillis();
            System.out.println("用时"+(end-start+",,,,,,,,,,,,,,,,,---------------------------------------------------"));
            *//**线程查询end*/
        if(!IsEmptyUtils.isEmpty(mainLibraryPurchaseDTO)){
            if(!IsEmptyUtils.isEmpty(mainLibraryPurchaseDTO.getTotalStation())){
                mainLibraryPurchaseDTO.setTotalStation(Integer.parseInt(getTrueBillsNum(mainLibraryPurchaseDTO.getTotalStation().toString())));
            }
        }
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(mainLibraryPurchaseDTO.getSdIds())){
            String[] sdIds = mainLibraryPurchaseDTO.getSdIds().split(",");
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
                    Integer totalStatus = mainLibraryPurchaseMapper.findMainLPListTotal(mainLibraryPurchaseDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                logger.info("传了sdId 预采购分页查询：切换至--"+sourceNames[i]+"--数据源");
                String[] siteMsgs = sourceNames[i].split("-");
                DynamicDataSourceContextHolder.setDataSourceType(siteMsgs[0]);
                mainLibraryPurchaseDTO.setStart(startEndNums.get(i).get(0));
                mainLibraryPurchaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                mainLibraryPurchaseDTO.setSiteName(siteMsgs[1]);
                mainLibraryPurchaseDTO.setSitePhoto(siteMsgs[2]);
                //获取采购单
                List<Map<String,Object>> mainLPList = mainLibraryPurchaseMapper.findMainLPList(mainLibraryPurchaseDTO);
                mainLPLs.addAll(mainLPList);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //如果sdid是空查询该用户下所有站点 for循环取该站点数据
            List<SiteVO> userSites = siteMapper.findUserSites(mainLibraryPurchaseDTO.getUserId());
            //用做查询list的dataSourceName
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                SiteData siteData = null;
                String sdIdStr = siteVO.getSdId().toString();
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
                    Integer totalStatus = mainLibraryPurchaseMapper.findMainLPListTotal(mainLibraryPurchaseDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            //查询数据
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                logger.info("预采购分页查询：切换至--"+sourceNames[i]+"--数据源");
                String[] siteMsgs = sourceNames[i].split("-");
                DynamicDataSourceContextHolder.setDataSourceType(siteMsgs[0]);
                mainLibraryPurchaseDTO.setStart(startEndNums.get(i).get(0));
                mainLibraryPurchaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                mainLibraryPurchaseDTO.setSiteName(siteMsgs[1]);
                mainLibraryPurchaseDTO.setSitePhoto(siteMsgs[2]);
                //获取采购单
                List<Map<String,Object>> mainLPList = mainLibraryPurchaseMapper.findMainLPList(mainLibraryPurchaseDTO);
                mainLPLs.addAll(mainLPList);
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        pdr.setList(mainLPLs);
        pdr.setTotals(total);
        return pdr;
    }

    @Override
    public ResultBean updateStatusByIdSdId(MainLibraryPurchase mainLibraryPurchase,String[] authorizedPrices,JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(mainLibraryPurchase)){
            if(!IsEmptyUtils.isEmpty(mainLibraryPurchase.getSdId())){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj.get(mainLibraryPurchase.getSdId().toString()))){
                    dataSourceName = jobj.get(mainLibraryPurchase.getSdId().toString()).toString();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //更新审核结果
                    if(mainLibraryPurchase.getStatus()==3){
                        mainLibraryPurchase.setReason(null);
                    }
                    mainLibraryPurchaseMapper.updateStatusByIdSdId(mainLibraryPurchase);
                    String msg = "";
                    if(!IsEmptyUtils.isEmpty(authorizedPrices)){
                        for(String authorizedPrice:authorizedPrices){
                            //idPrice.push(idSdIdMainId[0]+","+val.price+","+idSdIdMainId[1]);
                            String[] idSdIdMainId = authorizedPrice.split("-");
                            libraryPurchaseMapper.updateAuthorizedPriceBySdIdId(idSdIdMainId[0],idSdIdMainId[1],idSdIdMainId[2],idSdIdMainId[3]);
                        }
                        msg = "并更新核准单价";
                    }

                    resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                    resultBean.setMsg("提交审核"+msg+"成功");

                    //切换回默认数据源
                    DynamicDataSourceContextHolder.setDataSourceType(null);
                }
            }else{
                resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
                resultBean.setMsg("请传入正确数据");
            }
        }else{
            resultBean.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg("请传入正确数据");
        }
        return resultBean;
    }

    public String getTrueBillsNum(String billsNum){
        char[] billsNumArray = billsNum.toCharArray();
        String trueBillNum = "";
        //共有11位
        if(billsNumArray.length<=6){
            for(int i=0;i<billsNumArray.length;i++){
                int int_data=(int)(billsNumArray[i]-'0');
                if(int_data>0){
                    trueBillNum = billsNum.substring(i);
                    break;
                }
            }
        }else{
            trueBillNum = billsNum;
        }
        return trueBillNum;
    }
}
