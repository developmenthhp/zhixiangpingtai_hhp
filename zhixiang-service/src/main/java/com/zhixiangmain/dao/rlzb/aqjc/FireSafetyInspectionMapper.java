package com.zhixiangmain.dao.rlzb.aqjc;

import com.zhixiangmain.api.module.rlzb.aqjc.dto.FireSafetyInspectionDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.dao.rlzb.aqjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-03 13:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FireSafetyInspectionMapper {
    List<Map<String,Object>> findByMonth(FireSafetyInspectionDTO fireSafetyInspectionDTO);
}
