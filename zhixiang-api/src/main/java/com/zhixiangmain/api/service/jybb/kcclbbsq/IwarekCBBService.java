package com.zhixiangmain.api.service.jybb.kcclbbsq;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.jybb.kcclbbsq.dto.IwarekCBBDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.service.jybb.kcclbbsq
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-23 18:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IwarekCBBService {
    ResultBean getTotalBySdIdStatus(IwarekCBBDTO iwarekCBBDTO, JSONObject jobj);
}
