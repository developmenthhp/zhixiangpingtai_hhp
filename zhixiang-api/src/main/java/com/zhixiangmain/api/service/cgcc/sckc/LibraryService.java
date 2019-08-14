package com.zhixiangmain.api.service.cgcc.sckc;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.cgcc.StockOMaterials.dto.LibrarySearchDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.api.service.cgcc.sckc
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-02-28 18:16
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryService {
    ResultBean getLibraries(LibrarySearchDTO librarySearchDTO, Integer page, Integer limit, JSONObject jobj);
}
