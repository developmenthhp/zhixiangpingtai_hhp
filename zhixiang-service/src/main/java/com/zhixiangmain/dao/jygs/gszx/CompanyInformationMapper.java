package com.zhixiangmain.dao.jygs.gszx;

import com.zhixiangmain.api.module.jygs.gszx.dto.CompanyInformationDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.dao.jygs.gszx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-03 13:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CompanyInformationMapper {
    List<Map<String,Object>> findCompanyInformations(CompanyInformationDTO companyInformationDTO);

    Integer findCompanyInformationsTotal(CompanyInformationDTO companyInformationDTO);
}
