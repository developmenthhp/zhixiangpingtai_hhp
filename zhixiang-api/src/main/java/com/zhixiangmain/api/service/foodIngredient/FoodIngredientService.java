package com.zhixiangmain.api.service.foodIngredient;

import com.zhixiangmain.api.module.foodIngredient.dto.FoodIngredientDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;
/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.service.foodIngredient
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-08 15:15
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FoodIngredientService {
    ResultBean getByFoodId(int foodId, Integer sdid);

    ResultBean setFoodIngredient(FoodIngredientDTO foodIngredientDTO);

    ResultBean getByByOutFoodId(int foodId);
}
