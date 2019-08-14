package com.zhixiangmain.impl.base.metering;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhixiangmain.api.service.base.metering.MeteringUnitService;
import com.zhixiangmain.dao.base.metering.MeteringUnitMapper;
import com.zhixiangmain.module.base.metering.MeteringUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.impl.base.metering
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-26 16:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = MeteringUnitService.class)
public class MeteringUnitServiceImpl implements MeteringUnitService {
    private static final Logger logger = LoggerFactory
            .getLogger(MeteringUnitServiceImpl.class);
    @Autowired
    private MeteringUnitMapper meteringUnitMapper;
    @Override
    public List<Map<String, Object>> getAllMeteringUnit(MeteringUnit meteringUnit) {
        meteringUnit.setDeleteStatus("1");
        return meteringUnitMapper.findAllMeteringUnit(meteringUnit);
    }
}
