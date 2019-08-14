package com.zhixiangmain.api.service.hjjc.dmjsjb;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.hjjc.dmjsjb.dto.SlipperyAlertDTO;
import com.zhixiangmain.web.responseConfig.PageDataResult;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.service.hjjc.dmjsjb
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-26 10:51
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SlipperyAlertService {
    PageDataResult getSlipperyAlerts(SlipperyAlertDTO slipperyAlertDTO, Integer page, Integer limit, JSONObject jobj);
}
