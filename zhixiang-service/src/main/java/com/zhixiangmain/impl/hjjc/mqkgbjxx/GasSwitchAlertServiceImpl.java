package com.zhixiangmain.impl.hjjc.mqkgbjxx;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.api.service.hjjc.mqkgbjxx.GasSwitchAlertService;
import com.zhixiangmain.dao.hjjc.mqkgbjxx.GasSwitchAlertMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.hjjc.mqkgbjxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 17:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = GasSwitchAlertService.class)
public class GasSwitchAlertServiceImpl implements GasSwitchAlertService {
    private static final Logger logger = LoggerFactory
            .getLogger(GasSwitchAlertServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private GasSwitchAlertMapper gasSwitchAlertMapper;
}
