package com.zhixiangmain.dao.qxjp.scfgcz;

import com.zhixiangmain.api.module.base.dto.EmploymentViolationDTO;
import com.zhixiangmain.api.module.cgcc.BasicInfoFood.dto.BasicInfoFoodDTO;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.dao.qxjp.scfgcz
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-24 12:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface AirDryingWeightMapper {
    Integer findTotalEmploymentViolation(EmploymentViolationDTO employmentViolationDTO);

    Map<String, Object> findWeightByIngBaseId(BasicInfoFoodDTO basicInfoFoodDTO);
}
