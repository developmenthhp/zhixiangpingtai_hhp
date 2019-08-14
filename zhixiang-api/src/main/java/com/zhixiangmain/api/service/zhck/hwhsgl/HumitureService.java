package com.zhixiangmain.api.service.zhck.hwhsgl;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.zhck.hwhsgl.dto.HumitureDTO;
import com.zhixiangmain.module.base.dto.BaseEntityDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.zhck.hwhsgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-03 17:29
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface HumitureService {
    ResultBean getHumitureBySdId(BaseEntityDTO baseEntityDTO, JSONObject jobj);

    ResultBean getHumMonthBySdId(HumitureDTO humitureDTO, JSONObject jobj);

    ResultBean getHumByDateSdId(HumitureDTO humitureDTO, JSONObject jobj);
}
