package com.zhixiangmain.api.service.cpjs.zdcpbom;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cpjs.zdcpbom.FoodBase;
import com.zhixiangmain.api.module.cpjs.zdcpbom.dto.FoodBaseDTO;
import com.zhixiangmain.web.responseConfig.PageDataResult;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.service.cpjs.zdcpbom
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-06 10:45
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FoodBaseService {
    PageDataResult getFoodBaseList(FoodBaseDTO foodBaseDTO, Integer page, Integer limit);

    PageDataResult getInsideFoodBaseList(FoodBaseDTO foodBaseDTO, Integer page, Integer limit);

    ResultBean checkName(FoodBase foodBase);

    ResultBean updateFoodBase(FoodBase foodBase);

    ResultBean addFoodBase(FoodBase foodBase);

    ResultBean getFoodBaseById(int id, Integer sdid);

    ResultBean delFoodBaseById(int id, Integer sdid);

    ResultBean getAllFood(FoodBase foodBase);

    ResultBean getAllFoodIdName(FoodBaseDTO FoodBaseDTO, JSONObject jobj);

    ResultBean getAllFoodBBySdId(FoodBaseDTO foodBaseDTO, Integer page, Integer limit, JSONObject jobj);

    ResultBean getCompositionBySdIdId(FoodBaseDTO foodBaseDTO, JSONObject jobj);
}
