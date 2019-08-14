package com.zhixiangmain.dao.jcxxpt.cyrygk;

import com.zhixiangmain.api.module.base.dto.CertificateWarningDTO;
import com.zhixiangmain.api.module.jcxxpt.cyrygk.dto.HealthCertificateDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.dao.jcxxpt.cyrygk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-21 13:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface HealthMapper {
    List<Map<String,Object>> findTopCertificateWarning(CertificateWarningDTO certificateWarningDTO);

    Integer findHasHealthBySdId(HealthCertificateDTO healthCertificateDTO);

    Integer findMatureHealthBySdId(HealthCertificateDTO healthCertificateDTO);

    Integer findOverdueHealthBySdId(HealthCertificateDTO healthCertificateDTO);
}
