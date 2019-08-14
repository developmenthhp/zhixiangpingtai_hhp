package com.zhixiangmain.api.service.zhjg.twjcgl;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.zhjg.twjcgl.TemperatureDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.service.zhjg.twjcgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-25 11:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface TemperatureService {
    ResultBean getTemperatureBySdId(TemperatureDTO temperatureDTO, JSONObject jobj);
}
