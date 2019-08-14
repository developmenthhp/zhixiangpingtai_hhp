package com.zhixiangmain.api.service.scgl.sckjgl;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.scgl.sckjgl.dto.FoodInspectionDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.scgl.sckjgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-30 16:29
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FoodInspectionService {
    ResultBean getByWeek(FoodInspectionDTO foodInspectionDTO, JSONObject jobj);

    ResultBean getFInsBySdId(FoodInspectionDTO foodInspectionDTO, JSONObject jobj);

    ResultBean getFInsByDateSdId(FoodInspectionDTO foodInspectionDTO, JSONObject jobj);
}
