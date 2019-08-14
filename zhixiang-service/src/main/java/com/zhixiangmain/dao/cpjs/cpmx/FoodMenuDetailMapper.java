package com.zhixiangmain.dao.cpjs.cpmx;

import com.zhixiangmain.api.module.cpjs.cpmx.FoodMenuDetails;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDetailsDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.dao.cpjs.cpmx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-08 10:48
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FoodMenuDetailMapper {
    List<Map<String,Object>> findBySdIdFoodMenuId(FoodMenuDetailsDTO foodMenuDetailsDTO);

    Integer insertFoodMenuDetail(FoodMenuDetails foodMenuDetails);

    Integer updateFoodMenuDetail(FoodMenuDetails foodMenuDetails);

    Integer deleteFoodMenuDetail(Integer id);
}
