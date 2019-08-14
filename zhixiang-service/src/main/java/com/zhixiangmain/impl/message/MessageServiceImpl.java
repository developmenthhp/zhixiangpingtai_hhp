package com.zhixiangmain.impl.message;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Lists;
import com.zhixiangmain.Base.IsEmptyUtils;
import com.zhixiangmain.api.module.message.vo.MessageVO;
import com.zhixiangmain.api.service.message.MessageService;
import com.zhixiangmain.component.Sender;
import com.zhixiangmain.config.DynamicDataSourceContextHolder;
import com.zhixiangmain.module.site.vo.SiteVO;
import com.zhixiangmain.web.responseConfig.IStatusMessage;
import com.zhixiangmain.web.responseConfig.ResultBean;
import com.zhixiangmain.dao.cgcc.scycg.MainLibraryPurchaseMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.message
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-19 17:03
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = MessageService.class)
public class MessageServiceImpl implements MessageService{
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(MessageServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private MainLibraryPurchaseMapper mainLibraryPurchaseMapper;
    @Autowired
    private Sender sender;

    @Override
    public ResultBean getAllShowMSG(Integer userId, String sdIds,JSONObject jobj) {
        ResultBean resultBean = new ResultBean();
        List<MessageVO> messages = Lists.newArrayList();
        Integer total = 0;
        if(!IsEmptyUtils.isEmpty(sdIds)){
            boolean mainQueryFlag = false;
            String[] sdIdArray = sdIds.split(",");
            for(String sdId:sdIdArray){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    dataSourceName = jobj.get(sdId).toString();
                }

                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至--"+dataSourceName+"--数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取提交的未审核的采购单
                    List<MessageVO> messageVOS = mainLibraryPurchaseMapper.findAllByStatus(Integer.parseInt(sdId));
                    messages.addAll(messageVOS);
                    Integer totalStatus = mainLibraryPurchaseMapper.findAllTotalByStatus(Integer.parseInt(sdId));
                    total = total + totalStatus;
                }/*else{
                        if(!mainQueryFlag){

                        }
                    }*/
                mainQueryFlag = true;

            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            //如果sdid是空查询该用户下所有站点 for循环取该站点数据
            List<SiteVO> userSites = siteMapper.findUserSites(userId);
            //给为空查询主数据源一个标记，为空第一次查询完就不查询
            boolean mainQueryFlag = false;
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(jobj.get(siteVO.getSdId().toString()))){
                    dataSourceName = jobj.get(siteVO.getSdId().toString()).toString();
                }
                //如果为空，那么就说明没有配数据源或者总公司没有自己数据库
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("切换至"+dataSourceName+"数据源");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    //获取提交的未审核的采购单
                    List<MessageVO> messageVOS = mainLibraryPurchaseMapper.findAllByStatus(siteVO.getSdId());
                    messages.addAll(messageVOS);
                    Integer totalStatus = mainLibraryPurchaseMapper.findAllTotalByStatus(siteVO.getSdId());
                    total = total + totalStatus;
                }
                mainQueryFlag = true;
            }
            //切换回默认数据源
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setTotal(total);
        resultBean.setRows(messages);
        resultBean.setFlag(true);
        resultBean.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }

}
