package com.zhixiangmain.api.service.alertSummary;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.service.alertSummary
 * @Description: 提示显示查询业务层
 * @author: hhp
 * @date: 2019-01-17 14:58
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface AlertSummaryService {
    ResultBean getFirstAlert(int userId, String sdId, String type, JSONObject jobj);
}
