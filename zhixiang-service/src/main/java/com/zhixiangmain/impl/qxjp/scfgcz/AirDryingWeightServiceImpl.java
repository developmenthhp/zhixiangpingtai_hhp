package com.zhixiangmain.impl.qxjp.scfgcz;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.api.service.qxjp.scfgcz.AirDryingWeightService;
import com.zhixiangmain.dao.qxjp.scfgcz.AirDryingWeightMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.qxjp.scfgcz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 12:45
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = AirDryingWeightService.class)
public class AirDryingWeightServiceImpl implements AirDryingWeightService {
    private static final Logger logger = LoggerFactory
            .getLogger(AirDryingWeightServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private AirDryingWeightMapper airDryingWeightMapper;
}
