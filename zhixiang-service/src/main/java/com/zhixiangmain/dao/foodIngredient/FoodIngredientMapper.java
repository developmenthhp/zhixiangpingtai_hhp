package com.zhixiangmain.dao.foodIngredient;

import com.zhixiangmain.api.module.cpjs.zdcpbom.dto.FoodBaseDTO;
import com.zhixiangmain.api.module.foodIngredient.FoodIngredient;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.foodIngredient
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-08 15:25
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FoodIngredientMapper {
    List<Map<String,Object>> findByFoodId(@Param("foodId") int foodId, @Param("sdId") Integer sdId, @Param("sort") String sort);

    int updateFoodIngredient(FoodIngredient foodIngredient);

    int addFoodIngredient(FoodIngredient foodIngredient);

    int delFoodIngredientById(@Param("id") int id, @Param("sdId") Integer sid);

    List<Map<String,Object>> findByOutFoodId(@Param("foodId") int foodId);

    List<Map<String,Object>> findCompositionBySdIdId(FoodBaseDTO foodBaseDTO);
}
