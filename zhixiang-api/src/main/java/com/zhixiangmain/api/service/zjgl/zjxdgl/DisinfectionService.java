package com.zhixiangmain.api.service.zjgl.zjxdgl;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.disinfectionRcd.dto.DisinfectionRcdDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.zjgl.zjxdgl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-06-04 10:57
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface DisinfectionService {
    ResultBean getDisMonthBySdId(DisinfectionRcdDTO disinfectionRcdDTO, JSONObject jobj) throws Exception;

    ResultBean getDisByDateSdId(DisinfectionRcdDTO disinfectionRcdDTO, JSONObject jobj)throws Exception;

    ResultBean getPaginationData(DisinfectionRcdDTO disinfectionRcdDTO, Integer page, Integer limit, JSONObject jobj) throws Exception;
}
