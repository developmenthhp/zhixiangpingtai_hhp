package com.zhixiangmain.api.service.cgcc.scycg;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cgcc.scycg.MainLibraryPurchase;
import com.zhixiangmain.api.module.cgcc.scycg.dto.MainLibraryPurchaseDTO;
import com.zhixiangmain.web.responseConfig.PageDataResult;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-all-charge
 * @Package com.zhixiangyun.api.service.cgcc.scycg
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-03-19 16:51
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface MainLibraryPurchaseService {
    PageDataResult getMainLPList(MainLibraryPurchaseDTO mainLibraryPurchaseDTO, Integer page, Integer limit, JSONObject jobj);

    ResultBean updateStatusByIdSdId(MainLibraryPurchase mainLibraryPurchase, String[] authorizedPrices, JSONObject jobj);
}
