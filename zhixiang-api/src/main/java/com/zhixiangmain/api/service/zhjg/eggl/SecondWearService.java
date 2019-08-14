package com.zhixiangmain.api.service.zhjg.eggl;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.zhjg.eggl.dto.SecondWearDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.zhjg.eggl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-29 15:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SecondWearService {
    ResultBean getSWearBySdId(SecondWearDTO secondWearDTO, JSONObject jobj);
}
