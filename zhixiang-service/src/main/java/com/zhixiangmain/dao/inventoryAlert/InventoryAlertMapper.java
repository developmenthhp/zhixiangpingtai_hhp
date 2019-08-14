package com.zhixiangmain.dao.inventoryAlert;

import com.zhixiangmain.api.module.inventoryAlert.InventoryAlert;
import com.zhixiangmain.api.module.inventoryAlert.dto.InventoryAlertDTO;
import com.zhixiangmain.api.module.message.dto.NoticeCementDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-charge
 * @Package com.zhixiangyun.dao.inventoryAlert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019-01-18 9:13
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface InventoryAlertMapper {
    InventoryAlert findToFcznCon(@Param("sdId") int sdId);

    List<Map<String,Object>> findTopInventoryAlerts(NoticeCementDTO noticeCementDTO);

    List<Map<String,Object>> findIBMMonthBySdId(InventoryAlertDTO inventoryAlertDTO);
}
