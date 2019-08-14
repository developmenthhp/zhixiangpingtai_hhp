package com.zhixiangmain.dao.cpjs.zdcpbom;

import com.zhixiangmain.api.module.cpjs.zdcpbom.FoodBase;
import com.zhixiangmain.api.module.cpjs.zdcpbom.dto.FoodBaseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.cpjs.zdcpbom
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-06 13:21
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FoodBaseMapper {
    List<Map<String,Object>> findFoodBaseList(FoodBaseDTO foodBaseDTO);

    List<Map<String,Object>> findInsideFoodBaseList(FoodBaseDTO foodBaseDTO);

    Object checkNameTotal(FoodBase foodBase);

    int updateFoodBase(FoodBase foodBase);

    int addFoodBase(FoodBase foodBase);

    Map<String,Object> findFoodBaseById(@Param("id") int id, @Param("sdId") Integer sdId);

    Integer delFoodBaseById(@Param("id") int id, @Param("sdId") Integer sdId);

    List<Map<String,Object>> getAllFood(FoodBase foodBase);

    List<Map<String,Object>> findAllFoodIdName(FoodBaseDTO FoodBaseDTO);

    Integer findPaginationDataTotal(FoodBaseDTO foodBaseDTO);

    List<Map<String,Object>> findAllFoodBBySdId(FoodBaseDTO foodBaseDTO);
}
