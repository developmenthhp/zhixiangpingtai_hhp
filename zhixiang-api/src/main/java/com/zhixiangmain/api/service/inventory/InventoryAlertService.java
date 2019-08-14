package com.zhixiangmain.api.service.inventory;

import com.alibaba.fastjson.JSONObject;
import com.zhixiangmain.api.module.inventoryAlert.dto.InventoryAlertDTO;
import com.zhixiangmain.web.responseConfig.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package com.zhixiangmain.api.service.inventory
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-05-31 13:10
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface InventoryAlertService {
    ResultBean getIBMMonthBySdId(InventoryAlertDTO inventoryAlertDTO, JSONObject jobj);
}
