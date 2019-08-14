package com.zhixiangmain.impl.qxjp.scqxcz;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.api.service.qxjp.scqxcz.CleanWeightService;
import com.zhixiangmain.dao.qxjp.scqxcz.CleanWeightMapper;
import com.zhixiangmain.dao.site.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.impl.qxjp.scqxcz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 12:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CleanWeightService.class)
public class CleanWeightServiceImpl implements CleanWeightService {
    private static final Logger logger = LoggerFactory
            .getLogger(CleanWeightServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CleanWeightMapper cleanWeightMapper;
}
