package com.zhixiangmain.dao.cgcc.scrk;

import com.zhixiangmain.api.module.base.dto.TrialSigningDTO;
import com.zhixiangmain.api.module.cgcc.scrk.dto.IwareReportDTO;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.dao.cgcc.scrk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-23 17:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IwareReportMapper {
    Integer findTrialSigningCount(TrialSigningDTO trialSigningDTO);

    Integer findTotalBySdIdStatus(IwareReportDTO iwareReportDTO);
}
