package com.zhixiangmain.api.service.base;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.base.CategoryBase;
import com.zhixiangmain.api.module.base.dto.CategoryBaseDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.service.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-25 10:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CategoryBaseService {
    List<CategoryBase> getCategoryBaseByPid(Integer sdid, Integer pid);

    ResultBean getCategoryBaseByPidSdId(CategoryBaseDTO categoryBaseDTO, JSONObject jobj);
}
