package com.zhixiangmain.api.service.rlzb.aqjc;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.rlzb.aqjc.dto.FireSafetyInspectionDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.service.rlzb.aqjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-03 13:46
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FireSafetyInspectionService {
    ResultBean getByMonth(FireSafetyInspectionDTO fireSafetyInspectionDTO, JSONObject jobj);
}
