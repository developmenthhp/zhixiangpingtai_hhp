package com.zhixiangmain.api.service.jygs.gsry;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.jygs.gsry.dto.CompanyGloriesDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.service.jygs.gsry
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-04-03 10:55
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CompanyGloriesService {

    ResultBean getCompanyGlories(CompanyGloriesDTO companyGloriesDTO, Integer page, Integer limit, JSONObject jobj);
}
