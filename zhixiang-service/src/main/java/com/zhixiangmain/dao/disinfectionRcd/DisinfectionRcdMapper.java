package com.zhixiangmain.dao.disinfectionRcd;

import com.zhixiangmain.api.module.base.dto.EmploymentViolationDTO;
import com.zhixiangmain.api.module.disinfectionRcd.DisinfectionRcd;
import com.zhixiangmain.api.module.disinfectionRcd.dto.DisinfectionRcdDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.disinfectionRcd
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-17 18:09
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface DisinfectionRcdMapper {
    DisinfectionRcd findToAll(@Param("sdId") int sdId);

    Integer findTotalEmploymentViolation(EmploymentViolationDTO employmentViolationDTO);

    Integer findDisByDateSdId(DisinfectionRcdDTO disinfectionRcdDTO);

    List<Map<String,Object>> findDisMonthBySdId(DisinfectionRcdDTO disinfectionRcdDTO);

    Integer findPaginationDataTotal(DisinfectionRcdDTO disinfectionRcdDTO);

    List<Map<String,Object>> findPaginationData(DisinfectionRcdDTO disinfectionRcdDTO);
}
