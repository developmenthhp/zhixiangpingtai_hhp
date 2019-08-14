package com.zhixiangmain.api.service.yj.yj;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.web.responseConfig.EarlyWarningResult;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.yj.yj
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-11 16:58
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface EarlyWarningService {
    EarlyWarningResult getEWMonthBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj);
}
