package com.zhixiangmain.dao.cgcc.ghsjc;

import com.zhixiangmain.api.module.cgcc.ghsjc.dto.IngredientSupportDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.dao.cgcc.ghsjc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-18 9:27
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IngredientSupportMapper {
    List<Map<String,Object>> findAllIngSupBySdId(IngredientSupportDTO ingredientSupportDTO);
}
