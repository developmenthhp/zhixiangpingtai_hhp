package com.zhixiangmain.impl.rlzb.nbyg;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.rlzb.nbyg.dto.MainAccountDTO;
import com.zhixiangmain.api.service.rlzb.nbyg.MainAccountService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.base.SiteData;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.rlzb.nbyg.MainAccountMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.rlzb.nbyg
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-18 13:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = MainAccountService.class)
public class MainAccountServiceImpl implements MainAccountService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(MainAccountServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private MainAccountMapper mainAccountMapper;

    @Override
    public ResultBean getAllMainAccountBySdId(MainAccountDTO mainAccountDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        if(!IsEmptyUtils.isEmpty(mainAccountDTO.getSdIds())){
            String[] sdIds = mainAccountDTO.getSdIds().split(",");
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
                    mainAccountDTO.setSdId(Integer.parseInt(sdId));
                    mainAccountDTO.setSiteName(siteData.getName());
                    mainAccountDTO.setSitePhoto(siteData.getPhoto());
                    /*if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getIdSdId())){
                        foodBaseDTO.setId(Integer.parseInt(foodBaseDTO.getIdSdId().split("-")[0]));
                    }
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getMainCategoryIdSdId())){
                        foodBaseDTO.setMainCategoryId(Integer.parseInt(foodBaseDTO.getMainCategoryIdSdId().split("-")[0]));
                    }
                    if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSmallCategoryIdSdId())){
                        foodBaseDTO.setSmallCategoryId(Integer.parseInt(foodBaseDTO.getSmallCategoryIdSdId().split("-")[0]));
                    }*/
                    List<Map<String,Object>> categoryBases = mainAccountMapper.findAllMainAccountBySdId(mainAccountDTO);
                    mainLPLs.addAll(categoryBases);
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //long start = System.currentTimeMillis();
            //查询该用户所拥有的站点权限
            List<SiteVO> userSites = siteMapper.findUserSites(mainAccountDTO.getUserId());
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
                        mainAccountDTO.setSdId(siteVO.getSdId());
                        mainAccountDTO.setSiteName(siteData.getName());
                        mainAccountDTO.setSitePhoto(siteData.getPhoto());
                        /*if(!IsEmptyUtils.isEmpty(foodBaseDTO.getIdSdId())){
                            foodBaseDTO.setId(Integer.parseInt(foodBaseDTO.getIdSdId().split("-")[0]));
                        }
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getMainCategoryIdSdId())){
                            foodBaseDTO.setMainCategoryId(Integer.parseInt(foodBaseDTO.getMainCategoryIdSdId().split("-")[0]));
                        }
                        if(!IsEmptyUtils.isEmpty(foodBaseDTO.getSmallCategoryIdSdId())){
                            foodBaseDTO.setSmallCategoryId(Integer.parseInt(foodBaseDTO.getSmallCategoryIdSdId().split("-")[0]));
                        }*/
                        List<Map<String,Object>> categoryBases = mainAccountMapper.findAllMainAccountBySdId(mainAccountDTO);
                        mainLPLs.addAll(categoryBases);
                    }
                }
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }

        resultBean.setRows(mainLPLs);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
