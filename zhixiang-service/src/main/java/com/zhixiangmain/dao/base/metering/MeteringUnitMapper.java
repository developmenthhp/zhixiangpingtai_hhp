package com.zhixiangmain.dao.base.metering;

import com.zhixiangmain.module.base.metering.MeteringUnit;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.base.metering
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-26 16:21
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface MeteringUnitMapper {
    List<Map<String,Object>> findAllMeteringUnit(MeteringUnit meteringUnit);
}
