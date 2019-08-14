package com.zhixiangmain.impl.lygl.lyglxq;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.lygl.lyglxq.dto.KeepSampleDetailDTO;
import com.zhixiangmain.api.service.lygl.lyglxq.KeepSampleDetailService;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.dao.lygl.lyglxq.KeepSampleServiceDetailMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import com.zhixiangmain.module.base.SiteData;
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
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.impl.lygl.lyglxq
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-10 16:53
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = KeepSampleDetailService.class)
public class KeepSampleServiceDetailImpl implements KeepSampleDetailService {
    private static final Logger logger = LoggerFactory
            .getLogger(KeepSampleServiceDetailImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private KeepSampleServiceDetailMapper keepSampleServiceDetailMapper;

    @Override
    public ResultBean getDetailByIdSdId(KeepSampleDetailDTO keepSampleDetailDTO, JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        String dataSourceName = "";
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(keepSampleDetailDTO)){
            if(!IsEmptyUtils.isEmpty(keepSampleDetailDTO.getSdId())){
                String sdId = keepSampleDetailDTO.getSdId().toString();
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                logger.info(dataSourceName);
            }
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)){
            logger.info("切换至--"+dataSourceName+"--数据源");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            /*if(!IsEmptyUtils.isEmpty(siteData)){
                keepSampleDetailDTO.setSiteName(siteData.getName());
                keepSampleDetailDTO.setSitePhoto(siteData.getPhoto());
            }*/
            List<Map<String,Object>> mainLPList = keepSampleServiceDetailMapper.findDetailByIdSdId(keepSampleDetailDTO);
            resultBean.setRows(mainLPList);

            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
}
