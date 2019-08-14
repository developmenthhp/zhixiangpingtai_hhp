package com.zhixiangmain.api.service.cpjs.cpmx;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDetailsBlendDTO;
import com.zhixiangmain.api.module.cpjs.cpmx.dto.FoodMenuDetailsDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.service.cpjs.cpmx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-09 13:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FoodMenuDetailService {
    ResultBean getBySdIdFMId(FoodMenuDetailsDTO foodMenuDetailsDTO, JSONObject jobj);

    ResultBean updateBlend(FoodMenuDetailsBlendDTO foodMenuDetailsBlendDTO, JSONObject jobj);
}
