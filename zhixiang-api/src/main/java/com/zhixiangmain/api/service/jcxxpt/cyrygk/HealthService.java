package com.zhixiangmain.api.service.jcxxpt.cyrygk;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.jcxxpt.cyrygk.dto.HealthCertificateDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.service.jcxxpt.cyrygk
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-21 12:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface HealthService {
    ResultBean getHealthCharBySdId(HealthCertificateDTO healthCertificateDTO, JSONObject jobj);

    ResultBean getHealthTypeCharBySdId(HealthCertificateDTO healthCertificateDTO, JSONObject jobj);
}
