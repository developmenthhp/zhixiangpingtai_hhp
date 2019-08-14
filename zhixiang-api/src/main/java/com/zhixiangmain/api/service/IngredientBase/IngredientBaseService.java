package com.zhixiangmain.api.service.IngredientBase;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto.BasicInfoFoodDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.IngredientBase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-31 10:16
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IngredientBaseService {
    ResultBean getMonthBySdId(BasicInfoFoodDTO basicInfoFoodDTO, JSONObject jobj);

    ResultBean getByDateSdId(BasicInfoFoodDTO basicInfoFoodDTO, JSONObject jobj);
}
