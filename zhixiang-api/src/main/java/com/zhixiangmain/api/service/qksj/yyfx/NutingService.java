package com.zhixiangmain.api.service.qksj.yyfx;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.qksj.yyfx.dto.NutingDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.qksj.yyfx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-18 11:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface NutingService {
    ResultBean getRankingListBySdId(NutingDTO nutingDTO, Integer page, Integer limit, JSONObject jobj);
}
