package com.zhixiangmain.impl.zjbb.zjbb;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.cgcc.scrk.dto.IwareReportDTO;
import com.zhixiangmain.api.module.disinfectionRcd.dto.DisinfectionRcdDTO;
import com.zhixiangmain.api.module.jybb.hplldsq.dto.IwareOutDTO;
import com.zhixiangmain.api.module.jybb.kcclbbsq.dto.IwarekCBBDTO;
import com.zhixiangmain.api.module.jybb.kcpdbbsq.dto.IwarekCPDDTO;
import com.zhixiangmain.api.service.zjbb.zjbb.SelfCheckService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.cgcc.sckc.LibraryMapper;
import com.zhixiangmain.dao.cgcc.scrk.IwareReportMapper;
import com.zhixiangmain.dao.jybb.hplldsq.IwareOutMapper;
import com.zhixiangmain.dao.jybb.kcclbbsq.IwarekCBBMapper;
import com.zhixiangmain.dao.jybb.kcpdbbsq.IwarekCPDMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.zjbb.zjbb
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-13 10:57
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = SelfCheckService.class)
public class SelfCheckServiceImpl implements SelfCheckService {
    private static final Logger logger = LoggerFactory
            .getLogger(SelfCheckServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private IwareReportMapper iwareReportMapper;
    @Autowired
    private IwareOutMapper iwareOutMapper;
    /*@Autowired
    private LibraryMapper libraryMapper;*/
    @Autowired
    private IwarekCBBMapper iwarekCBBMapper;
    @Autowired
    private IwarekCPDMapper iwarekCPDMapper;

    @Override
    public ResultBean getTotalBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        //所有报表总数存放list
        List<Integer> allTotalList = Lists.newArrayList();
        int irTotal = 0;
        int ioTotal = 0;
        int icbbTotal = 0;
        int ikTotal = 0;
        if(!IsEmptyUtils.isEmpty(baseEntityDTO.getSdIds())){
            String[] sdIds = baseEntityDTO.getSdIds().split(",");
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
                    baseEntityDTO.setSdId(Integer.parseInt(sdId));

                    IwareReportDTO iwareReportDTO = new IwareReportDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, iwareReportDTO);
                    //货品入库单总数
                    Integer irCount = iwareReportMapper.findTotalBySdIdStatus(iwareReportDTO);
                    irTotal = irTotal + irCount;

                    IwareOutDTO iwareOutDTO = new IwareOutDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, iwareOutDTO);
                    //货品领料单
                    Integer ioCount  = iwareOutMapper.findTotalBySdIdStatus(iwareOutDTO);
                    ioTotal = ioTotal + ioCount;

                    IwarekCBBDTO iwarekCBBDTO = new IwarekCBBDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, iwarekCBBDTO);
                    //货品领料单
                    Integer icbbCount  = iwarekCBBMapper.findTotalBySdIdStatus(iwarekCBBDTO);
                    icbbTotal = icbbTotal + icbbCount;

                    IwarekCPDDTO iwarekCPDDTO = new IwarekCPDDTO();
                    //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                    BeanUtils.copyProperties(baseEntityDTO, iwarekCPDDTO);
                    //货品领料单
                    Integer ikCount  = iwarekCPDMapper.findTotalBySdIdStatus(iwarekCPDDTO);
                    ikTotal = ikTotal + ikCount;

                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(baseEntityDTO.getUserId());
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
                        baseEntityDTO.setSdId(siteVO.getSdId());

                        IwareReportDTO iwareReportDTO = new IwareReportDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, iwareReportDTO);
                        //货品入库单总数
                        Integer irCount = iwareReportMapper.findTotalBySdIdStatus(iwareReportDTO);
                        irTotal = irTotal + irCount;

                        IwareOutDTO iwareOutDTO = new IwareOutDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, iwareOutDTO);
                        //货品领料单
                        Integer ioCount  = iwareOutMapper.findTotalBySdIdStatus(iwareOutDTO);
                        ioTotal = ioTotal + ioCount;

                        IwarekCBBDTO iwarekCBBDTO = new IwarekCBBDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, iwarekCBBDTO);
                        //货品领料单
                        Integer icbbCount  = iwarekCBBMapper.findTotalBySdIdStatus(iwarekCBBDTO);
                        icbbTotal = icbbTotal + icbbCount;

                        IwarekCPDDTO iwarekCPDDTO = new IwarekCPDDTO();
                        //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
                        BeanUtils.copyProperties(baseEntityDTO, iwarekCPDDTO);
                        //货品领料单
                        Integer ikCount  = iwarekCPDMapper.findTotalBySdIdStatus(iwarekCPDDTO);
                        ikTotal = ikTotal + ikCount;
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        allTotalList.add(irTotal);
        allTotalList.add(ioTotal);
        allTotalList.add(icbbTotal);
        allTotalList.add(ikTotal);
        resultBean.setData(allTotalList);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
