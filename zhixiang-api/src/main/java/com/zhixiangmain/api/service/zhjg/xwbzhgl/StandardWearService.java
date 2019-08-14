package com.zhixiangmain.api.service.zhjg.xwbzhgl;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.base.dto.AbnormalSnapshotDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-background-charge
 * @Package com.zhixiangyun.api.service.zhjg.xwbzhgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-22 16:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface StandardWearService {
    ResultBean getSWCharBySdId(AbnormalSnapshotDTO abnormalSnapshotDTO, JSONObject jobj);
}
