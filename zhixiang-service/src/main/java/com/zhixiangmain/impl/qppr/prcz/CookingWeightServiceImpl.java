package com.zhixiangmain.impl.qppr.prcz;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.api.service.qppr.prcz.CookingWeightService;
import com.zhixiangmain.dao.qppr.prcz.CookingWeightMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.qppr.prcz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 13:15
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CookingWeightService.class)
public class CookingWeightServiceImpl implements CookingWeightService {
    private static final Logger logger = LoggerFactory
            .getLogger(CookingWeightServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CookingWeightMapper cookingWeightMapper;
}
