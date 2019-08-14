package com.zhixiangmain.impl.jygs.gszz;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.api.service.jygs.gszz.BusinessService;
import com.zhixiangmain.dao.jygs.gszz.BusinessMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.impl.jygs.gszz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-07 15:32
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = BusinessService.class)
public class BusinessServiceImpl implements BusinessService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(BusinessServiceImpl.class);
    @Autowired
    private BusinessMapper businessMapper;
}
